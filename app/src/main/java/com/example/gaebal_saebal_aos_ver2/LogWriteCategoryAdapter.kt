package com.example.gaebal_saebal_aos_ver2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.LogWriteCategoryItemBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity
import kotlinx.coroutines.selects.select

class LogWriteCategoryAdapter(
        private val category: MutableList<CategoryDataEntity>,
        private var selectCheck: MutableList<Boolean>
    ): RecyclerView.Adapter<LogWriteCategoryAdapter.ViewHolder>() {
    private lateinit var binding: LogWriteCategoryItemBinding

    //select여부를 저장할 MutableList
    //private var selectCheck: MutableList<Boolean> = mutableListOf<Boolean>()

    /*init {
        //selectCheck.add(true)
        for(i: Int in (0..category.size - 1)){
            if(category[i].category_uid == checkCategoryId)
                selectCheck.add(true)
            else
                selectCheck.add(false)
        }
    }*/

    inner class ViewHolder(private val binding: LogWriteCategoryItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: CategoryDataEntity, position: Int, selectCheck: MutableList<Boolean>){
            binding.selectCategoryBtn.text = data.category_name
            binding.selectCategoryBtn.isChecked = selectCheck[position]

            binding.selectCategoryBtn.setOnClickListener {
                for (i: Int in (0..selectCheck.size - 1)) {
                    if (i == position) {
                        selectCheck[i] = true
                    } else {
                        selectCheck[i] = false
                    }
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LogWriteCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount() = category.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(category[position], position, selectCheck)
    }
}