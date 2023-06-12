package com.gaebalsaebal.gaebal_saebal_aos_ver2

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.android.synthetic.main.boj_problem_dialog.*
import kotlinx.android.synthetic.main.floating_btn_dialog.*
import java.util.Calendar.getInstance

class FloatingBtn(context: Context, fragment: Fragment, recordId: Int, db: AppDatabase){

    private val fragment = fragment
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener
    var activity: MainActivity? = null

    // Room DB 세팅
    private var db: AppDatabase = db
    private var recordId: Int = recordId

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.floating_btn_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        // 기록 수정 버튼
        dialog.log_detail_modify.setOnClickListener{
            // 기록 수정 페이지에 기록 id 넘겨주면서 이동
            MainActivity.getInstance()?.goLogEditPage(recordId)

            // 페이지 닫기
            dialog.dismiss()
            MainActivity.getInstance()?.onRemoveOrEditDetail(fragment)
        }

        // 기록 삭제 버튼
        dialog.log_detail_delete.setOnClickListener{
            // 데이터 베이스에서 기록 삭제
            db?.recordDataDao()?.deleteRecordFromUid(recordId)

            // 페이지 닫기
            dialog.dismiss()
            MainActivity.getInstance()?.onRemoveOrEditDetail(fragment)
        }
    }
    interface OnDialogClickListener {
        fun onClicked(num: Int)
    }
}