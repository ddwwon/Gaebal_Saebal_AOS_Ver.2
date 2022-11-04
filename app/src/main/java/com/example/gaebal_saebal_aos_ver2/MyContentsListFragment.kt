// 메인 페이지 > 카테고리 내부(contents 리스트)
package com.example.gaebal_saebal_aos_ver2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentMyContentsListBinding

class MyContentsListFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyContentsListBinding // viewBinding

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

        initMyContentsListRecycler()
        addData()
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
    //date: String, title: String, tag: String
    private fun addData() {
        datas.apply {
            //add(MyContentsListData(category, contents))
            add(MyContentsListData("포인터란...?", "22/11/05 02:22 AM", "#C"))
            add(MyContentsListData("클래스와 상속", "22/11/06 03:52 PM", "#Python"))
            add(MyContentsListData("포인터란...?", "22/11/05 02:22 AM", "#C"))
            add(MyContentsListData("클래스와 상속", "22/11/06 03:52 PM", "#Python"))
            add(MyContentsListData("포인터란...?", "22/11/05 02:22 AM", "#C"))
            add(MyContentsListData("클래스와 상속", "22/11/06 03:52 PM", "#Python"))
            add(MyContentsListData("포인터란...?", "22/11/05 02:22 AM", "#C"))
            add(MyContentsListData("클래스와 상속", "22/11/06 03:52 PM", "#Python"))
            myContentsListAdapter.notifyDataSetChanged()
        }
    }
}