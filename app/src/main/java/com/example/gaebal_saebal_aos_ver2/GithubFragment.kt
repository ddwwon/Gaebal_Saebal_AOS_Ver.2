package com.example.gaebal_saebal_aos_ver2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentGithubBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

lateinit var selectedRepoFullName: String

class GithubFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentGithubBinding
    private lateinit var GithubRepoListAdapter: GibhubRepoListAdapter
    private lateinit var GithubRepoItemAdapter: GithubRepoItemAdapter

    // Github api로 받아온 값 저장
    var githubRepoData: MutableList<GithubRepoData> = mutableListOf<GithubRepoData>() // 레포지토리
    var githubData: MutableList<GithubData> = mutableListOf<GithubData>()             // 레포지토리의 commit, pr, issue

    var activity: MainActivity? = null
    var selectedRepoOwner : String? = null
    var selectedRepo : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        viewBinding = FragmentGithubBinding.inflate(layoutInflater)

        // bottom sheet 밖의 영역을 클릭하면, bottom sheet dismiss
        viewBinding.githubFrame.setOnClickListener{
            dismiss()
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // repo 보여주는 recycler 초기화
        initGithubRepoRecycler()

        //auth = "token "
        auth = MyApplication.prefs.getString("auth", "")

        // 사용자의 repo를 불러온다.
        gitHubGetRepoClient()

        // repo 선택된 경우
        if(!selectedRepo.isNullOrBlank()) {
            Log.d("sort", "--------------------------")
            // 정렬
            githubData.sortBy { it.record_github_date }

            // 15개 이하로 보여주기
            if (githubData.size > 15) {
                githubData = githubData.subList(0, 15)
            }

            GithubRepoItemAdapter.notifyDataSetChanged()
        }
    }

    // repo 보여주는 recycler 초기화
    private fun initGithubRepoRecycler() {
        // repo의 목록을 보여주는 adapter
        GithubRepoListAdapter = GibhubRepoListAdapter(
            requireContext(),
            // repo 목록을 클릭하면, 해당 repo의 index 값을 넘겨줌
            onClickContent = {
                goRepoDetail(it)
            }
        )
        viewBinding.githubListRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.githubListRecyclerview.adapter = GithubRepoListAdapter

        GithubRepoListAdapter.datalist = githubRepoData
    }

    // commit, pr, issue 리스트 보여주는 recycler 초기화
    private fun initGithubItemRecycler() {
        // issue, pr, commit list에서 클릭하면 데이터를 저장함과 동시에 bottomsheet dismiss
        GithubRepoItemAdapter = GithubRepoItemAdapter(
            requireContext(),
            onClickContent = {
                dataSaveAndDismiss(it)
            } )

        viewBinding.githubListRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.githubListRecyclerview.adapter = GithubRepoItemAdapter

        GithubRepoItemAdapter.datalist = githubData
    }

    // 받은 id를 사용해 해당 repo에 맞는 issue, pr, commit을 불러옴
    // 사용자가 선택한 Github Repo의 issue, commit, pull request 내용 불러와서 정렬
    private fun goRepoDetail(id: Int) {

        // commit, pr, issue 리스트 보여주는 recycler 초기화
        initGithubItemRecycler()

        // 사용자가 선택한 repo의 값을 저장함
        selectedRepo = githubRepoData[id].record_github_repo_name
        selectedRepoOwner = githubRepoData[id].record_github_repo_owner
        selectedRepoFullName = githubRepoData[id].record_github_repo_fullname

        // 해당 repo의 commit, issue, pull request를 불러옴
        gitHubRepoInfoClient()
    }

    // commit, pr, issue 합한 갯수가 15개가 넘어가면 호출되는 함수
    private fun cutGihubList() {
        // 오름차순 정렬
        githubData.sortBy { it.record_github_date }
        // 역순으로 바꾸기 - 최신순으로 보여주기 위해
        githubData.reverse()

        // 15개 이하로 보여주기 위해 맨 마지막 내용을 삭제
        githubData.remove(githubData[githubData.size - 1])

        GithubRepoItemAdapter.notifyDataSetChanged()
    }

    override fun dismiss() {
        super.dismiss()
    }

    private fun dataSaveAndDismiss(id: Int) {
        // 선택한 값
        recordGithubType = githubData[id].record_github_type
        recordGithubDate = githubData[id].record_github_date
        recordGithubTitle = githubData[id].record_github_title
        recordGithubRepo = selectedRepo!!

        // 선택한 값이 작성 페이지에 보일 수 있도록
        var currentFragment: MutableList<Fragment> = mutableListOf<Fragment>()

        // 현재 프래그먼트 찾기
        for (fragment: Fragment in requireActivity().supportFragmentManager.fragments) {
            if (fragment.isVisible) {
                currentFragment.add(fragment)
            }
        }
        currentFragment[currentFragment.size - 2]!!.onResume()

        // 창 닫기
        dismiss()
    }

    // 사용자의 repo 목록 불러옴
    fun gitHubGetRepoClient() {
        MyInfoGitClient.getApi().getRepos(auth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({items ->
                items.forEach {
                    githubRepoData.add(GithubRepoData(it.name, it.full_name, it.owner.login))
                    GithubRepoListAdapter.notifyDataSetChanged()
                }
            }, {e ->
                println("getReposerror: " + e.toString())
            })
    }

    // 사용자가 선택한 repo의 issue, pr, commit 목록 불러옴
    fun gitHubRepoInfoClient() {
        MyInfoGitClient.getApi().getIssues(auth,
            selectedRepoOwner,
            selectedRepo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({items ->
                items.forEach {
                    githubData.add(GithubData("issue", it.create_at, it.title))
                    if (githubData.size > 15) cutGihubList() // 15개 넘어갈 때마다 정렬하고 자르기
                    GithubRepoItemAdapter.notifyDataSetChanged()
                    println("getissue: " + it)
                }
            }, {e ->
                println("getIssueserror: " + e.toString())
            })

        MyInfoGitClient.getApi().getPRs(auth,
            selectedRepoOwner,
            selectedRepo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({items ->
                items.forEach {
                    githubData.add(GithubData("pull request", it.create_at, it.url))
                    if (githubData.size > 15) cutGihubList() // 15개 넘어갈 때마다 정렬하고 자르기
                    GithubRepoItemAdapter.notifyDataSetChanged()
                    println("getpr: " + it)
                }
            }, {e ->
                println("getPRserror: " +e.toString())
            })

        MyInfoGitClient.getApi().getCommits(auth,
            selectedRepoOwner,
            selectedRepo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({items ->
                items.forEach {
                    githubData.add(GithubData("commit", it.commit.author.date, it.commit.message))
                    if (githubData.size > 15) cutGihubList() // 15개 넘어갈 때마다 정렬하고 자르기
                    GithubRepoItemAdapter.notifyDataSetChanged()
                    println("getcom: " + it)
                }
            }, {e ->
                println("getCommitserror: " + e.toString())
            })
    }
}