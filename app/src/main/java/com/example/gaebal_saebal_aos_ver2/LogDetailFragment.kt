package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.example.gaebal_saebal_aos_ver2.databinding.FragmentLogDetailBinding

//import kotlinx.android.synthetic.main.fragment_log_detail.*

class LogDetailFragment : Fragment() {
    private lateinit var viewBinding: FragmentLogDetailBinding

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

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_log_detail, container, false)
//        val log_detail_back_btn = view.findViewById<AppCompatButton>(R.id.log_detail_back_btn)
//        val floting_btn = view.findViewById<AppCompatButton>(R.id.floating_btn)
        viewBinding = FragmentLogDetailBinding.inflate(layoutInflater)

        return viewBinding.root

//        log_detail_back_btn.setOnClickListener {
//
//        }
//
//        floting_btn.setOnClickListener {
//
//        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.logDetailBackBtn.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

}