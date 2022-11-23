// 메인 페이지 > 카테고리 내부(contents 리스트)
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentMyContentsListBinding
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

            var recordDates: MutableList<String> = storedContentsData[i].record_date.toString().split(" ")!!.toMutableList()
            var setRecordDate:String = recordDates[5] + " " + recordDates[1] + " " + recordDates[2]  + " " + recordDates[0] + " " + recordDates[3]

            addData(
                storedContentsData[i].record_uid,
                setRecordDate,
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
            
            // 기록 리스트 페이지에서 기록 삭제/추가/수정을 한 경우 메인 페이지로 돌아갔을 때 바로 적용되도록
            var currentFragment: MutableList<Fragment> = mutableListOf<Fragment>()

            // 현재 프래그먼트 찾기
            for (fragment: Fragment in requireActivity().supportFragmentManager.fragments) {
                if (fragment.isVisible) {
                    currentFragment.add(fragment)
                }
            }
            currentFragment[currentFragment.size - 2]!!.onResume()
        }

        // 기록 작성 버튼 클릭
        viewBinding.categoryDetailWriteBtn.setOnClickListener{
            val intent = Intent(activity, LogWriteActivity::class.java)
            intent.putExtra("mFragment", "Write") // 어떤 페이지로 전환할지에 대한 값
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        // 체크박스 전체 선택/해제
        viewBinding.contentsListCheckboxAll.setOnCheckedChangeListener { _, isChecked ->
            myContentsListAdapter.setAllCheck(isChecked)
            myContentsListAdapter.notifyDataSetChanged()
        }

        // 카테고리 이동 버튼 클릭
        viewBinding.contentsListFolderBtn.setOnClickListener {
            // 체크된 기록
            var checkRecords: ArrayList<Int> = arrayListOf<Int>()
            var index = 0
            var size = datas.size
            while(index < size) {
                // 체크된 카테고리
                if(checkData[index].checked) {
                    checkRecords.add(datas[index].contentId)
                }
                index++
            }

            // 카테고리 이동 이후에는 항상 모두 선택 체크박스 선택 해제
            viewBinding.contentsListCheckboxAll.isChecked = false

            // 선택된 기록이 없을 경우
            if(checkRecords.size == 0) {
                Toast.makeText(requireActivity(), "카테고리 이동을 원하는 글을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            // 선택된 기록이 있을 경우
            else {
                // 기록 id 데이터 넘겨줌.
                val bundle = Bundle()
                bundle.putIntegerArrayList("recordIds", checkRecords)

                val categoryChangeFragment = CategoryChangeFragment()
                categoryChangeFragment.arguments = bundle

                // 카테고리 목록 나타남
                categoryChangeFragment.show(
                    requireActivity().supportFragmentManager,
                    categoryChangeFragment.tag
                )
            }
        }

        // 삭제 버튼 클릭
        viewBinding.contentsListDeleteBtn.setOnClickListener {
            var index = 0
            var size = datas.size
            while(index < size) {
                // 체크된 카테고리
                if(checkData[index].checked) {
                    db?.recordDataDao()?.deleteRecordFromUid(datas[index].contentId)
                    datas.removeAt(index)
                    checkData.removeAt(index)
                    myContentsListAdapter.notifyDataSetChanged()
                    index--
                    size--
                }
                index++
            }

            // 카테고리 삭제 이후에는 항상 모두 선택 체크박스 선택 해제
            viewBinding.contentsListCheckboxAll.isChecked = false
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