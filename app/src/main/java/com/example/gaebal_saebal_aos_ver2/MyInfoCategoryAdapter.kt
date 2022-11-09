// 카테고리 추가/삭제 페이지 - 카테고리 리스트 recyclerview adapter
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.MyInfoCategoryItemBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity

class MyInfoCategoryAdapter(private val context: Context) :
    RecyclerView.Adapter<MyInfoCategoryAdapter.MyInfoCategoryViewHolder>() {

    private lateinit var viewBinding: MyInfoCategoryItemBinding

    var datas = mutableListOf<CategoryDataEntity>()
    var checkboxList = mutableListOf<CheckBoxListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            MyInfoCategoryViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.my_info_category_item, parent, false)

        viewBinding = MyInfoCategoryItemBinding.bind(view)

        return MyInfoCategoryViewHolder(MyInfoCategoryItemBinding.bind(view))
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: MyInfoCategoryViewHolder, position: Int) {
        holder.bind(datas[position], checkboxList[position])

    }

    inner class MyInfoCategoryViewHolder(private val binding: MyInfoCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryDataEntity, check: CheckBoxListData) {
            binding.myInfoCategoryTitle.text = item.category_name
            binding.myInfoCategoryCheckbox.isChecked = check.checked

            binding.myInfoCategoryCheckbox.setOnClickListener {
                if(binding.myInfoCategoryCheckbox.isChecked)
                    check.checked = true
                else
                    check.checked = false
            }
        }
    }

    // 체크박스 전체 선택/해제
    fun setAllCheck(boolean: Boolean) {
        for(ckbox in checkboxList) {
            if (ckbox.checked == !boolean) {
                ckbox.checked = boolean
            }
        }
    }
}