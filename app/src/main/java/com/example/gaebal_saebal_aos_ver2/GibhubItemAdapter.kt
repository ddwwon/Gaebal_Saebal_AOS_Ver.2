package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentGithubBinding
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentMyInfoGitBinding
import com.example.gaebal_saebal_aos_ver2.databinding.GithubItemBinding
import com.example.gaebal_saebal_aos_ver2.databinding.LogWriteCategoryItemBinding

class GibhubItemAdapter(
    val context: Context,
    val onClickContent: (id: Int) -> Unit
):RecyclerView.Adapter<GibhubItemAdapter.ViewHolder>() {

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: GithubItemBinding

    // GithubFragment에 data를 넘겨주기 위해서 여기서 재선언한번 해줘야 함!
    var datalist = githubRepoData

    class ViewHolder(private val viewBinding: GithubItemBinding) : RecyclerView.ViewHolder(viewBinding.root){
        fun bind(data: GithubRepoData){
            viewBinding.githubType.visibility = View.GONE
            viewBinding.githubDate.visibility = View.GONE
            viewBinding.githubTitle.text = data.record_github_repo_name
            viewBinding.githubRepo.text = data.record_github_repo_fullname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = GithubItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = datalist.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(datalist[position])

        viewHolder.itemView.setOnClickListener {
            onClickContent.invoke(position)
            println("positoin: " + position)
        }

    }
}