package com.example.gaebal_saebal_aos_ver2

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentLogWriteBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.boj_problem_dialog.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import kotlin.properties.Delegates

//import kotlinx.android.synthetic.main.boj_problem_dialog.*

class BojDialog (context: Context, pastFragment: Fragment) {
    private val pastFragment: Fragment = pastFragment

    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener
    var activity: MainActivity? = null

    fun setOnClickListener(listener: OnDialogClickListener)
    {
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
            // boj 문제 번호 database(recordBeakjoonNum)에 저장
            recordBeakjoonNum = Integer.parseInt(dialog.boj_num.text.toString())
            println("recordBeakjoonNum: " + recordBeakjoonNum)
            bojClient()

            // 선택한 값이 작성 페이지에 보일 수 있도록
            pastFragment.onResume()

            // 닫기
            dialog.dismiss()
        }
    }

    // Boj Api
    fun bojClient() {
        BojClient.getApi().getBOJNumber(recordBeakjoonNum).enqueue(object : retrofit2.Callback<Title> {
            override fun onResponse(call: Call<Title>, response: Response<Title>) {
                // 성공 처리
                if(response.isSuccessful()) { // <--> response.code == 200
                    var token = response.body().toString().split("=")
                    var temp = token[1].split(")")
                    // boj 문제 이름 data에 저장(recordBeakjoonName)
                    recordBeakjoonName = temp[0]
                    println("tmep: " + temp[0])
                    // boj 문제 번호 + 문제 이름을 bojNumAndTitle에 저장
                    var bojNumAndTitle = recordBeakjoonNum.toString() + " - " + recordBeakjoonName
                    // boj 문제 번호 보이게
                    logWriteBaekJoonNumber.visibility = View.VISIBLE
                    logWriteBaekJoonNumber.setText(bojNumAndTitle)
                } else if(recordBeakjoonName == "없음") { // code == 400
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

    interface OnDialogClickListener {
        fun onClicked(num: Int)
    }
}