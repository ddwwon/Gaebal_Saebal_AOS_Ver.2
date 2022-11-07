// 카테고리 세부 페이지(Contents List)에서 보이는 해시태그 recyclerview adapter
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gaebal_saebal_aos_ver2.databinding.MyContentsHashtagItemBinding

class MyContentsHashTagAdapter(private val context: Context) :
    RecyclerView.Adapter<MyContentsHashTagAdapter.MyContentsHashTagViewHolder>() {

    var datas = ArrayList<String>()

    fun build(i: ArrayList<String>): MyContentsHashTagAdapter{
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
            binding.contentsListTag.text = "#" + item
        }
    }
}