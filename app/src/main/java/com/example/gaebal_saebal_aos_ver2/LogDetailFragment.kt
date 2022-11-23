package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentLogDetailBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.RecordDataEntity

//import kotlinx.android.synthetic.main.fragment_log_detail.*

//data class logDetailItem(logCategory: )
class LogDetailFragment : Fragment() {
    private lateinit var viewBinding: FragmentLogDetailBinding

    // Room DB 세팅
    private var db: AppDatabase? = null

    // 카테고리 세부 recyclerview adapter
    private var datas = mutableListOf<String>()
    private lateinit var myContentsHashTagAdapter: MyContentsHashTagAdapter

    // 기록 id
    private var mRecordId: Int? =  null

    var activity: MainActivity? = null

    // onAttach, onDetach: clicklistener를 사용하기 위해서 필요요요요
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLogDetailBinding.inflate(layoutInflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 메인 페이지에서 넘어온 기록 id 데이터 받아오기
        arguments?.let {
            mRecordId = it.getInt("contentId").toInt()
        }

        // db 세팅
        db = AppDatabase.getInstance(this.requireContext())

        // 태그 recyclerview 세팅
        initMyContentsListRecycler()

        // 해당 기록 내용 가져오기
        val mContent: RecordDataEntity = db!!.recordDataDao().getRecordContent(mRecordId!!)

        // 기록 내용 보이기 입력 ---------------------------------------------------------------------------------------------------
        // 없을 수도 있는 내용: 태그, 백준, 깃허브, 이미지, 코드
        viewBinding.logDetailHashtags.visibility = View.GONE
        viewBinding.baekjoonView.visibility = View.GONE
        viewBinding.logDetailGithub.visibility = View.GONE
        viewBinding.logDetailImage.visibility = View.GONE
        viewBinding.logDetailCode.visibility = View.GONE

        // 제목: '카테고리명'의 N번째 기록
        // 카테고리명
        val category: String = db!!.categoryDataDao().getCategoryName(mContent.record_category_uid)
        // 몇 번째 기록인지
        val contentsOfSameCategory: MutableList<RecordDataEntity> = db!!.recordDataDao().getRecordFromCategory(mContent.record_category_uid)
        var contentNum: Int? = null
        for(i: Int in 0..(contentsOfSameCategory.size - 1)) {
            if(contentsOfSameCategory[i].record_uid == mRecordId) {
                contentNum = i + 1
                break
            }
        }
        // 제목 입력
        viewBinding.logDetailTitle.text = category + "의 " + contentNum!!.toString() + "번째 기록"

        // 본문 - 기록 내용 입력
        viewBinding.logDetailContent.text = mContent.record_contents

        // 해시태그 입력 - 내용이 존재할 경우
        if(mContent.record_tags != "") {
            viewBinding.logDetailHashtags.visibility = View.VISIBLE
            var hashTags: MutableList<String> = mContent.record_tags?.split(";")!!.toMutableList()
            datas.addAll(hashTags)
            myContentsHashTagAdapter.notifyDataSetChanged()
        }

        // 기록 작성 날짜 입력
        var recordDates: MutableList<String> = mContent.record_date.toString().split(" ")!!.toMutableList()
        var setRecordDate:String = recordDates[5] + " " + recordDates[1] + " " + recordDates[2]  + " " + recordDates[0] + " " + recordDates[3]
        viewBinding.logDetailDate.text = setRecordDate

        // 백준 입력 - 내용이 있을 경우
        if(mContent.record_baekjoon_name != "") {
            viewBinding.baekjoonView.visibility = View.VISIBLE
            viewBinding.beakjoonNumberName.text = mContent.record_baekjoon_num.toString() + " - " + mContent.record_baekjoon_name
        }

        // 깃허브 입력 - 내용이 있을 경우
        if(mContent.record_github_title != "") {
            viewBinding.logDetailGithub.visibility = View.VISIBLE
            viewBinding.githubRepo.text = mContent.record_github_repo // 깃허브 레포
            viewBinding.githubTitle.text = mContent.record_github_title // 깃허브 내용

            // 깃허브 타입: issue, commit, Pull request -> 아이콘 지정
            val gitType =  mContent.record_github_type
            if(gitType == "commit") {
                viewBinding.githubType.setImageDrawable(getResources().getDrawable(R.drawable.commit_icon))
            }
            else if(gitType == "issue") {
                viewBinding.githubType.setImageDrawable(getResources().getDrawable(R.drawable.issue_icon))
            }
            else if(gitType == "Pull request") {
                viewBinding.githubType.setImageDrawable(getResources().getDrawable(R.drawable.pull_request_icon))
            }

            // 깃허브 날짜
            var gitDates: MutableList<String> = mContent.record_github_date.toString().split(" ")!!.toMutableList()
            var setGitDate:String = gitDates[5] + " " + gitDates[1] + " " + gitDates[2]  + " " + gitDates[0] + " " + gitDates[3]
            viewBinding.githubDate.text = setGitDate
        }

        // 이미지 입력 - 내용이 있을 경우
        if(mContent.record_image_exist!!) {
            viewBinding.logDetailImage.visibility = View.VISIBLE
            viewBinding.logDetailImage.setImageBitmap(mContent.record_image)
        }

        // 코드 입력 - 내용이 있을 경우
        if(mContent.record_code != "") {
            viewBinding.logDetailCode.visibility= View.VISIBLE
            viewBinding.logDetailCodeText.text = mContent.record_code
        }

        // boj name을 data에서 불러옴(테스트 필요)
        //var bojNumAndTitle = mContent.record_baekjoon_num.toString() + " - " + mContent.record_baekjoon_name
        //viewBinding.logDetailBeakjoonNumber.setText(bojNumAndTitle)

        // 닫기 버튼
        viewBinding.logDetailBackBtn.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 수정 or 삭제
        viewBinding.floatingBtn.setOnClickListener(){
            val dialog = FloatingBtn(requireActivity(), this, mRecordId!!, db!!)
            dialog.showDialog()
        }
    }

    // recyclerview 세팅
    private fun initMyContentsListRecycler() {
        myContentsHashTagAdapter = MyContentsHashTagAdapter(requireContext())
        viewBinding.logDetailHashtags.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.logDetailHashtags.adapter = myContentsHashTagAdapter
        myContentsHashTagAdapter.datas = datas
    }

//    public fun removeLogDetail(){
//        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
//        requireActivity().supportFragmentManager.popBackStack()
//    }

}