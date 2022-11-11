// 메인 페이지 > 카테고리 내부(contents 리스트)
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
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentMyContentsListBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity
import com.example.gaebal_saebal_aos_ver2.db_entity.RecordDataEntity

class MyContentsListFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyContentsListBinding // viewBinding

    // Room DB 세팅
    private var db: AppDatabase? = null

    // 카테고리
    private var mCategory: String = ""

    // 카테고리 세부 recyclerview adapter
    private var datas = mutableListOf<MyContentsListData>()
    private val checkData = mutableListOf<CheckBoxListData2>()
    private lateinit var myContentsListAdapter: MyContentsListAdapter

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

        // refresh
        activity?.refreshFragment(this)

        // 메인 페이지에서 넘어온 카테고리 데이터 받아오기
        arguments?.let {
            mCategory = it.getString("category").toString()
        }

        // 페이지에 보이는 카테고리
        viewBinding.contentsListCategory.text = mCategory

        // db 세팅
        db = AppDatabase.getInstance(this.requireContext())

        // 데이터 초기화
        datas = mutableListOf<MyContentsListData>()

        // recyclerview 세팅 및 데이터 추가
        initMyContentsListRecycler()

        // db에서 해당 카테고리의 contents 데이터 불러오기
        val mCategoryUid: Int = db!!.categoryDataDao().getCategoryUid(mCategory)
        val storedContentsData: MutableList<RecordDataEntity>
                = db!!.recordDataDao().getRecordFromCategory(mCategoryUid)

        for(i: Int in 0..(storedContentsData.size - 1)) {
            var hashTags: MutableList<String> = storedContentsData[i].record_tags?.split(";")!!.toMutableList()

            addData(
                storedContentsData[i].record_uid,
                storedContentsData[i].record_date.toString(),
                storedContentsData[i].record_contents,
                hashTags
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMyContentsListBinding.inflate(layoutInflater);

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 메인 페이지에서 넘어온 카테고리 데이터 받아오기
        arguments?.let {
            mCategory = it.getString("category").toString()
        }

        // 페이지에 보이는 카테고리
        viewBinding.contentsListCategory.text = mCategory

        // recyclerview 세팅 및 데이터 추가
        initMyContentsListRecycler()

        // 이전 버튼 클릭 시
        viewBinding.contentsListBackBtn.setOnClickListener {
            // 이전 페이지로 이동
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 기록 작성 버튼 클릭
        viewBinding.categoryDetailWriteBtn.setOnClickListener{
            val intent = Intent(activity, LogWriteActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        // 체크박스 전체 선택/해제
        viewBinding.contentsListCheckboxAll.setOnCheckedChangeListener { _, isChecked ->
            myContentsListAdapter.setAllCheck(isChecked)
            myContentsListAdapter.notifyDataSetChanged()
        }

    }

    // recyclerview 세팅
    private fun initMyContentsListRecycler() {
        myContentsListAdapter = MyContentsListAdapter(
            requireContext(),
            onClickContent = {
                goLogDetail(it)
            }
        )
        viewBinding.myContentsListRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.myContentsListRecyclerview.adapter = myContentsListAdapter
        myContentsListAdapter.datas = datas
        myContentsListAdapter.checkboxList = checkData
    }

    // 데이터 추가
    // tag도 recyclerview로 바꿔야 함 -> 여러 개 나올 수 있으므로
    // date: String, title: String, tag: String
    private fun addData(id: Int, date: String, title: String, tag: MutableList<String>) {
        datas.apply {
            //add(MyContentsListData(category, contents))
            add(MyContentsListData(id, date, title, tag))
        }
        checkData.apply {
            add(CheckBoxListData2(title, false))
        }
        myContentsListAdapter.notifyDataSetChanged()
    }

    // 기록 세부 페이지(LogDetail) 열기
    fun goLogDetail(id: Int) {
        // 기록 세부 페이지로 이동
        // 기록 세부 페이지로 기록 id 정보 넘겨주기
        activity?.sendContentIdFromMyLogDetail(id)
    }
}