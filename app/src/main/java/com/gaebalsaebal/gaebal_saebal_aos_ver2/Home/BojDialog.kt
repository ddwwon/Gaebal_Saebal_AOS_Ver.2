package com.gaebalsaebal.gaebal_saebal_aos_ver2

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.gaebalsaebal.gaebal_saebal_aos_ver2.R
import kotlinx.android.synthetic.main.boj_problem_dialog.*
import retrofit2.Call
import retrofit2.Response

//import kotlinx.android.synthetic.main.boj_problem_dialog.*

class BojDialog (context: Context, pastFragment: Fragment) {
    private val pastFragment: Fragment = pastFragment

    private val dialog = Dialog(context)
    private lateinit var onClickListener: ButtonClickListener
    var activity: MainActivity? = null

    // 백준 번호, 이름
    private var baekjoonNum: Int? = null
    private var baekjoonName: String? = null

    interface ButtonClickListener {
        fun onClicked(mBaekjoonNum: Int, mBaekjoonName: String)
    }

    fun setOnClickListener(listener: ButtonClickListener) {
        onClickListener = listener
    }

    // dialog를 띄우는 함수
    fun showDialog()
    {
        dialog.setContentView(R.layout.boj_problem_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        // dialog에서 취소 버튼을 누르면, dialog dismiss
        dialog.boj_cancel_btn.setOnClickListener{
            dialog.dismiss()
        }

        // dialog에서 확인 버튼을 누르면, dialog dismiss 되고, boj title을 받아옴
        dialog.boj_ok_btn.setOnClickListener{
            // boj 문제 번호를 변수에 저장
            baekjoonNum = Integer.parseInt(dialog.boj_num.text.toString())
            println("recordBeakjoonNum: " + baekjoonNum)

            // 문제 번호에 해당하는 문제 이름을 백준 Api에서 찾는 함수 호출
            bojClient()

            // 백준 문제 번호 입력 Dialog 닫기
            dialog.dismiss()
        }
    }

    // Boj Api
    fun bojClient() {
        BojClient.getApi().getBOJNumber(baekjoonNum!!).enqueue(object : retrofit2.Callback<Title> {
            override fun onResponse(call: Call<Title>, response: Response<Title>) {
                // 성공 처리
                if(response.isSuccessful()) { // <--> response.code == 200
                    var token = response.body().toString().split("=")
                    var temp = token[1].split(")")
                    // boj 문제 이름 data에 저장(recordBeakjoonName)
                    baekjoonName = temp[0]
                    println("tmep: " + temp[0])

                    // boj 문제 번호, 문제 이름을 데이터를 호출 Fragment로 전달
                    onClickListener.onClicked(baekjoonNum!!, baekjoonName!!)

                    // 선택한 값이 작성 페이지에 보일 수 있도록
                    pastFragment.onResume()

                } else if(baekjoonName == "없음") { // code == 400
                    // 실패 처리
                    println("getbojerror")
                }
            }
            override fun onFailure(call: Call<Title>, t:Throwable) { // code == 500
                // 실패 처리
                println("getbojerror: " + t)
            }
        })
    }
}