package com.example.gaebal_saebal_aos_ver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.gaebal_saebal_aos_ver2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding // viewBinding

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
}