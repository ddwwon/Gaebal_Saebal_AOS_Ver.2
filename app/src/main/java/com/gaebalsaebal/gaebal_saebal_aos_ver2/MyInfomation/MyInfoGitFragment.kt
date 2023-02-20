// 설정 페이지 > 깃허브 사용자 설정 페이지
package com.gaebalsaebal.gaebal_saebal_aos_ver2

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.media.session.MediaSession
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.gaebalsaebal.gaebal_saebal_aos_ver2.databinding.FragmentMyInfoGitBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

lateinit var auth : String
lateinit var repoList: ArrayList<String>
lateinit var repoName: ArrayList<String>
lateinit var repoOwner: ArrayList<String>
lateinit var issueList: ArrayList<String>
lateinit var issueTitle: String
lateinit var issueCreated: String
lateinit var pullList: ArrayList<String>
lateinit var pullUrl: String
lateinit var pullCreated: String
lateinit var commitList: ArrayList<String>
lateinit var commitMessage: String
lateinit var commitDate: String

class MyInfoGitFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyInfoGitBinding // viewBinding
    var githubFragment: GithubFragment ?= null
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

        // 저장 버튼 클릭 시
        viewBinding.saveToken.setOnClickListener{
            // token 값 github에 올릴 때는 빼고 올리기, 안그러면 revoke 됨
            auth = "token " + viewBinding.inputToken.text.toString()
            MyApplication.prefs.setString("auth", "token " + viewBinding.inputToken.text.toString())
            // auth를 사용해서, 레포를 불러오면서 token에 오류가 있는지 확인
            MyInfoGitClient.getApi().getRepos(auth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    println("token right")
                    Toast.makeText(requireActivity(), "Token이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    // 이전 페이지로 이동
                    requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
                    requireActivity().supportFragmentManager.popBackStack()
                }, {e ->
                    Log.d(TAG, e.toString())
                    println("token error")
                    val dialog = TokenCheckDialogFragment(requireActivity(), this)
                    dialog.showDialog()
                })
        }
    }
}