package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.GithubItemBinding

// Github Fragment에서 Repo를 선택했을 때, 해당 repo에 맞는 issue, pr, commit 내용 출력
class GithubRepoItemAdapter(
    val context: Context,
    val onClickContent: (id: Int) -> Unit
): RecyclerView.Adapter<GithubRepoItemAdapter.ViewHolder>() {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding: GithubItemBinding

    // GithubFragment에 data를 넘겨주기 위해서 여기서 재선언한번 해줘야 함!
    var datalist = mutableListOf<GithubData>()

    class ViewHolder(private val viewBinding: GithubItemBinding) : RecyclerView.ViewHolder(viewBinding.root){
        fun bind(data: GithubData, context: Context){
            viewBinding.githubType.visibility = View.VISIBLE
            if (data.record_github_type == "issue") {
                viewBinding.githubType.setImageDrawable(context.getResources().getDrawable(R.drawable.issue_icon))
            } else if(data.record_github_type == "pull request") {
                viewBinding.githubType.setImageDrawable(context.getResources().getDrawable(R.drawable.pull_request_icon))
            } else {
                viewBinding.githubType.setImageDrawable(context.getResources().getDrawable(R.drawable.commit_icon))
            }
            viewBinding.githubDate.visibility = View.VISIBLE
            viewBinding.githubDate.text = data.record_github_date
            viewBinding.githubTitle.text = data.record_github_title
            viewBinding.githubRepo.text = selectedRepoFullName
        }
    }

    fun updateReceiptsList(newlist: MutableList<GithubData>) {
        datalist.clear()
        datalist.addAll(newlist)
        this.notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = GithubItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = datalist.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(datalist[position], context)

        viewHolder.itemView.setOnClickListener {
            onClickContent.invoke(position)
            println("positoin: " + position)
        }
    }
}