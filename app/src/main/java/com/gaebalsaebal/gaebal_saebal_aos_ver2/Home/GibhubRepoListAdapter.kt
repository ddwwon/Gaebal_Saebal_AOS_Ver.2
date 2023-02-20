package com.gaebalsaebal.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaebalsaebal.gaebal_saebal_aos_ver2.databinding.GithubItemBinding

// 레포지토리 목록을 보여줌
class GibhubRepoListAdapter(
    val context: Context,
    val onClickContent: (id: Int) -> Unit
):RecyclerView.Adapter<GibhubRepoListAdapter.ViewHolder>() {

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: GithubItemBinding

    // GithubFragment에 data를 넘겨주기 위해서 여기서 재선언한번 해줘야 함!
    var datalist: MutableList<GithubRepoData> = mutableListOf<GithubRepoData>()

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
            println("position: " + position)
        }

    }
}