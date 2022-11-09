package com.example.gaebal_saebal_aos_ver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.gaebal_saebal_aos_ver2.databinding.ActivityLogWriteBinding


data class Category(val contents:String)
class LogWriteActivity : AppCompatActivity() {

//    var category = ArrayList<Category>()

//    private lateinit var LogWriteCategoryAdapter:LogWriteCategoryAdapter
    lateinit var binding:ActivityLogWriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportFragmentManager.beginTransaction().replace(binding.logWriteFrame.id, LogWriteFragment()).commit()
//        category.add(Category("미정"))
//        category.add(Category("자료구조"))
//        category.add(Category("백준"))
//        category.add(Category("PBL"))
//
//        LogWriteCategoryAdapter = LogWriteCategoryAdapter(this.category)
//        LogWriteCategoryAdapter.setItemClickListener(object : LogWriteCategoryAdapter.OnItemClickListener{
//            override fun onClick(v: View, position: Int) {
//
//            }
//        })
//
//
//        binding.logWriteCategoryRecyclerview.adapter = LogWriteCategoryAdapter
//
//        binding.baekjoonBtn.setOnClickListener{
////            val Dialog = BojDialog(this)
////            supportFragmentManager
////                .beginTransaction()
////                .add(R.id.boj_dialog, Dialog)
////                .commit()
//            val dialog = BojDialog(this)
//            dialog.showDialog()
//            dialog.setOnClickListener(object: BojDialog.OnDialogClickListener {
//                override fun onClicked(num: Int) {
//
//                }
//            })
//        }
//
//        binding.githubBtn.setOnClickListener{
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.add(R.id.log_write_frame, GithubFragment()).commit()
//        }
//
//        binding.backBtn.setOnClickListener{
//            finish()
//        }
    }
    fun onFragmentChange(index: String) {
        when(index) {
            "BojDialog" -> {
                // 백준 문제 번호를 입력하는 창을 띄우게
                val dialog = BojDialog(this)
                dialog.showDialog()
                dialog.setOnClickListener(object: BojDialog.OnDialogClickListener {
                    override fun onClicked(num: Int) {

                    }
                })
            }
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