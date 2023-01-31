// 나의 기록
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentMyRecordBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity
import com.example.gaebal_saebal_aos_ver2.db_entity.RecordDataEntity


class MyRecordFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyRecordBinding // viewBinding

    // Room DB 세팅
    private var db: AppDatabase? = null

    // 카테고리 recyclerview adapter
    private var datas = mutableListOf<MyRecordCategoryData>()
    private lateinit var myRecordCategoryAdapter: MyRecordCategoryAdapter

    // 프래그먼트 전환을 위해
    var activity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    override fun onResume() {
        super.onResume()
        Log.d("Resume", "-----------------------------")

        // refresh
        activity?.refreshFragment(this)

        // db 세팅
        db = AppDatabase.getInstance(this.requireContext())

        // db에서 카테고리 데이터 불러오기
        val storedCategoryData: MutableList<CategoryDataEntity> = db!!.categoryDataDao().getAllCategoryData()

        // recyclerview data 리셋(초기화)
        datas = mutableListOf<MyRecordCategoryData>()

        // recyclerview 세팅
        initMyRecordCategoryRecycler()

        // 카테고리 recyclerview에 데이터 추가
        for(i: Int in 0..(storedCategoryData.size - 1)) {
            val storedContentsData: MutableList<RecordDataEntity>
                    = db!!.recordDataDao().getRecordFromCategory(storedCategoryData[i].category_uid)

            // contents는 최대 5개 보이고, 내용의 길이가 6자 이상일 경우 +...으로 축약
            var mContentsData: ArrayList<String> = ArrayList<String>()
            for(j: Int in 0..(storedContentsData.size - 1)) {
                if(j === 5) break // contents는 최대 5개

                // 줄바꿈 제거
                var mContent: String = storedContentsData[j].record_contents.replace("\\r\\n|\\r|\\n|\\n\\r".toRegex()," ")
                if(mContent.length > 6) {
                    mContent = mContent.substring(0 until 6) + "..."// 6글자까지 자르기
                }
                mContentsData.add(mContent)
            }

            // 데이터 추가
            addData(storedCategoryData[i].category_name, mContentsData)
        }
    }


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

        // 기록 작성 버튼 클릭
        viewBinding.recordWriteBtn.setOnClickListener {
            val intent = Intent(getActivity(), LogWriteActivity::class.java)
            intent.putExtra("mFragment", "Write") // 어떤 페이지로 전환할지에 대한 값
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    // recyclerview 세팅
    private fun initMyRecordCategoryRecycler() {
        myRecordCategoryAdapter = MyRecordCategoryAdapter(
            requireContext(),
            onClickCategory = {
                openCategory(it)
            })
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

    // 카테고리 세부 페이지(contents list) 열기
    private fun openCategory(category: String) {
        // 카테고리 세부 페이지로 이동
        // 카테고리 세부 페이지로 카테고리 정보 넘겨주기
        activity?.sendCategoryFromMyRecord(category)
    }
}