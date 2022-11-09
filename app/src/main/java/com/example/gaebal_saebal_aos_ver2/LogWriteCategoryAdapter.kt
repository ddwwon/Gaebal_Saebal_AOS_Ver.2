package com.example.gaebal_saebal_aos_ver2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.LogWriteCategoryItemBinding
import kotlinx.coroutines.selects.select

class LogWriteCategoryAdapter(private val category: ArrayList<Category>): RecyclerView.Adapter<LogWriteCategoryAdapter.ViewHolder>() {

    private lateinit var binding: LogWriteCategoryItemBinding
    private var selectCategory:Int = 0
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    class ViewHolder(private val binding: LogWriteCategoryItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: Category){
            binding.selectCategoryBtn.text = data.contents

            binding.selectCategoryBtn.setOnClickListener{

            }
//
//            binding.switchItem.isChecked = data.checked
//
//            binding.switchItem.setOnClickListener{
//                data.checked = binding.switchItem.isChecked
//            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = LogWriteCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount() = category.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (viewHolder is ViewHolder){
            viewHolder.bind(category[position])
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