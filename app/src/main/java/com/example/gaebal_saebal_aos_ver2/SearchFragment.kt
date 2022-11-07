// 검색 페이지
package com.example.gaebal_saebal_aos_ver2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var viewBinding: FragmentSearchBinding // viewBinding

    // 임시 데이터 - 나중에 기기에 저장된 데이터 불러와서 사용할 것
    private val storedCategoryData = arrayListOf("자료구조", "알고리즘")
    private val storedContentsData = arrayListOf(
        arrayListOf(arrayListOf("스택", "22/11/05 02:22 AM")),
        arrayListOf(arrayListOf("dp", "22/11/05 02:22 AM"), arrayListOf("분할정복이란 무엇인가", "22/11/05 02:22 AM")))
    private val storedContentsHashTag = arrayListOf(
        arrayListOf(arrayListOf("C", "Test")),
        arrayListOf(arrayListOf("Java", "Test"), arrayListOf("Test")))

    // 검색 결과 recyclerview adapter
    private val datas = mutableListOf<SearchResultData>()
    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSearchBinding.inflate(layoutInflater);

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerview 세팅 및 데이터 추가
        initSearchResultRecycler()
        for(i: Int in 0..(storedCategoryData.size - 1)) {
            for(j: Int in 0..(storedContentsData[i].size - 1)) {
                addData(storedCategoryData[i], storedContentsData[i][j][1], storedContentsData[i][j][0], storedContentsHashTag[i][j])
            }
        }
    }

    // recyclerview 세팅
    private fun initSearchResultRecycler() {
        searchResultAdapter = SearchResultAdapter(requireContext())
        viewBinding.searchResultRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.searchResultRecyclerview.adapter = searchResultAdapter
        searchResultAdapter.datas = datas
    }

    // 데이터 추가
    // tag도 recyclerview로 바꿔야 함 -> 여러 개 나올 수 있으므로
    // date: String, title: String, tag: String
    private fun addData(category: String, date: String, title: String, tag: ArrayList<String>) {
        datas.apply {
            //add(MyContentsListData(category, contents))
            add(SearchResultData(category, date, title, tag))
            searchResultAdapter.notifyDataSetChanged()
        }
    }

}