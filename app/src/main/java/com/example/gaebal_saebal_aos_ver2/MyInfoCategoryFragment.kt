// 설정 페이지 > 카테고리 추가/삭제 페이지
package com.example.gaebal_saebal_aos_ver2

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentMyInfoCategoryBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity

class MyInfoCategoryFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyInfoCategoryBinding // viewBinding

    // Category DB 세팅
    private var db: AppDatabase? = null

    // 카테고리 세부 recyclerview adapter
    private val datas = mutableListOf<CategoryDataEntity>()
    private val checkData = mutableListOf<CheckBoxListData>()
    private lateinit var myInfoCategoryAdapter: MyInfoCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMyInfoCategoryBinding.inflate(layoutInflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // db 세팅
        db = AppDatabase.getInstance(requireActivity())

        // recyclerview 세팅
        initMyInfoCategoryRecycler()

        // 저장된 카테고리 데이터 불러와서 추가
        val categoryDatas = db!!.categoryDataDao().getAllCategoryData()
        if(categoryDatas.isNotEmpty()) {
            //db?.categoryDataDao()?.deleteAllCategoryData()
            /*datas.apply{
                addAll(categoryDatas)
            }*/
            for(i: Int in 0..(categoryDatas.size - 1)) {
                addData(categoryDatas[i].category_name)
            }
            //Log.d("Test", "--------------------------------")
            //Log.d("Test", categoryDatas[0].toString())
        }

        // 이전 버튼 클릭 시
        viewBinding.myInfoCategoryBackBtn.setOnClickListener {
            // 이전 페이지로 이동
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 체크박스 전체 선택/해제
        viewBinding.categorySetCheckboxAll.setOnCheckedChangeListener { _, isChecked ->
            myInfoCategoryAdapter.setAllCheck(isChecked)
            myInfoCategoryAdapter.notifyDataSetChanged()
        }

        // 카테고리 추가 버튼 클릭 시 - 입력창 열기/닫기
        viewBinding.addCategoryBtn.setOnClickListener {
            if(viewBinding.addCategoryInput.visibility == View.GONE)
                viewBinding.addCategoryInput.setVisibility(View.VISIBLE)
            else {
                viewBinding.myInfoCategoryEdittext.text = null
                viewBinding.addCategoryInput.setVisibility(View.GONE)
            }
        }
        
        // 카테고리 이름 입력 후 엔터 -> 카테고리 생성
        viewBinding.myInfoCategoryEdittext.setOnEditorActionListener(
            TextView.OnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.KEYCODE_ENTER) {
                    val newCategory = viewBinding.myInfoCategoryEdittext.text.toString()
                    viewBinding.myInfoCategoryEdittext.text = null

                    val mCategory = CategoryDataEntity(0, newCategory) // CategoryDataEntity 생성
                    db?.categoryDataDao()?.insertCategoryData(mCategory)          // DB에 추가

                    addData(newCategory)
                }
                false
        })

    }

    // recyclerview 세팅
    private fun initMyInfoCategoryRecycler() {
        myInfoCategoryAdapter = MyInfoCategoryAdapter(requireContext())
        viewBinding.myInfoCategoryRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.myInfoCategoryRecyclerview.adapter = myInfoCategoryAdapter
        myInfoCategoryAdapter.datas = datas
        myInfoCategoryAdapter.checkboxList = checkData
    }

    // 데이터 추가
    private fun addData(category: String) {
        datas.apply {
            add(CategoryDataEntity(0, category))
        }
        checkData.apply {
            add(CheckBoxListData(category, false))
        }
        myInfoCategoryAdapter.notifyDataSetChanged()
    }
}