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
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentLogWriteBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity
import com.example.gaebal_saebal_aos_ver2.db_entity.RecordDataEntity
import java.util.*

class LogWriteFragment : Fragment() {

    private lateinit var viewBinding: FragmentLogWriteBinding
    var activity: LogWriteActivity? = null
    private lateinit var LogWriteCategoryAdapter: LogWriteCategoryAdapter

    // Room DB 세팅
    private var db: AppDatabase? = null

    // 카테고리
    var category: MutableList<CategoryDataEntity> = mutableListOf<CategoryDataEntity>()
    var categorySelectCheck: MutableList<Boolean> = mutableListOf<Boolean>()

    // boj 문제 번호, 문제 이름을 저장하는 변수
    var recordBaekjoonNum: Int = -1      // 백준 문제 번호
    var recordBaekjoonName: String = ""  // 백준 문제 이름

    // github data을 저장하는 변수
    var recordGithubType: String = "" // 깃허브 타입: issue, commit, Pull request
    var recordGithubDate: String = "" // 깃허브 날짜
    var recordGithubTitle: String = "" // 깃허브 제목
    var recordGithubRepo: String = "" // 깃허브 레포지토리

    override fun onResume() {
        super.onResume()

        // 백준 선택된 경우 - 내용 보여주기, 추가 버튼 숨기기
        if(recordBaekjoonNum != -1) {
            // + textview 없어지게
            viewBinding.baekjoonBtn.visibility = View.GONE
            // boj 아이콘 보이게
            viewBinding.logWriteCodeIc.visibility = View.VISIBLE

            // boj 문제 번호 + 문제 이름을 bojNumAndTitle에 저장
            var bojNumAndTitle = recordBaekjoonNum.toString() + " - " + recordBaekjoonName
            // boj 문제 번호 보이게
            viewBinding.logWriteBeakjoonNumber.visibility = View.VISIBLE
            viewBinding.logWriteBeakjoonNumber.setText(bojNumAndTitle)
        }

        // 깃허즈 Dialog에서 선택한 값 받아오기 - 깃허브 타입
        val mGithubType = arguments?.getString("githubType").toString()
        
        // 깃허브 선택된 경우 - 내용 보여주기, 추가 버튼 숨기기
        if(mGithubType != "null") {
            // 선택된 값 저장
            recordGithubType = mGithubType
            recordGithubDate = arguments?.getString("githubDate").toString()
            recordGithubTitle = arguments?.getString("githubTitle").toString()
            recordGithubRepo = arguments?.getString("githubRepo").toString()

            // 깃허브 추가 버튼 감추기, 깃허브 정보 보여주기
            viewBinding.githubBtnBack.visibility = View.GONE
            viewBinding.githubPart.visibility = View.VISIBLE

            // 선택된 깃허브 정보 입력
            viewBinding.githubDate.text = recordGithubDate
            viewBinding.githubTitle.text = recordGithubTitle
            viewBinding.githubRepo.text = recordGithubRepo

            // 깃허브 타입 아이콘 지정
            if (recordGithubType == "issue") {
                viewBinding.githubType.setImageDrawable(getResources().getDrawable(R.drawable.issue_icon))
            } else if (recordGithubType == "pull request") {
                viewBinding.githubType.setImageDrawable(getResources().getDrawable(R.drawable.pull_request_icon))
            } else if (recordGithubType == "commit") {
                viewBinding.githubType.setImageDrawable(getResources().getDrawable(R.drawable.commit_icon))
            }
        }
    }
    
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

        // 깃허브 변수 초기화
        recordGithubType = "" // 깃허브 타입: issue, commit, Pull request
        recordGithubDate = "" // 깃허브 날짜
        recordGithubTitle = "" // 깃허브 제목
        recordGithubRepo = "" // 깃허브 레포지토리

        // 빈 이미지 세팅
        val resources: Resources = this.resources
        val nullImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.default_image)

        // 카테고리 데이터
        var mCategory = db!!.categoryDataDao().getAllCategoryData()
        category.addAll(mCategory)
        
        // 카테고리가 없을 경우 - '이름 없음' 카테고리 생성
        if(category.size == 0) {
            db?.categoryDataDao()?.insertCategoryData(CategoryDataEntity(0, "이름 없음"))
            mCategory = db!!.categoryDataDao().getAllCategoryData()
            category.addAll(mCategory)
            Toast.makeText(requireActivity(), "카테고리가 존재하지 않아 '이름 없음' 카테고리를 생성했습니다.", Toast.LENGTH_SHORT).show()
        }

        //카테고리 데이터
        val defaultCategory = arguments?.getString("category").toString()
        var defaultCategoryNum = db!!.categoryDataDao().getCategoryUid(defaultCategory)
        Log.d("test", defaultCategory)
        // 기본 선택된 카테고리
        // 기본 선택된 카테고리가 없을 때는 처음 카테고리를 선택한 상태로 함
        if(defaultCategoryNum.toString() == "0"){
            for(i: Int in (0..category.size - 1)){
                if(i == 0)
                    categorySelectCheck.add(true)
                else
                    categorySelectCheck.add(false)
            }
        }
        // 기본 선택된 카테고리가 있을 때
        else {
            for(i: Int in (0..category.size - 1)){
                if(category[i].category_uid == defaultCategoryNum)
                    categorySelectCheck.add(true)
                else
                    categorySelectCheck.add(false)
            }
        }


        LogWriteCategoryAdapter = LogWriteCategoryAdapter(
            this.category,
            categorySelectCheck
        )

        viewBinding.backBtn.setOnClickListener {
            requireActivity().finish()
        }

        // 등록 버튼 클릭 시 DB에 내용 저장
        viewBinding.logWriteRegisterBtn.setOnClickListener {
            // 기본값
            var recordCategoryUid: Int? = null // 카테고리 id
            var recordContent: String? = null // 기록 내용
            var recordTag: String = "" // 태그
//            var recordBeakjoonNum: Int = -1; // 백준 문제 번호
//            var recordBeakjoonName: String = ""; // 백준 문제 이름
//            var recordGithubType: String = "" // 깃허브 타입: issue, commit, Pull request
//            var recordGithubDate: String = "" // 깃허브 날짜
//            var recordGithubTitle: String = "" // 깃허브 제목
//            var recordGithubRepo: String = "" // 깃허브 레포지토리
            var recordImage: Bitmap = nullImage // 이미지
            var recordImageExist: Boolean = false // 이미지 존재 유무
            var recordCode: String = "" // 코드
            val currentDate: Date = Date() // 현재 날짜

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

            if (recordCategoryUid != null && recordContent != "") {
                // RecordDataEntity 생성
                val mRecord = RecordDataEntity(
                    0,
                    recordCategoryUid,
                    recordContent,
                    recordTag,
                    recordBaekjoonNum,
                    recordBaekjoonName,
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
                if (recordDatas.isNotEmpty()) {
                    Log.d("Test", "--------------------------------")
                    Log.d("Test", recordDatas.toString())
                }

                requireActivity().finish()
            } else {
                Toast.makeText(requireActivity(), "본문을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.logWriteCategoryRecyclerview.adapter = LogWriteCategoryAdapter

        // 기본 백준 icon, textview 숨기기
        viewBinding.logWriteCodeIc.visibility = View.GONE
        viewBinding.logWriteBeakjoonNumber.visibility = View.GONE

        // 백준에 + 버튼 클릭시 백준 번호를 입력하는 modal 창이 나온다.
        viewBinding.baekjoonBtn.setOnClickListener {
            // dialog 띄우기
            val dialog = BojDialog(requireActivity(), this)
            dialog.showDialog()

            // 백준 Dialog에서 문제 번호, 이름 값 받아오기
            dialog.setOnClickListener(object: BojDialog.ButtonClickListener {
                override fun onClicked(mBaekjoonNum: Int, mBaekjoonName: String) {
                    //Toast.makeText(context, mBaekjoonName, Toast.LENGTH_SHORT).show()
                    // 문제 번호, 이름 데이터 저장
                    recordBaekjoonNum = mBaekjoonNum
                    recordBaekjoonName = mBaekjoonName
                }
            })

            // 선택값 반영해서 보여주는 건 onResume()에서
        }

        // 백준 입력창 선택 시 백준 번호를 입력하는 modal 창이 나온다.
        viewBinding.baekjoonBtnBack.setOnClickListener ( View.OnClickListener {
            // dialog 띄우기
            val dialog = BojDialog(requireActivity(), this)
            dialog.showDialog()

            // 백준 Dialog에서 문제 번호, 이름 값 받아오기
            dialog.setOnClickListener(object: BojDialog.ButtonClickListener {
                override fun onClicked(mBaekjoonNum: Int, mBaekjoonName: String) {
                    //Toast.makeText(context, mBaekjoonName, Toast.LENGTH_SHORT).show()
                    // 문제 번호, 이름 데이터 저장
                    recordBaekjoonNum = mBaekjoonNum
                    recordBaekjoonName = mBaekjoonName
                }
            })

            // 선택값 반영해서 보여주는 건 onResume()에서
        })

        viewBinding.githubPart.visibility = View.GONE

        // 깃허브에 + 버튼 클릭시 하단에서 bottom sheet이 나오면서 최근 이슈, 풀, 커밋 리스트가 나온다
        viewBinding.githubBtn.setOnClickListener ( View.OnClickListener {
            // bottom sheet 나옴
            val githubfragment = GithubFragment()
            githubfragment.show(requireActivity().supportFragmentManager, githubfragment.tag)

            // 선택값 반영해서 보여주는 건 onResume()에서
        })

        // 깃허브 내용 클릭 시 변경 가능. 하단에서 bottom sheet이 나오면서 최근 이슈, 풀, 커밋 리스트가 나온다
        viewBinding.githubPart.setOnClickListener {
            // bottom sheet 나옴
            val githubfragment = GithubFragment()
            githubfragment.show(requireActivity().supportFragmentManager, githubfragment.tag)

            // 선택값 반영해서 보여주는 건 onResume()에서
        }

        // 본문 작성
        viewBinding.logWriteMainText.addTextChangedListener(object : TextWatcher {
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

        // 코드 작성
        viewBinding.logWriteCodeText.addTextChangedListener(object : TextWatcher {
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

        // ImageView 숨김(공간까지!)
        viewBinding.addImageView.visibility = View.GONE

        // 사진에서 + 버튼을 누르면
        viewBinding.imageBtn.setOnClickListener {
            activity?.onFragmentChange("GalleryAccess")
            navigatePhotos()
            // ImageView 보임
            viewBinding.addImageView.visibility = View.VISIBLE
        }
    }

    // 사진에 + 버튼 클릭 시, 갤러리를 오픈
    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            println("wrong")
            return
        }
        when (requestCode) {
            2000 -> {
                val selectedImageURI: Uri? = data?.data
                if (selectedImageURI != null) {
                    viewBinding.addImageView.setImageURI(selectedImageURI)
                } else {
                    println("wrong")
                }
            }
            else -> {
                println("wrong")
            }
        }
    }
    // 여기까지 갤러리 기능 구현
}





