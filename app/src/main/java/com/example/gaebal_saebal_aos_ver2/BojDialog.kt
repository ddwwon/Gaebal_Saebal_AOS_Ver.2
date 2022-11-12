package com.example.gaebal_saebal_aos_ver2

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.EditText
import androidx.annotation.MainThread
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.boj_problem_dialog.*
import kotlin.properties.Delegates

//import kotlinx.android.synthetic.main.boj_problem_dialog.*

class BojDialog (context: Context) {

    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener
    var activity: MainActivity? = null
    // 백준 문제 번호 저장하는 변수
    var problemNum = ""

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.boj_problem_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.boj_cancel_btn.setOnClickListener{
            dialog.dismiss()
        }

        dialog.boj_ok_btn.setOnClickListener{
            dialog.dismiss()
            problemNum = dialog.boj_num.text.toString()
            println("problemNum: " + problemNum)
            MainActivity.getInstance()?.onFragmentChange("BojNumInput")
        }
    }
    interface OnDialogClickListener {
        fun onClicked(num: Int)
    }
}