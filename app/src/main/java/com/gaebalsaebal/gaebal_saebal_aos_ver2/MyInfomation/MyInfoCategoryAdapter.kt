// 카테고리 추가/삭제 페이지 - 카테고리 리스트 recyclerview adapter
package com.gaebalsaebal.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gaebalsaebal.gaebal_saebal_aos_ver2.databinding.MyInfoCategoryItemBinding
import com.gaebalsaebal.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity

class MyInfoCategoryAdapter(
        private val context: Context,
        val db: AppDatabase
    ) :
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

            // 카테고리명 터치 -> 수정 가능
            binding.myInfoCategoryTitle.setOnClickListener(View.OnClickListener {
                binding.myInfoCategoryTitle.visibility = View.GONE          // 카테고리명 숨기기

                binding.myInfoCategoryTitleRename.visibility = View.VISIBLE // 입력창 보이기
                binding.myInfoCategoryTitleRename.setText(binding.myInfoCategoryTitle.text.toString()) // 입력창에 변경전 카테고리명으로 세팅
            })

            // 변경할 카테고리명으로 수정 후 엔터 -> 카테고리명 수정
            binding.myInfoCategoryTitleRename.setOnEditorActionListener(
                TextView.OnEditorActionListener { textView, actionId, keyEvent ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        val preCategory = viewBinding.myInfoCategoryTitle.text.toString()
                        val renameCategory = viewBinding.myInfoCategoryTitleRename.text.toString()
                        //viewBinding.myInfoCategoryTitleRename.text = null

                        if(renameCategory == "") {
                            Toast.makeText(context, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                        else if(db?.categoryDataDao()?.checkExistCategoryData(renameCategory).toString().toInt() == 0) {
                            db?.categoryDataDao()?.renameCategory(preCategory, renameCategory) // db에서 카테고리명 수정

                            viewBinding.myInfoCategoryTitle.text = renameCategory // 화면에서 카테고리명 수정
                            viewBinding.myInfoCategoryTitleRename.text = null     // 입력창 초기화

                            binding.myInfoCategoryTitle.visibility = View.VISIBLE    // 카테고리명 보이기
                            binding.myInfoCategoryTitleRename.visibility = View.GONE // 입력창 숨기기
                        }
                        else {
                            Toast.makeText(context, "카테고리명은 중복될 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    false
                }
            )

            // 체크박스 선택 시
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