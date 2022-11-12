package com.example.gaebal_saebal_aos_ver2

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.gaebal_saebal_aos_ver2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding // viewBinding
    var imageUriList = arrayListOf<Uri>()
    var imageViewList = arrayListOf<ImageView>()

    init {
        instance = this
    }

    companion object {
        private var instance:MainActivity?=null
        fun getInstance():MainActivity? {
            return instance
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(binding.fragmentLayout.id, MyRecordFragment()).commit()
        // navigationBottomView 등록: 하단바 fragment id(bottom_navigation) 등록
        transitionNavigationBottomView(binding.bottomNavigation, supportFragmentManager)
    }

    // NavigationBottomView 화면 전환하는 함수.
    private fun transitionNavigationBottomView(bottomView: BottomNavigationView, fragmentManager: FragmentManager){
        bottomView.setOnItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.my_record -> {
                    fragmentManager.beginTransaction().replace(binding.fragmentLayout.id, MyRecordFragment()).commit()
                }
                R.id.search -> {
                    fragmentManager.beginTransaction().replace(binding.fragmentLayout.id, SearchFragment()).commit()
                }
                R.id.my_information -> {
                    fragmentManager.beginTransaction().replace(binding.fragmentLayout.id, MyInformationFragment()).commit()
                }
                else -> Log.d("test", "error") == 0
            }
            Log.d("test", "final") == 0
        }
    }

    // fragment 전환
    fun changeFragment(fragment: Fragment){
        // 이전페이지로 돌아가는 기능을 이용할 수 있도록 replace가 아니라 add로
        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentLayout.id, fragment)
            .commit()
    }

    // 메인페이지에서 카테고리 세부 페이지로 이동할 때 카테고리 제목 데이터 넘겨줌.
    fun sendCategoryFromMyRecord(mCategory: String) {
        val fragment: Fragment = MyContentsListFragment()

        // 카테고리 제목 데이터 넘겨줌.
        val bundle = Bundle()
        bundle.putString("category", mCategory)
        fragment.arguments = bundle

        // Contents List 페이지(카테고리 세부 페이지)로 이동
        // 이전페이지로 돌아가는 기능을 이용할 수 있도록 replace가 아니라 add로
        changeFragment(fragment)
    }

    fun onFragmentChange(index: String) {
        when (index) {
//            "FloatingBtn" -> {
//                // 기록 상세 뷰에서 플로팅 버튼 누르면 모달창 뜨게
//                val dialog = FloatingBtn(this)
//                dialog.showDialog()
////                dialog.setOnClickListener(object: FloatingBtn.OnDialogClickListener{
////                    override fun onClicked(num: Int) {
////                    }
////                })
//            }
            "MyContentsListFragment" -> {
                // 기록 프레그먼트로 전환
                println("hiiiii")
                supportFragmentManager.beginTransaction().remove(LogDetailFragment()).commit()
                supportFragmentManager.popBackStack()
//                supportFragmentManager.beginTransaction().replace(R.id.fragment_layout, MyContentsListFragment()).commit()
            }

            "BojNumInput" -> {
                val dialog = BojDialog(this)
                dialog.showDialog()
                dialog.setOnClickListener(object: BojDialog.OnDialogClickListener {
                    override fun onClicked(num: Int) {
                    }
                })
            }
        }
    }
    fun onFloatingChange(index: String, fragment: Fragment){
        when (index) {
            "FloatingBtn" -> {
                // 기록 상세 뷰에서 플로팅 버튼 누르면 모달창 뜨게
                val dialog = FloatingBtn(this, fragment)
                dialog.showDialog()
//                dialog.setOnClickListener(object: FloatingBtn.OnDialogClickListener{
//                    override fun onClicked(num: Int) {
//                    }
//                })
            }
        }
    }
    fun onRemoveDetail(fragment: Fragment) {
        println("hiiiii")
        supportFragmentManager.beginTransaction().remove(fragment).commit()
        supportFragmentManager.popBackStack()
    }
//    private fun navigatePhotos() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        startActivityForResult(intent,2000)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode != Activity.RESULT_OK) {
//            Toast.makeText(this,"잘못된 접근입니다",Toast.LENGTH_SHORT).show()
//            return
//        }
//        when(requestCode){
//            2000 -> {
//                val selectedImageURI : Uri? = data?.data
//                if( selectedImageURI != null ) {
////                    val imageView = findViewById<ImageView>(R.id.addImageView)
////                    binding.
////                    binding..setImageURI(selectedImageURI)
////                    imageURI = selectedImageURI
//                }else {
//                    Toast.makeText(this,"이미지를 가져오지 못했습니다1",Toast.LENGTH_SHORT).show()
//                }
//            }
//            else -> {
//                Toast.makeText(this,"이미지를 가져오지 못했습니다2",Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}