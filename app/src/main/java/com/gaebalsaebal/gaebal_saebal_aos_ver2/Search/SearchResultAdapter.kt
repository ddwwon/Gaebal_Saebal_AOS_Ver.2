// 검색 결과 리스트 recyclerview adapter
package com.gaebalsaebal.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gaebalsaebal.gaebal_saebal_aos_ver2.databinding.SearchResultItemBinding

class SearchResultAdapter(
        private val context: Context,
        val onClickContent: (id: Int) -> Unit
    ) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    var datas = mutableListOf<SearchResultData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            SearchResultViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false)

        return SearchResultViewHolder(SearchResultItemBinding.bind(view))
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(datas[position])

        holder.itemView.setOnClickListener{
            onClickContent.invoke(datas[position].contentId) // 기록 세부 페이지 이동 함수 호출 - 기록 id 정보 넘겨줌
        }
    }

    inner class SearchResultViewHolder(private val binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchResultData) {
            binding.searchResultCategory.text = item.contentCategory
            binding.searchResultDate.text = item.contentWriteDate
            binding.searchResultTitle.text = item.contentTitle
            //binding.contentsListTag.text = item.contentHashtag
            binding.searchResultHashtagRecyclerview.apply {
                adapter = MyContentsHashTagAdapter(context).build(item.contentHashtag)
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }
}