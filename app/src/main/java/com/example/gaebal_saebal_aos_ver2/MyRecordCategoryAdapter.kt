// 메인 페이지에서 카테고리 목록 보이는 거 recyclerview adapter
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.MyRecordItemBinding

class MyRecordCategoryAdapter(private val context: Context) :
    RecyclerView.Adapter<MyRecordCategoryAdapter.MyRecordCategoryViewHolder>() {

    var datas = mutableListOf<MyRecordCategoryData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            MyRecordCategoryViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.my_record_item, parent, false)
        //val binding = MyRecordItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return MyRecordCategoryViewHolder(MyRecordItemBinding.bind(view))
        //return MyRecordCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: MyRecordCategoryViewHolder, position: Int) {
        holder.bind(datas[position])

    }

    inner class MyRecordCategoryViewHolder(private val binding: MyRecordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyRecordCategoryData) {
            binding.myRecordCategory.text = item.category
            binding.myRecordContent.text = item.content
        }
    }
}