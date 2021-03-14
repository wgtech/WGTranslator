package project.wgtech.wgtranslator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import project.wgtech.wgtranslator.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private val TAG = SplashActivity::class.java.simpleName
    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

    }
}