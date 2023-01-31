package com.example.gaebal_saebal_aos_ver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.gaebal_saebal_aos_ver2.databinding.ActivityLogWriteBinding


data class Category(val contents:String)
class LogWriteActivity : AppCompatActivity() {

//    var category = ArrayList<Category>()

//    private lateinit var LogWriteCategoryAdapter:LogWriteCategoryAdapter
    lateinit var binding:ActivityLogWriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val defaultCategory = intent.getStringExtra("category").toString()
        var logWriteFragment = LogWriteFragment()
        var bundle = Bundle()

        //현재 카테고리 번호를 LogWriteFragment로 전달
        bundle.putString("category", defaultCategory)
        logWriteFragment.arguments = bundle

        binding = ActivityLogWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mFragment = intent.getStringExtra("mFragment") // 어떤 페이지로 전환할지에 대한 값

        // 기록 작성
        if(mFragment == "Write") {
            supportFragmentManager.beginTransaction().replace(binding.logWriteFrame.id, logWriteFragment).commit()
        }
        // 기록 수정
        else if(mFragment == "Edit") {
            val logEditFragment: Fragment = LogEditFragment()

            // 기록 id - 수정할 기록이 무엇인지 알기 위해
            val mRecordId = intent.getIntExtra("mRecordId", 0) // 어떤 페이지로 전환할지에 대한 값

            // 기록 수정 페이지로 이동할 때 기록 id 넘겨줌
            val bundle = Bundle()
            bundle.putInt("recordId", mRecordId)
            logEditFragment.arguments = bundle

            supportFragmentManager.beginTransaction().replace(binding.logWriteFrame.id, logEditFragment).commit()
        }
    }

    fun onFragmentChange(index: String) {
        when(index) {
            /*"BojDialog" -> {
                // 백준 문제 번호를 입력하는 창을 띄우게
                val dialog = BojDialog(this)
                dialog.showDialog()
                dialog.setOnClickListener(object: BojDialog.OnDialogClickListener {
                    override fun onClicked(num: Int) {

                    }
                })
            }*/
            "GitHubFragment" ->  {
                // 기록 작성 뷰에서 깃허브 버튼 클릭 시, persistenet bottom sheet로 창을 띄움
                val githubfragment = GithubFragment()
                githubfragment.show(supportFragmentManager, githubfragment.tag)
            }
            "TextOverDialog" -> {
                // 기록 작성 프레그먼트의 본문에서 500자가 넘으면 글자 수를 줄이라는 다이얼로그 출력
                val dialog = TextOverDialog(this)
                dialog.showDialog()
                dialog.setOnClickListener(object: TextOverDialog.OnDialogClickListener {
                    override fun onClicked(num: Int) {
                    }
                })
            }
            "TextZeroDialog" -> {
                // 기록 작성 프레그먼트의 본문에서 500자가 넘으면 글자 수를 줄이라는 다이얼로그 출력
                val dialog = TextZeroDialog(this)
                dialog.showDialog()
                dialog.setOnClickListener(object: TextZeroDialog.OnDialogClickListener {
                    override fun onClicked(num: Int) {
                    }
                })
            }
        }

    }
}