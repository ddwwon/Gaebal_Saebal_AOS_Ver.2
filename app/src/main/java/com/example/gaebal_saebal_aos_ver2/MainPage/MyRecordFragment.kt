// 나의 기록
package com.example.gaebal_saebal_aos_ver2.MainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentMyRecordBinding


class MyRecordFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyRecordBinding // viewBinding

    lateinit var myRecordCategoryAdapter: MyRecordCategoryAdapter
    val datas = mutableListOf<MyRecordCategoryData>()

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

        //initMyRecordCategoryRecycler()
    }

    private fun initMyRecordCategoryRecycler() {
        viewBinding.myRecordRecyclerview.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        viewBinding.myRecordRecyclerview.adapter = myRecordCategoryAdapter
        myRecordCategoryAdapter.datas = datas
        //memoAdapter.notifyDataSetChanged()

        datas.apply {
            add(MyRecordCategoryData("Test", "test"))
            add(MyRecordCategoryData("Test", "test"))
            add(MyRecordCategoryData("Test", "test"))
            myRecordCategoryAdapter.notifyDataSetChanged()
        }
    }

}