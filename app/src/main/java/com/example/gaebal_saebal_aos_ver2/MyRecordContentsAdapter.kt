// 메인 페이지 카테고리 목록에 보이는 컨텐츠 recyclerview
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.MyRecordContentsItemBinding

class MyRecordContentsAdapter(private val context: Context) :
    RecyclerView.Adapter<MyRecordContentsAdapter.MyRecordContentsViewHolder>() {

    var datas = ArrayList<String>()

    fun build(i: ArrayList<String>): MyRecordContentsAdapter{
        datas = i
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            MyRecordContentsViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.my_record_contents_item, parent, false)

        return MyRecordContentsViewHolder(MyRecordContentsItemBinding.bind(view))
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: MyRecordContentsViewHolder, position: Int) {
        holder.bind(datas[position])

    }

    inner class MyRecordContentsViewHolder(private val binding: MyRecordContentsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.myRecordContent.text = item
        }
    }
}