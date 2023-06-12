// 카테고리 세부 페이지 recyclerview adapter
package com.gaebalsaebal.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gaebalsaebal.gaebal_saebal_aos_ver2.databinding.MyContentsListItemBinding

class MyContentsListAdapter(
    private val context: Context,
    val onClickContent: (id: Int) -> Unit
    ) :
    RecyclerView.Adapter<MyContentsListAdapter.MyContentsListViewHolder>() {

    var datas = mutableListOf<MyContentsListData>()
    var checkboxList = mutableListOf<CheckBoxListData2>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            MyContentsListViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.my_contents_list_item, parent, false)

        return MyContentsListViewHolder(MyContentsListItemBinding.bind(view))
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: MyContentsListViewHolder, position: Int) {
        holder.bind(datas[position], checkboxList[position])

        holder.itemView.setOnClickListener{
            onClickContent.invoke(datas[position].contentId) // 기록 세부 페이지 이동 함수 호출 - 기록 id 정보 넘겨줌
        }

    }

    inner class MyContentsListViewHolder(private val binding: MyContentsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyContentsListData, check: CheckBoxListData2) {
            binding.contentsListDate.text = item.contentWriteDate
            binding.contentsListTitle.text = item.contentTitle
            //binding.contentsListTag.text = item.contentHashtag
            binding.myContentsHashtagRecyclerview.apply {
                adapter = MyContentsHashTagAdapter(context).build(item.contentHashtag)
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

            binding.contentsListCheckbox.isChecked = check.checked

            binding.contentsListCheckbox.setOnClickListener {
                if(binding.contentsListCheckbox.isChecked)
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