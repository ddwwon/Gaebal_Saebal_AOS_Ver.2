// 설정 페이지 > 카테고리 추가/삭제 페이지
package com.example.gaebal_saebal_aos_ver2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentMyInfoCategoryBinding

class MyInfoCategoryFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyInfoCategoryBinding // viewBinding

    // 임시 데이터 - 나중에 기기에 저장된 데이터 불러와서 사용할 것
    private val storedCategoryData = arrayListOf("자료구조", "알고리즘", "인공지능")

    // 카테고리 세부 recyclerview adapter
    private val datas = mutableListOf<MyInfoCategoryData>()
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

        // recyclerview 세팅
        initMyInfoCategoryRecycler()

        //데이터 추가
        for(i: Int in 0..(storedCategoryData.size - 1)) {
            addData(storedCategoryData[i])
        }

        // 이전 버튼 클릭 시
        viewBinding.myInfoCategoryBackBtn.setOnClickListener {
            // 이전 페이지로 이동
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    // recyclerview 세팅
    private fun initMyInfoCategoryRecycler() {
        myInfoCategoryAdapter = MyInfoCategoryAdapter(requireContext())
        viewBinding.myInfoCategoryRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.myInfoCategoryRecyclerview.adapter = myInfoCategoryAdapter
        myInfoCategoryAdapter.datas = datas
    }

    // 데이터 추가
    private fun addData(category: String) {
        datas.apply {
            add(MyInfoCategoryData(category))
            myInfoCategoryAdapter.notifyDataSetChanged()
        }
    }
}