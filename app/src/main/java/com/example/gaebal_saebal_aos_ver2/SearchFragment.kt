// 검색 페이지
package com.example.gaebal_saebal_aos_ver2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentSearchBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.RecordDataEntity

class SearchFragment : Fragment() {
    private lateinit var viewBinding: FragmentSearchBinding // viewBinding

    // Room DB 세팅
    private var db: AppDatabase? = null

    // 검색 결과 recyclerview adapter
    private var datas = mutableListOf<SearchResultData>()
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

        // db 세팅
        db = AppDatabase.getInstance(this.requireContext())

        // recyclerview 세팅 및 데이터 추가
        initSearchResultRecycler()

        // 검색창 입력 후 엔터
        viewBinding.searchEditText.setOnEditorActionListener(
            TextView.OnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val mSearchWord = viewBinding.searchEditText.text.toString() // 검색어

                    // 검색 결과 리스트 초기화
                    datas = mutableListOf<SearchResultData>()

                    // recyclerview 세팅 및 데이터 추가
                    initSearchResultRecycler()

                    // 검색 결과
                    val searchResults: MutableList<RecordDataEntity> = db!!.recordDataDao().searchResult(mSearchWord)

                    // 검색 결과 데이터 삽입
                    for(i: Int in 0..(searchResults.size - 1)) {
                        val mCategory = db!!.categoryDataDao().getCategoryName(searchResults[i].record_category_uid)
                        // 컨텐츠 내용 - 줄바꿈 제거
                        var mContent: String = searchResults[i].record_contents.replace("\\r\\n|\\r|\\n|\\n\\r".toRegex()," ")
                        if(mContent.length > 15) {
                            mContent = mContent.substring(0 until 15) + "..."// 15글자까지 자르기
                        }
                        var hashTags: MutableList<String> = searchResults[i].record_tags?.split(";")!!.toMutableList()
                        addData(mCategory, searchResults[i].record_date.toString(), mContent, hashTags)
                    }
                }
                false
        })
    }

    // recyclerview 세팅
    private fun initSearchResultRecycler() {
        searchResultAdapter = SearchResultAdapter(requireContext())
        viewBinding.searchResultRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.searchResultRecyclerview.adapter = searchResultAdapter
        searchResultAdapter.datas = datas
    }

    // 데이터 추가
    // tag도 recyclerview로
    // date: String, title: String, tag: String
    private fun addData(category: String, date: String, title: String, tag: MutableList<String>) {
        datas.apply {
            //add(MyContentsListData(category, contents))
            add(SearchResultData(category, date, title, tag))
            searchResultAdapter.notifyDataSetChanged()
        }
    }
}