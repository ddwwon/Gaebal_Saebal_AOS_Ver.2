package com.example.gaebal_saebal_aos_ver2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentLogWriteBinding


class LogWriteFragment : Fragment() {
    private lateinit var viewBinding: FragmentLogWriteBinding
    var activity: LogWriteActivity? = null
    private lateinit var LogWriteCategoryAdapter: LogWriteCategoryAdapter

    var category = ArrayList<Category>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as LogWriteActivity

    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = FragmentLogWriteBinding.inflate(layoutInflater)

        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        category.add(Category("미정"))
        category.add(Category("자료구조"))
        category.add(Category("백준"))
        category.add(Category("PBL"))

        LogWriteCategoryAdapter = LogWriteCategoryAdapter(this.category)
        LogWriteCategoryAdapter.setItemClickListener(object :
            LogWriteCategoryAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

            }
        })

        viewBinding.backBtn.setOnClickListener{
            activity?.finish()
        }


        viewBinding.logWriteCategoryRecyclerview.adapter = LogWriteCategoryAdapter

        // 백준에 + 버튼 클릭시 백준 번호를 입력하는 modal 창이 나온다.
        viewBinding.baekjoonBtn.setOnClickListener {
            //액티비티일 때
//            val Dialog = BojDialog()
//            activity.supportFragmentManager
//                .beginTransaction()
//                .add(R.id.boj_dialog, Dialog)
//                .commit()
            //fragment일 때
//            val dialog = BojDialog(getContext())
//            dialog.showDialog()
//            dialog.setOnClickListener(object: BojDialog.OnDialogClickListener {
//                override fun onClicked(num: Int) {
//
//                }
//            })
            activity?.onFragmentChange("BojDialog")
        }

        // 깃허브에 + 버튼 클릭시 하단에서 bottom sheet이 나오면서 최근 이슈, 풀, 커밋 리스트가 나온다
        viewBinding.githubBtn.setOnClickListener {
            activity?.onFragmentChange("GitHubFragment")
        }

        viewBinding.logWriteMainText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewBinding.charCnt.text = "0/1000"
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var userinput = viewBinding.logWriteMainText.text.toString()
                viewBinding.charCnt.text = userinput.length.toString() + "/1000"
                if (userinput.length >= 1000) {
                    activity?.onFragmentChange("TextOverDialog")
                }
                if (userinput.length == 0) {
                    activity?.onFragmentChange("TextZeroDialog")
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                var userinput = viewBinding.logWriteMainText.text.toString()
                viewBinding.charCnt.text = userinput.length.toString() + "/1000"

            }
        })

        viewBinding.logWriteCodeText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewBinding.codeCharCnt.text = "0/1000"
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var userinput = viewBinding.logWriteCodeText.text.toString()
                viewBinding.codeCharCnt.text = userinput.length.toString() + "/1000"
            }

            override fun afterTextChanged(p0: Editable?) {
                var userinput = viewBinding.logWriteCodeText.text.toString()
                viewBinding.codeCharCnt.text = userinput.length.toString() + "/1000"
                if (userinput.length >= 1000) {
                    activity?.onFragmentChange("TextOverDialog")
                }
                if (userinput.length == 0) {
                    activity?.onFragmentChange("TextZeroDialog")
                }
            }
        })

        // ImageView 숨김(공간까지!)
        viewBinding.addImageView.visibility = View.GONE

        // 사진에서 + 버튼을 누르면
        viewBinding.imageBtn.setOnClickListener {
            activity?.onFragmentChange("GalleryAccess")
            navigatePhotos()
            // ImageView 보임
            viewBinding.addImageView.visibility = View.VISIBLE
        }
    }

    //
    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) {
            println("wrong")
            return
        }
        when(requestCode){
            2000 -> {
                val selectedImageURI : Uri? = data?.data
                if( selectedImageURI != null ) {
                    viewBinding.addImageView.setImageURI(selectedImageURI)
                }else {
                    println("wrong")
                }
            }
            else -> {
                println("wrong")
            }
        }
    }

}





