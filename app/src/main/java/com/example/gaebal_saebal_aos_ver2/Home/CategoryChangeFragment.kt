package com.example.gaebal_saebal_aos_ver2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.ActivityMainBinding
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentCategoryChangeBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryChangeFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentCategoryChangeBinding // viewBinding

    // Category DB 세팅
    private var db: AppDatabase? = null

    // 카테고리 변경 recyclerview adapter
    private val datas = mutableListOf<CategoryDataEntity>()
    private lateinit var changeCategoryAdapter: CategoryChangeAdapter

    // 선택된 기록들
    private var mRecordIds: ArrayList<Int>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCategoryChangeBinding.inflate(layoutInflater);

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 선택된 기록 id들 받아오기
        arguments?.let {
            mRecordIds = it.getIntegerArrayList("recordIds")
        }

        // db 세팅
        db = AppDatabase.getInstance(requireActivity())

        // recyclerview 세팅
        initCategoryChangeRecycler()

        // 저장된 카테고리 데이터 불러와서 추가
        val categoryDatas = db!!.categoryDataDao().getAllCategoryData()
        if(categoryDatas.isNotEmpty()) {
            for(i: Int in 0..(categoryDatas.size - 1)) {
                addData(categoryDatas[i])
            }
        }

        viewBinding.categoryChangeFrame.setOnClickListener{
            dismiss()
        }
    }

    // recyclerview 세팅
    private fun initCategoryChangeRecycler() {
        changeCategoryAdapter = CategoryChangeAdapter(
            requireContext(),
            onClickCategory = {
                changeCategory(it)
            }
        )
        viewBinding.categoryChangeRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.categoryChangeRecyclerview.adapter = changeCategoryAdapter
        changeCategoryAdapter.datas = datas
    }

    // 데이터 추가
    private fun addData(category: CategoryDataEntity) {
        datas.apply {
            add(category)
        }
        changeCategoryAdapter.notifyDataSetChanged()
    }

    // 카테고리 이동
    fun changeCategory(mCategoryId: Int) {
        // 선택된 기록들을 선택된 카테고리로 이동
        for(i: Int in 0..(mRecordIds!!.size-1)) {
            db?.recordDataDao()?.changeCategory(mRecordIds!![i], mCategoryId)
        }

        dismiss()
        // 메인 페이지로 이동
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(ActivityMainBinding.inflate(layoutInflater).fragmentLayout.id, MyRecordFragment())
            .commit()
    }

    override fun dismiss() {
        super.dismiss()
    }
}