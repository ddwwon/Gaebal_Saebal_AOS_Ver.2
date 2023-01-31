package com.example.gaebal_saebal_aos_ver2

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
//import com.example.gaebal_saebal_aos_ver2.MyInfomation.MyInformationFragment
//import com.example.gaebal_saebal_aos_ver2.Search.SearchFragment
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

    private var doubleBackToExit = false
    // 이전 버튼 - 폰에 있는 이전 버튼
    override fun onBackPressed() {
        //super.onBackPressed()

        if (doubleBackToExit) {
            finishAffinity()
        } else {
            // 현재 액티비티
            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val info = manager.getRunningTasks(1)
            val componentName = info[0].topActivity
            val ActivityName = componentName!!.shortClassName.substring(1)

            // 메인 액티비티인 경우
            if(ActivityName == "MainActivity") {
                var currentFragment: Fragment? = null
                var cntFragment: Int = 0

                // 현재 프래그먼트 찾기
                for (fragment: Fragment in supportFragmentManager.fragments) {
                    if (fragment.isVisible) {
                        currentFragment = fragment
                        cntFragment++
                    }
                }

                // 현재 프래그먼트가 기록, 검색, 설정 페이지 중 하나가 아닌 경우
                if (cntFragment > 1) {
                    // 이전 페이지로 이동
                    supportFragmentManager.beginTransaction().remove(currentFragment!!).commit()
                    supportFragmentManager.popBackStack()
                }
                // 현재 프래그먼트가 기록, 검색, 설정 페이지 중 하나인 경우
                else {
                    Toast.makeText(this, "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
                    doubleBackToExit = true
                    runDelayed(1500L) {
                        doubleBackToExit = false
                    }
                }
            }
        }
    }
    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
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

    // 프래그먼트 refresh
    fun refreshFragment(fragment: Fragment) {
        Log.d("Refresh", "-----------------------------")

        supportFragmentManager
            .beginTransaction()
            .detach(fragment)
            .attach(fragment)
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

    // 기록 리스트 페이지 or 검색 페이지에서 기록 세부페이지로 이동할 때 기록 id 데이터 넘겨줌
    fun sendContentIdFromMyLogDetail(mContentId: Int) {
        val fragment: Fragment = LogDetailFragment()

        // 기록 id 데이터 넘겨줌.
        val bundle = Bundle()
        bundle.putInt("contentId", mContentId)
        fragment.arguments = bundle

        // Log Detail 페이지(기록 세부 페이지)로 이동
        // 이전페이지로 돌아가는 기능을 이용할 수 있도록 replace가 아니라 add로
        changeFragment(fragment)
    }

    fun goLogEditPage(mRecordId: Int) {
        val intent = Intent(this, LogWriteActivity::class.java)
        intent.putExtra("mFragment", "Edit") // 어떤 페이지로 전환할지에 대한 값
        intent.putExtra("mRecordId", mRecordId) // 기록 Id
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    fun onFragmentChange(index: String) {
        when (index) {
            "MyContentsListFragment" -> {
                // 기록 프레그먼트로 전환
                supportFragmentManager.beginTransaction().remove(LogDetailFragment()).commit()
                supportFragmentManager.popBackStack()
           }
            /*"BojNumInput" -> {
                val dialog = BojDialog(this)
                dialog.showDialog()
                dialog.setOnClickListener(object: BojDialog.OnDialogClickListener {
                    override fun onClicked(num: Int) {
                    }
                })
            }*/
            "GitHubFragment" -> {
                val githubfragment = GithubFragment()
                githubfragment.show(supportFragmentManager, githubfragment.tag)
            }
        }
    }

    fun onRemoveOrEditDetail(mFragment: Fragment) {
        supportFragmentManager.beginTransaction().remove(mFragment).commit()
        supportFragmentManager.popBackStack()

        var currentFragment: MutableList<Fragment> = mutableListOf<Fragment>()

        // 현재 프래그먼트 찾기
        for (fragment: Fragment in supportFragmentManager.fragments) {
            if (fragment.isVisible) {
                currentFragment.add(fragment)
            }
        }
        currentFragment[currentFragment.size - 2]!!.onResume()
    }
    
    /*fun onFloatingChange(index: String, fragment: Fragment){
        when (index) {
            "FloatingBtn" -> {
                // 기록 상세 뷰에서 플로팅 버튼 누르면 모달창 뜨게
                val dialog = FloatingBtn(this, fragment)
                dialog.showDialog()
            }
        }
    }*/
}