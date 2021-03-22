package project.wgtech.wgtranslator.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import project.wgtech.wgtranslator.viewmodel.MainViewModel
import project.wgtech.wgtranslator.R
import project.wgtech.wgtranslator.Util
import project.wgtech.wgtranslator.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    @Inject lateinit var util: Util

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mainViewModel

        if (util.isSupportKitkat) {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (util.isSupportMarshmallow) {
                    statusBarColor = context.getColor(R.color.lavender_dream)
                }
                when {
                    util.isSupportAndroid11 -> {
                        window.insetsController?.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
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



        binding.spinnerLanguageInputMain.apply {
            adapter = mainViewModel.languageSpinnerAdapter
            setSelection(1, true)
        }
        binding.spinnerLanguageOutputMain.apply {
            adapter = mainViewModel.languageSpinnerAdapter
            setSelection(2, true)
        }
        binding.spinnerLanguageOutputMain.setSelection(2)

        binding.editTextInputMain.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val inputSelected = binding.spinnerLanguageInputMain.selectedItemPosition
                val outputSelected = binding.spinnerLanguageOutputMain.selectedItemPosition
                mainViewModel.refreshTranslateData(inputSelected, outputSelected, s.toString())
            }

        })
        mainViewModel.translated.observe(this, Observer {
            binding.textViewOutputMain.text = it
        })
    }
}