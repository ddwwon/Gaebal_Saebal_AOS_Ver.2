package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentLogDetailBinding
import com.example.gaebal_saebal_aos_ver2.db_entity.RecordDataEntity

//import kotlinx.android.synthetic.main.fragment_log_detail.*

//data class logDetailItem(logCategory: )
class LogDetailFragment : Fragment() {
    private lateinit var viewBinding: FragmentLogDetailBinding

    // Room DB 세팅
    private var db: AppDatabase? = null

    // 기록 id
    private var mRecordId: Int =  -1

    var activity: MainActivity? = null

    // onAttach, onDetach: clicklistener를 사용하기 위해서 필요요요요
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLogDetailBinding.inflate(layoutInflater)

        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 메인 페이지에서 넘어온 기록 id 데이터 받아오기
        arguments?.let {
            mRecordId = it.getInt("contentId").toInt()
        }

        // db 세팅
        db = AppDatabase.getInstance(this.requireContext())

        val mContent: RecordDataEntity = db!!.recordDataDao().getRecordContent(mRecordId)



        viewBinding.logDetailBackBtn.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }

        viewBinding.floatingBtn.setOnClickListener(){
            activity?.onFloatingChange("FloatingBtn", this)
        }


    }
//    public fun removeLogDetail(){
//        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
//        requireActivity().supportFragmentManager.popBackStack()
//    }

}