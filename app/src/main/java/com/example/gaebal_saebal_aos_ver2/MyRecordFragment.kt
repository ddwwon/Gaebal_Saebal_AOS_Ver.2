// 나의 기록
package com.example.gaebal_saebal_aos_ver2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentMyRecordBinding


class MyRecordFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyRecordBinding // viewBinding

    // 임시 데이터 - 나중에 기기에 저장된 데이터 불러와서 사용할 것
    private val storedCategoryData = arrayListOf("자료구조", "알고리즘", "인공지능")
    private val storedContentsData = arrayListOf(arrayListOf("스택", "큐", "그래프", "트리"),
                                                arrayListOf("dp", "분할정복"),
                                                arrayListOf("비지도학습", "지도학습", "기계학습"))

    // 카테고리 recyclerview adapter
    private val datas = mutableListOf<MyRecordCategoryData>()
    private lateinit var myRecordCategoryAdapter: MyRecordCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMyRecordBinding.inflate(layoutInflater);

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMyRecordCategoryRecycler()
        // 데이터 추가
        for(i: Int in 0..(storedCategoryData.size - 1)) {
            addData(storedCategoryData[i], storedContentsData[i])
        }
    }

    private fun initMyRecordCategoryRecycler() {
        myRecordCategoryAdapter = MyRecordCategoryAdapter(requireContext())
        //myRecordCategoryAdapter = MyRecordCategoryAdapter(this)
        viewBinding.myRecordRecyclerview.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        viewBinding.myRecordRecyclerview.adapter = myRecordCategoryAdapter
        myRecordCategoryAdapter.datas = datas
    }

    // 데이터 추가 - 카테고리, 컨텐츠들
    private fun addData(category: String, contents: ArrayList<String>) {
        datas.apply {
            add(MyRecordCategoryData(category, contents))
            myRecordCategoryAdapter.notifyDataSetChanged()
        }
    }

}