package com.gaebalsaebal.gaebal_saebal_aos_ver2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.UiThread
import com.gaebalsaebal.gaebal_saebal_aos_ver2.databinding.ActivityLoadingBinding

// 로딩 화면(splash screen)
class LoadingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoadingBinding // viewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 초기화
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splachAnimation()
    }

    @UiThread
    private fun splachAnimation() {
        val centerAnim: Animation = AnimationUtils.loadAnimation(this,
            com.gaebalsaebal.gaebal_saebal_aos_ver2.R.anim.anim_splash_center
        )
        binding.splashMainLogo.startAnimation(centerAnim)
        //val dogAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash_left)
        //binding.splashLeftDog.startAnimation(dogAnim)

        val intent = Intent(this, com.gaebalsaebal.gaebal_saebal_aos_ver2.MainActivity::class.java)
        centerAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                startActivity(intent)
                overridePendingTransition(
                    com.gaebalsaebal.gaebal_saebal_aos_ver2.R.anim.anim_splash_out_top,
                    com.gaebalsaebal.gaebal_saebal_aos_ver2.R.anim.anim_splash_in_down
                )
                finish()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }
}