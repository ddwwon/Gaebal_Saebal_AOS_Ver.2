package com.example.gaebal_saebal_aos_ver2

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentTokenCheckDialogBinding
import kotlinx.android.synthetic.main.fragment_token_check_dialog.*
import java.lang.IllegalStateException
import java.lang.RuntimeException

class TokenCheckDialogFragment(context: Context, pastFragment: Fragment) {

    private val pastFragment: Fragment = pastFragment
    private val dialog = Dialog(context)
    private lateinit var onClickListener: BojDialog.ButtonClickListener
    var activity: MainActivity ?= null

    fun setOnClickListener(listener: BojDialog.ButtonClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.fragment_token_check_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.token_check_dialog_btn.setOnClickListener {
            dialog.dismiss()
        }
    }
}