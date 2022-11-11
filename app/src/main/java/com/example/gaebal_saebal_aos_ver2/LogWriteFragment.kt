package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentLogWriteBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.RecordDataEntity
import java.util.*
import kotlin.collections.ArrayList


class LogWriteFragment : Fragment() {
    private lateinit var viewBinding: FragmentLogWriteBinding
    var activity: LogWriteActivity? = null
    private lateinit var LogWriteCategoryAdapter: LogWriteCategoryAdapter

    // Category DB 세팅
    private var db: AppDatabase? = null

    var category = ArrayList<Category>()

    private val pickImage = 100
    private var imageUri: Uri? = null

    // onAttach, onDetach: clicklistener를 사용하기 위해서 필요요요요
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as LogWriteActivity

    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = FragmentLogWriteBinding.inflate(layoutInflater)

        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // db 세팅
        db = AppDatabase.getInstance(this.requireContext())

        // 빈 이미지 세팅
        val resources: Resources = this.resources
        val nullImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.default_image)

        category.add(Category("미정"))
        category.add(Category("자료구조"))
        category.add(Category("백준"))
        category.add(Category("PBL"))

        LogWriteCategoryAdapter = LogWriteCategoryAdapter(this.category)
        LogWriteCategoryAdapter.setItemClickListener(object :
            LogWriteCategoryAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

            }
        })

        viewBinding.backBtn.setOnClickListener{
            activity?.finish()
        }

        // 등록 버튼 클릭 시 DB에 내용 저장
        viewBinding.logWriteRegisterBtn.setOnClickListener {
            // 기본값
            var recordCategoryUid: Int? = null // 카테고리 id
            var recordContent: String? = null // 기록 내용
            var recordTag: String = "" // 태그
            var recordBeakjoonNum: Int = -1; // 백준 문제 번호
            var recordGithubType: String = "" // 깃허브 타입: issue, commit, Pull request
            var recordGithubDate: Date = Date() // 깃허브 날짜
            var recordGithubTitle: String = "" // 깃허브 제목
            var recordGithubRepo: String = "" // 깃허브 레포지토리
            var recordImage: Bitmap = nullImage // 이미지
            var recordImageExist: Boolean = false // 이미지 존재 유무
            var recordCode: String = "" // 코드
            val currentDate: Date = Date() // 현재 날짜

            // 사용자 입력값
            recordContent = viewBinding.logWriteMainText.text.toString() // 본문 내용
            recordTag = viewBinding.tagInput.text.toString() // 태그
            recordCode = viewBinding.logWriteCodeText.text.toString() // 코드

            db?.recordDataDao()?.deleteAllRecordData()
            recordCategoryUid = 1

            if(recordCategoryUid != null && recordContent != "") {
                // RecordDataEntity 생성
                val mRecord = RecordDataEntity(
                    0,
                    recordCategoryUid,
                    recordContent,
                    recordTag,
                    recordBeakjoonNum,
                    recordGithubType,
                    recordGithubDate,
                    recordGithubTitle,
                    recordGithubRepo,
                    recordImage,
                    recordImageExist,
                    recordCode,
                    currentDate
                )
                db?.recordDataDao()?.insertRecordData(mRecord) // DB에 추가

                val recordDatas = db!!.recordDataDao().getAllRecordData()
                if(recordDatas.isNotEmpty()) {
                    Log.d("Test", "--------------------------------")
                    Log.d("Test", recordDatas.toString())
                }

                activity?.finish()
            }
            else {
                Toast.makeText(requireActivity(), "본문을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.logWriteCategoryRecyclerview.adapter = LogWriteCategoryAdapter

        // 백준에 + 버튼 클릭시 백준 번호를 입력하는 modal 창이 나온다.
        viewBinding.baekjoonBtn.setOnClickListener {
            //액티비티일 때
//            val Dialog = BojDialog()
//            activity.supportFragmentManager
//                .beginTransaction()
//                .add(R.id.boj_dialog, Dialog)
//                .commit()
            //fragment일 때
//            val dialog = BojDialog(getContext())
//            dialog.showDialog()
//            dialog.setOnClickListener(object: BojDialog.OnDialogClickListener {
//                override fun onClicked(num: Int) {
//
//                }
//            })
            activity?.onFragmentChange("BojDialog")
        }

        // 깃허브에 + 버튼 클릭시 하단에서 bottom sheet이 나오면서 최근 이슈, 풀, 커밋 리스트가 나온다
        viewBinding.githubBtn.setOnClickListener {
            activity?.onFragmentChange("GitHubFragment")
        }

        viewBinding.logWriteMainText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewBinding.charCnt.text = "0/1000"
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var userinput = viewBinding.logWriteMainText.text.toString()
                viewBinding.charCnt.text = userinput.length.toString() + "/1000"
                if (userinput.length >= 1000) {
                    activity?.onFragmentChange("TextOverDialog")
                }
                if (userinput.length == 0) {
                    activity?.onFragmentChange("TextZeroDialog")
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                var userinput = viewBinding.logWriteMainText.text.toString()
                viewBinding.charCnt.text = userinput.length.toString() + "/1000"

            }
        })

        viewBinding.logWriteCodeText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewBinding.codeCharCnt.text = "0/1000"
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var userinput = viewBinding.logWriteCodeText.text.toString()
                viewBinding.codeCharCnt.text = userinput.length.toString() + "/1000"
            }

            override fun afterTextChanged(p0: Editable?) {
                var userinput = viewBinding.logWriteCodeText.text.toString()
                viewBinding.codeCharCnt.text = userinput.length.toString() + "/1000"
                if (userinput.length >= 1000) {
                    activity?.onFragmentChange("TextOverDialog")
                }
                if (userinput.length == 0) {
                    activity?.onFragmentChange("TextZeroDialog")
                }
            }
        })


    }

}





