// 설정 페이지 > 깃허브 사용자 설정 페이지
package com.example.gaebal_saebal_aos_ver2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentMyInfoGitBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyInfoGitFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyInfoGitBinding // viewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMyInfoGitBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이전 버튼 클릭 시
        viewBinding.myInfoGitBackBtn.setOnClickListener {
            // 이전 페이지로 이동
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }

        viewBinding.saveToken.setOnClickListener{
            gitHubClient()
        }
    }

    fun gitHubClient() {
        var auth = "token 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"
//        var auth = "token " + viewBinding.inputToken.text.toString()
        var selectedRepoOwner = "ddwwon"
        var selectedRepo = "GAEBAL_SAEBAL_AOS"
        var test = arrayListOf<String>()

        MyInfoGitClient.getApi().getRepos(auth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({items ->
                items.forEach {println("getRepos: " + it)}
                items.forEach {test.add(it.toString())}
            }, {e ->
                println("getReposerror: " + e.toString())
            })
        test.forEach {"test pritn: "+ println(it)}

        MyInfoGitClient.getApi().getIssues(auth,
            selectedRepoOwner,
            selectedRepo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({items ->
                items.forEach {println("getIssues: " + it)}
            }, {e ->
                println("getIssueserror: " + e.toString())
            })

        MyInfoGitClient.getApi().getPRs(auth,
            selectedRepoOwner,
            selectedRepo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({items ->
                items.forEach {println("getPRs: " + it)}
            }, {e ->
                println("getPRserror: " +e.toString())
            })

        MyInfoGitClient.getApi().getCommits(auth,
            selectedRepoOwner,
            selectedRepo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({items ->
                items.forEach {println("getCommits: " + it)}
            }, {e ->
                println("getCommitserror: " + e.toString())
            })
    }
}