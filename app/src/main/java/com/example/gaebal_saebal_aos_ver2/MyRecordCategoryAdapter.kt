// 메인 페이지에서 카테고리 목록 보이는 거 recyclerview adapter
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.MyRecordItemBinding

class MyRecordCategoryAdapter(
    private val context: Context,
    val onClickCategory: () -> Unit // content 클릭 시 onClickCategory 실행
    ) :
    RecyclerView.Adapter<MyRecordCategoryAdapter.MyRecordCategoryViewHolder>() {

    var datas = mutableListOf<MyRecordCategoryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            MyRecordCategoryViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.my_record_item, parent, false)

        return MyRecordCategoryViewHolder(MyRecordItemBinding.bind(view))
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: MyRecordCategoryViewHolder, position: Int) {
        holder.bind(datas[position])

        // content 클릭 시
        holder.itemView.setOnClickListener {
            onClickCategory.invoke() //페이지 이동 함수 호출
        }
    }

    inner class MyRecordCategoryViewHolder(private val binding: MyRecordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyRecordCategoryData) {
            binding.myRecordCategory.text = item.category
            //binding.myRecordContent.text = item.content
            binding.myRecordContentsRecyclerview.apply {
                adapter = MyRecordContentsAdapter(context).build(item.contents)
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }
}