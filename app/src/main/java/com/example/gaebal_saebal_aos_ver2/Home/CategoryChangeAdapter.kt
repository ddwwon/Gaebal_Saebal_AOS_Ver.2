package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.CategoryChangeItemBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity

class CategoryChangeAdapter (
        private val context: Context,
        val onClickCategory: (mCategoryId: Int) -> Unit // content 클릭 시 onClickCategory 실행
    ) :
    RecyclerView.Adapter<CategoryChangeAdapter.CategoryChangeViewHolder>() {

    var datas = mutableListOf<CategoryDataEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            CategoryChangeViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.category_change_item, parent, false)


        return CategoryChangeViewHolder(CategoryChangeItemBinding.bind(view))
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: CategoryChangeViewHolder, position: Int) {
        holder.bind(datas[position])

        // content 클릭 시
        holder.itemView.setOnClickListener {
            onClickCategory.invoke(datas[position].category_uid) // 카테고리 이동 함수 호출
        }
    }

    inner class CategoryChangeViewHolder(private val binding: CategoryChangeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryDataEntity) {
            binding.changeCategoryName.text = item.category_name
        }
    }
}