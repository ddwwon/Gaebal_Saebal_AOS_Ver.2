// 카테고리 세부 페이지(Contents List)에서 보이는 해시태그 recyclerview adapter
package com.gaebalsaebal.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaebalsaebal.gaebal_saebal_aos_ver2.databinding.MyContentsHashtagItemBinding

class MyContentsHashTagAdapter(private val context: Context) :
    RecyclerView.Adapter<MyContentsHashTagAdapter.MyContentsHashTagViewHolder>() {

    var datas = mutableListOf<String>()

    fun build(i: MutableList<String>): MyContentsHashTagAdapter{
        datas = i
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            MyContentsHashTagViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.my_contents_hashtag_item, parent, false)

        return MyContentsHashTagViewHolder(MyContentsHashtagItemBinding.bind(view))
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: MyContentsHashTagViewHolder, position: Int) {
        holder.bind(datas[position])

    }

    inner class MyContentsHashTagViewHolder(private val binding: MyContentsHashtagItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            if(item != "")
                binding.contentsListTag.text = "#" + item
        }
    }
}