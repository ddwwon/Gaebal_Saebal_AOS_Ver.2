// 기록 편집 페이지
package com.example.gaebal_saebal_aos_ver2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentLogEditBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity
import com.example.gaebal_saebal_aos_ver2.db_entity.RecordDataEntity
import java.util.*


class LogEditFragment : Fragment() {
    private lateinit var viewBinding: FragmentLogEditBinding
    var activity: LogWriteActivity? = null
    private lateinit var LogWriteCategoryAdapter: LogWriteCategoryAdapter

    // Category DB 세팅
    private var db: AppDatabase? = null

    var category: MutableList<CategoryDataEntity> = mutableListOf<CategoryDataEntity>()
    var categorySelectCheck: MutableList<Boolean> = mutableListOf<Boolean>()

    // 내용
    var recordUid: Int? = null // 기록 id
    var recordCategoryUid: Int? = null // 카테고리 id
    var recordContent: String? = null // 기록 내용
    var recordTag: String? = null  // 태그
    var recordBaekjoonNum: Int? = null  // 백준 문제 번호
    var recordBaekjoonName: String? = null  // 백준 문제 이름
    var recordGithubType: String? = null  // 깃허브 타입: issue, commit, Pull request
    var recordGithubDate: String? = null  // 깃허브 날짜
    var recordGithubTitle: String? = null  // 깃허브 제목
    var recordGithubRepo: String? = null  // 깃허브 레포지토리
    var recordImage: Bitmap? = null  // 이미지
    var recordImageExist: Boolean? = null  // 이미지 존재 유무
    var recordCode: String? = null  // 코드
    var recordDate: Date? = null  // 현재 날짜

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

        viewBinding = FragmentLogEditBinding.inflate(layoutInflater)

        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 메인 페이지에서 넘어온 기록 id 데이터 받아오기
        arguments?.let {
            recordUid = it.getInt("recordId").toInt()
        }

        // db 세팅
        db = AppDatabase.getInstance(this.requireContext())

        // 작성되어 있는 내용
        var mRecord: RecordDataEntity = db!!.recordDataDao().getRecordContent(recordUid!!)
        recordCategoryUid = mRecord.record_category_uid // 카테고리 id
        recordContent = mRecord.record_contents // 기록 내용
        recordTag = mRecord.record_tags  // 태그
        recordBaekjoonNum = mRecord.record_baekjoon_num  // 백준 문제 번호
        recordBaekjoonName = mRecord.record_baekjoon_name // 백준 문제 이름
        recordGithubType = mRecord.record_github_type  // 깃허브 타입: issue, commit, Pull request
        recordGithubDate = mRecord.record_github_date  // 깃허브 날짜
        recordGithubTitle = mRecord.record_github_title  // 깃허브 제목
        recordGithubRepo = mRecord.record_github_repo  // 깃허브 레포지토리
        recordImage = mRecord.record_image  // 이미지
        recordImageExist = mRecord.record_image_exist  // 이미지 존재 유무
        recordCode = mRecord.record_code  // 코드
        recordDate = mRecord.record_date  // 현재 날짜 - 수정 불가

        // 빈 이미지 세팅
        //val resources: Resources = this.resources
        //val nullImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.default_image)

        // ImageView 숨김(공간까지!)
        viewBinding.addImageView.visibility = View.GONE

        // 이전 내용 보이게 해두기
        viewBinding.logWriteMainText.setText(recordContent) // 기록 내용 = 본문
        viewBinding.tagInput.setText(recordTag) // 태그
        if(recordImageExist!!) { // 이미지 존재 시
            Log.d("Test Image", "-----------------------------")
            viewBinding.addImageView.visibility = View.VISIBLE
            viewBinding.addImageView.setImageBitmap(recordImage)
        }
        viewBinding.logWriteCodeText.setText(recordCode) // 코드

        // 카테고리 recyclerview
        category.addAll(db!!.categoryDataDao().getAllCategoryData())

        // 기본 선택된 카테고리
        for(i: Int in (0..category.size - 1)){
            if(category[i].category_uid == recordCategoryUid)
                categorySelectCheck.add(true)
            else
                categorySelectCheck.add(false)
        }

        LogWriteCategoryAdapter = LogWriteCategoryAdapter(
            this.category,
            categorySelectCheck
        )

        viewBinding.backBtn.setOnClickListener{
            activity?.finish()
        }

        // 등록 버튼 클릭 시 DB에 내용 저장 - 수정 완료
        viewBinding.logWriteRegisterBtn.setOnClickListener {
            // 사용자 입력값
            recordContent = viewBinding.logWriteMainText.text.toString() // 본문 내용
            recordTag = viewBinding.tagInput.text.toString() // 태그
            recordCode = viewBinding.logWriteCodeText.text.toString() // 코드

            // 이미지 존재 시
            if(viewBinding.addImageView.visibility == View.VISIBLE) {
                recordImageExist = true
                recordImage = viewBinding.addImageView.drawToBitmap()
            }

            //db?.recordDataDao()?.deleteAllRecordData()
            // 카테고리
            for(i: Int in (0..category.size - 1)){
                if(categorySelectCheck[i]) {
                    recordCategoryUid = category[i].category_uid
                }
            }

            if(recordCategoryUid != null && recordContent != "") {
                // RecordDataEntity 생성
                val mNewRecord = RecordDataEntity(
                    recordUid!!,
                    recordCategoryUid!!,
                    recordContent!!,
                    recordTag!!,
                    recordBaekjoonNum!!,
                    recordBaekjoonName!!,
                    recordGithubType!!,
                    recordGithubDate!!,
                    recordGithubTitle!!,
                    recordGithubRepo!!,
                    recordImage!!,
                    recordImageExist!!,
                    recordCode!!,
                    recordDate!!
                )
                db?.recordDataDao()?.updateRecordData(mNewRecord) // DB 수정

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

        // 사진에서 + 버튼을 누르면
        viewBinding.imageBtn.setOnClickListener {
            activity?.onFragmentChange("GalleryAccess")
            navigatePhotos()
            // ImageView 보임
            viewBinding.addImageView.visibility = View.VISIBLE
        }
    }

    //
    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) {
            println("wrong")
            return
        }
        when(requestCode){
            2000 -> {
                val selectedImageURI : Uri? = data?.data
                if( selectedImageURI != null ) {
                    viewBinding.addImageView.setImageURI(selectedImageURI)
                }else {
                    println("wrong")
                }
            }
            else -> {
                println("wrong")
            }
        }
    }

}





