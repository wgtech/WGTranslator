package project.wgtech.wgtranslator.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import project.wgtech.wgtranslator.R
import project.wgtech.wgtranslator.Util
import project.wgtech.wgtranslator.databinding.FragmentMainBinding
import project.wgtech.wgtranslator.viewmodel.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding

    @Inject lateinit var util: Util
    @Inject lateinit var inputMethodManager: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (view.requestFocus()) {
            inputMethodManager.showSoftInput(view, (InputMethodManager.HIDE_IMPLICIT_ONLY))
        }

        binding.spinnerLanguageInputMain.apply {
            adapter = mainViewModel.languageSpinnerAdapter
            setSelection(1, true)
        }
        binding.spinnerLanguageOutputMain.apply {
            adapter = mainViewModel.languageSpinnerAdapter
            setSelection(2, true)
        }

        binding.editTextInputMain.addTextChangedListener(object : TextWatcher {
            private var launchedScope: Job? = null

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (launchedScope != null) {
                    (launchedScope as CoroutineScope).cancel()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                launchedScope = lifecycleScope.launch {
                    if (s.isNullOrBlank()) {
                        binding.textViewOutputMain.text = ""

                    } else {
                        delay(500)
                        val inputSelected = binding.spinnerLanguageInputMain.selectedItemPosition
                        val outputSelected = binding.spinnerLanguageOutputMain.selectedItemPosition
                        mainViewModel.refreshTranslateData(inputSelected, outputSelected, s.toString())
                    }
                }
            }
        })

        mainViewModel.translated.observe(viewLifecycleOwner, Observer {
            binding.textViewOutputMain.text = it
        })

        binding.executePendingBindings()
    }
}