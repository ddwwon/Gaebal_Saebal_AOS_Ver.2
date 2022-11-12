package com.example.gaebal_saebal_aos_ver2

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

class FloatingBtn (context: Context, fragment: Fragment, recordId: Int, db: AppDatabase){

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

        dialog.log_detail_modify.setOnClickListener{
            dialog.dismiss()
        }

        dialog.log_detail_delete.setOnClickListener{
            db?.recordDataDao().deleteRecordFromUid(recordId)
            dialog.dismiss()
            MainActivity.getInstance()?.onRemoveDetail(fragment)
        }
    }
    interface OnDialogClickListener {
        fun onClicked(num: Int)
    }
}