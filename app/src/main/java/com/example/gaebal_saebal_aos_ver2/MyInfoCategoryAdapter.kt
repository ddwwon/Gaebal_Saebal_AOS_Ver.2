// 카테고리 추가/삭제 페이지 - 카테고리 리스트 recyclerview adapter
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.MyInfoCategoryItemBinding

class MyInfoCategoryAdapter(private val context: Context) :
    RecyclerView.Adapter<MyInfoCategoryAdapter.MyInfoCategoryViewHolder>() {

    var datas = mutableListOf<MyInfoCategoryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            MyInfoCategoryViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.my_info_category_item, parent, false)

        return MyInfoCategoryViewHolder(MyInfoCategoryItemBinding.bind(view))
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: MyInfoCategoryViewHolder, position: Int) {
        holder.bind(datas[position])

    }

    inner class MyInfoCategoryViewHolder(private val binding: MyInfoCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyInfoCategoryData) {
            binding.myInfoCategoryTitle.text = item.category
        }
    }
}