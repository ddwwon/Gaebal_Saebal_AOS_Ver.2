package com.example.gaebal_saebal_aos_ver2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentGithubBinding
import com.example.gaebal_saebal_aos_ver2.databinding.GithubItemBinding
import com.example.gaebal_saebal_aos_ver2.databinding.LogWriteCategoryItemBinding

class GibhubItemAdapter(private val githubItemArray: ArrayList<githubItem>): RecyclerView.Adapter<GibhubItemAdapter.ViewHolder>() {
    private lateinit var viewBinding: GithubItemBinding
    class ViewHolder(private val viewBinding: GithubItemBinding) : RecyclerView.ViewHolder(viewBinding.root){
        fun bind(data: githubItem){
            viewBinding.githubDate.setText(data.gitDate.toString())
            viewBinding.githubTitle.setText(data.gitTitle)
            viewBinding.githubRepo.setText(data.gitRepo)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        viewBinding = GithubItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)

    }

    override fun getItemCount() = githubItemArray.size

    override fun onBindViewHolder(viewHolder: GibhubItemAdapter.ViewHolder, position: Int) {
        if (viewHolder is GibhubItemAdapter.ViewHolder){
            viewHolder.bind(githubItemArray[position])
        }
//        if (position == selectCategory){
//            binding.selectCategoryBtn.isChecked = true
//        }
//        else{
//            binding.selectCategoryBtn.isChecked = false
//        }
        viewHolder.itemView.setOnClickListener{
            itemClickListener.onClick(it, position)
        }
//        viewHolder.apply {
//            viewHolder.bind(category[position])
//        }


    }
}