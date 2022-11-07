package com.example.gaebal_saebal_aos_ver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.gaebal_saebal_aos_ver2.databinding.ActivityLogWriteBinding


data class Category(val contents:String)
class LogWriteActivity : AppCompatActivity() {

    var category = ArrayList<Category>()

    private lateinit var LogWriteCategoryAdapter:LogWriteCategoryAdapter
    lateinit var binding:ActivityLogWriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        category.add(Category("미정"))
        category.add(Category("자료구조"))
        category.add(Category("백준"))
        category.add(Category("PBL"))

        LogWriteCategoryAdapter = LogWriteCategoryAdapter(this.category)
        LogWriteCategoryAdapter.setItemClickListener(object : LogWriteCategoryAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {

            }
        })


        binding.logWriteCategoryRecyclerview.adapter = LogWriteCategoryAdapter

        binding.baekjoonBtn.setOnClickListener{
//            val Dialog = BojDialog(this)
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.boj_dialog, Dialog)
//                .commit()
            val dialog = BojDialog(this)
            dialog.showDialog()
            dialog.setOnClickListener(object: BojDialog.OnDialogClickListener {
                override fun onClicked(num: Int) {

                }
            })
        }
    }
}