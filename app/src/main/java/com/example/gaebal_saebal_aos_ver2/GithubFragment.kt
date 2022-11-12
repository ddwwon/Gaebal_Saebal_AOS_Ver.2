package com.example.gaebal_saebal_aos_ver2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentGithubBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.util.*
import java.util.Calendar.getInstance
import kotlin.collections.ArrayList

data class githubItem(val gitType:String, val gitDate: Date, val gitTitle: String, val gitRepo: String)
class GithubFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentGithubBinding
    private lateinit var GithubItemAdapter: GibhubItemAdapter

    var activity: MainActivity? = null
    var githubItemArray = ArrayList<githubItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

//        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//        val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        viewBinding = FragmentGithubBinding.inflate(layoutInflater)

        viewBinding.gitgubFrame.setOnClickListener{
            dismiss()
        }

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        githubItemArray.add(githubItem("issue", Date(2022, 6, 8), "firebase 오류", "ddwwwon/gaebalsaebal"))
        githubItemArray.add(githubItem("commit", Date(2022, 6, 8), "커밋 충돌", "ddwwwon/gaebalsaebal"))
        githubItemArray.add(githubItem("pull request", Date(2022, 6, 8), "풀 리퀘", "ddwwwon/gaebalsaebal"))
        githubItemArray.add(githubItem("pull request", Date(2022, 6, 8), "풀 리퀘", "ddwwwon/gaebalsaebal"))
        githubItemArray.add(githubItem("pull request", Date(2022, 6, 8), "풀 리퀘", "ddwwwon/gaebalsaebal"))
        githubItemArray.add(githubItem("pull request", Date(2022, 6, 8), "풀 리퀘", "ddwwwon/gaebalsaebal"))


        GithubItemAdapter = GibhubItemAdapter(this.githubItemArray)
        GithubItemAdapter.setItemClickListener(object :
        GibhubItemAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

            }
        })

        viewBinding.githubListRecyclerview.adapter = GithubItemAdapter
    }

    override fun dismiss() {
        super.dismiss()
        println("dismisswork")
//        MainActivity.getInstance()?.onFragmentChange("GithubInput")
    }


}