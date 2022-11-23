package com.example.gaebal_saebal_aos_ver2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentGithubBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_github.*
import kotlin.reflect.typeOf

lateinit var selectedRepoFullName: String

class GithubFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentGithubBinding
    private lateinit var GithubItemAdapter: GibhubItemAdapter
    private lateinit var GithubRepoItemAdapter: GithubRepoItemAdapter

    var activity: MainActivity? = null
    var datas = githubRepoData
    var sortedRepoData = mutableListOf<GithubData>()
    lateinit var selectedRepoOwner : String
    lateinit var selectedRepo : String

    override fun onCreate(savedInstanceState: Bundle?) {
//        auth = "token "
        // 사용자의 repo를 불러온다.
        gitHubGetRepoClient()
        super.onCreate(savedInstanceState)
    }

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
        // recycler를 호출하는 함수
        initGithubRecycelr()
    }

    private fun initGithubRecycelr() {
        // repo의 목록을 보여주는 adapter
        GithubItemAdapter = GibhubItemAdapter(
            requireContext(),
            // repo 목록을 클릭하면, 해당 repo의 index 값을 넘겨줌
            onClickContent = {
                goRepoDetail(it)
            }
        )
        viewBinding.githubListRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.githubListRecyclerview.adapter = GithubItemAdapter

        GithubItemAdapter.datalist = datas
    }

    private fun goRepoDetail(id: Int) {
        // 받은 id를 사용해 해당 repo에 맞는 issue, pr, commit을 불러옴
        receiveRepoInfoFromGithub(id)

    }

    override fun dismiss() {
        super.dismiss()
    }

    private fun dataSaveAndDismiss(id: Int) {
        recordGithubType = sortedRepoData[id].record_github_type
        recordGithubDate = sortedRepoData[id].record_github_date
        recordGithubTitle = sortedRepoData[id].record_github_title
        recordGithubRepo = selectedRepo
        dismiss()
    }

    // 사용자가 선택한 Github Repo의 issue, commit, pull request 내용 불러와서 정렬
    fun receiveRepoInfoFromGithub(id: Int) {

        // issue, pr, commit list에서 클릭하면 데이터를 저장함과 동시에 bottomsheet dismiss
        GithubRepoItemAdapter = GithubRepoItemAdapter(
            requireContext(),
            onClickContent = {
                dataSaveAndDismiss(it)
            } )

        // 사용자가 선택한 repo의 값을 저장함
        selectedRepo = datas[id].record_github_repo_name
        selectedRepoOwner = datas[id].record_github_repo_owner
        selectedRepoFullName = datas[id].record_github_repo_fullname

        // 해당 repo의 commit, issue, pull request를 불러옴
        gitHubRepoInfoClient()

        // 시간 순으로 정렬 후, 최대 15개까지 보여준다.
        githubData.sortBy { it.record_github_date }
        if (githubData.size > 15) {
            sortedRepoData = githubData.subList(0, 15)
        } else {
            sortedRepoData = githubData.subList(0, githubData.size)
        }

        // 불러온 list들을 보여준다.
        viewBinding.githubListRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.githubListRecyclerview.adapter = GithubRepoItemAdapter
        GithubRepoItemAdapter.datalist = sortedRepoData
//        GithubRepoItemAdapter.updateReceiptsList(sortedRepoData)
        GithubRepoItemAdapter.notifyDataSetChanged()
    }

    // 사용자의 repo 목록 불러옴
    fun gitHubGetRepoClient() {
        MyInfoGitClient.getApi().getRepos(auth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({items ->
                items.forEach {
                    githubRepoData.add(GithubRepoData(it.name, it.full_name, it.owner.login))
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
                    githubData.add(GithubData("issue", it.create_at, it.title) )
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
                    githubData.add(GithubData("pull request", it.create_at, it.url) )
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
                    githubData.add(GithubData("commit", it.commit.author.date, it.commit.message) )
                    println("getcom: " + it)
                }
            }, {e ->
                println("getCommitserror: " + e.toString())
            })
    }
}