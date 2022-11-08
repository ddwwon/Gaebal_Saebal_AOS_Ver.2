// 카테고리 세부 페이지 recyclerview adapter
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.MyContentsListItemBinding

class MyContentsListAdapter(private val context: Context) :
    RecyclerView.Adapter<MyContentsListAdapter.MyContentsListViewHolder>() {

    var datas = mutableListOf<MyContentsListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            MyContentsListViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.my_contents_list_item, parent, false)

        return MyContentsListViewHolder(MyContentsListItemBinding.bind(view))
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: MyContentsListViewHolder, position: Int) {
        holder.bind(datas[position])

        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it, position)
        }

    }
    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class MyContentsListViewHolder(private val binding: MyContentsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyContentsListData) {
            binding.contentsListDate.text = item.contentWriteDate
            binding.contentsListTitle.text = item.contentTitle
            //binding.contentsListTag.text = item.contentHashtag
            binding.myContentsHashtagRecyclerview.apply {
                adapter = MyContentsHashTagAdapter(context).build(item.contentHashtag)
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }
}