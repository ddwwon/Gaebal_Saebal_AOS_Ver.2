// 메인 페이지 > 카테고리 내부(contents 리스트)
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentMyContentsListBinding

class MyContentsListFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyContentsListBinding // viewBinding

    // 임시 데이터 - 나중에 기기에 저장된 데이터 불러와서 사용할 것
    private val storedCategoryData = arrayListOf("자료구조", "알고리즘", "인공지능")
    private val storedContentsData = arrayListOf(
        arrayListOf(arrayListOf("스택", "22/11/05 02:22 AM", "#C"), arrayListOf("큐", "22/11/05 02:22 AM", "#C"), arrayListOf("그래프", "22/11/05 02:22 AM", "#C"), arrayListOf("트리", "22/11/05 02:22 AM", "#C")),
        arrayListOf(arrayListOf("dp", "22/11/05 02:22 AM", "#C"), arrayListOf("분할정복이란 무엇인가", "22/11/05 02:22 AM", "#C")),
        arrayListOf(arrayListOf("비지도학습이란 무엇인가", "22/11/05 02:22 AM", "#C"), arrayListOf("지도학습", "22/11/05 02:22 AM", "#C"), arrayListOf("기계학습", "22/11/05 02:22 AM", "#C")))

    // 카테고리
    private var mCategory: String? = null

    // 카테고리 세부 recyclerview adapter
    private val datas = mutableListOf<MyContentsListData>()
    private lateinit var myContentsListAdapter: MyContentsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMyContentsListBinding.inflate(layoutInflater);

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 메인 페이지에서 넘어온 카테고리 데이터 받아오기
        arguments?.let {
            mCategory = it.getString("category")
        }

        // 페이지에 보이는 카테고리
        viewBinding.contentsListCategory.text = mCategory

        // recyclerview 세팅 및 데이터 추가
        initMyContentsListRecycler()
        for(i: Int in 0..(storedCategoryData.size - 1)) {
            // 메인페이지에서 선택했던 카테고리에 대한 데이터로
            Log.d("Test", "---------------------")
            Log.d("Test", mCategory + "")
            if(mCategory == storedCategoryData[i]) {
                for(j: Int in 0..(storedContentsData[i].size - 1)) {
                    addData(storedContentsData[i][j][1], storedContentsData[i][j][0], storedContentsData[i][j][2])
                }
                break
            }
        }

        // 이전 버튼 클릭 시
        viewBinding.contentsListBackBtn.setOnClickListener {
            // 이전 페이지로 이동
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    // recyclerview 세팅
    private fun initMyContentsListRecycler() {
        myContentsListAdapter = MyContentsListAdapter(requireContext())
        viewBinding.myContentsListRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.myContentsListRecyclerview.adapter = myContentsListAdapter
        myContentsListAdapter.datas = datas
    }

    // 데이터 추가
    // tag도 recyclerview로 바꿔야 함 -> 여러 개 나올 수 있으므로
    // date: String, title: String, tag: String
    private fun addData(date: String, title: String, tag: String) {
        datas.apply {
            //add(MyContentsListData(category, contents))
            add(MyContentsListData(date, title, tag))
            myContentsListAdapter.notifyDataSetChanged()
        }
    }
}