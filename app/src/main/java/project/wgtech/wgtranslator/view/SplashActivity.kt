package project.wgtech.wgtranslator.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import project.wgtech.wgtranslator.R
import project.wgtech.wgtranslator.Util
import project.wgtech.wgtranslator.databinding.ActivitySplashBinding
import project.wgtech.wgtranslator.model.StatusCode
import project.wgtech.wgtranslator.viewmodel.SplashViewModel
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    @Inject lateinit var util: Util

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        if (util.isSupportKitkat) {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                when {
                    util.isSupportAndroid11 -> {
                        window.insetsController?.setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                    }
                    util.isSupportMarshmallow -> {
                        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                    else -> {
                        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    }
                }
            }
        }

        splashViewModel.status.observe(this, { status ->
            when (status.code) {
                StatusCode.INITIALIZE -> {
                    binding.tvSplash.apply {
                        visibility = View.VISIBLE
                        text = util.getString(R.string.message_now_initialize)
                    }
                }
                StatusCode.DOWNLOAD_MODEL, StatusCode.DOWNLOAD_COMPLETE_MODEL,
                StatusCode.INITIALIZE_COMPLETE_MODEL -> {
                    binding.tvSplash.apply {
                        text = status.message
                    }
                    if (status.code == StatusCode.INITIALIZE_COMPLETE_MODEL) {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }
                }
                StatusCode.DOWNLOAD_FAILURE_MODEL -> {
                    AlertDialog.Builder(this, R.style.Theme_WGTranslator_AlertDialog)
                        .setIcon(status.drawable)
                        .setTitle(util.getString(R.string.title_error))
                        .setMessage(status.message)
                        .create().show()
                }
                else -> { /* NOTHING HAPPENED? */ }
            }
        })
    }
}