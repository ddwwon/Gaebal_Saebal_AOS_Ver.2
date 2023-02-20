// 나의 정보 - 설정 페이지
package com.gaebalsaebal.gaebal_saebal_aos_ver2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gaebalsaebal.gaebal_saebal_aos_ver2.databinding.FragmentMyInformationBinding

class MyInformationFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyInformationBinding // viewBinding

    // 프래그먼트 전환을 위해
    var activity: MainActivity? = null

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
        viewBinding = FragmentMyInformationBinding.inflate(layoutInflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 카테고리 추가/삭제 페이지로 이동
        viewBinding.settingCategoryBtn.setOnClickListener {
            activity?.changeFragment(MyInfoCategoryFragment())
        }

        // 깃허브 계정 설정 페이지로 이동
        viewBinding.settingGitBtn.setOnClickListener {
            activity?.changeFragment(MyInfoGitFragment())
        }
    }

}