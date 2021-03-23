package project.wgtech.wgtranslator.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import dagger.hilt.android.AndroidEntryPoint
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

    companion object {
        val instance = MainFragment()
    }

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
            // TODO stateHidden|adjustResize // https://stackoverflow.com/questions/20355053/adjustresize-with-animation/34779195#34779195
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
            private val handler: Handler = Handler(Looper.getMainLooper())
            private var runnable: Runnable = Runnable { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(runnable)
            }

            override fun afterTextChanged(s: Editable?) {
                handler.removeCallbacks(runnable)
                runnable = Runnable {
                    val inputSelected = binding.spinnerLanguageInputMain.selectedItemPosition
                    val outputSelected = binding.spinnerLanguageOutputMain.selectedItemPosition
                    mainViewModel.refreshTranslateData(inputSelected, outputSelected, s.toString())
                }
                handler.postDelayed(runnable, 500)
            }

        })
        mainViewModel.translated.observe(viewLifecycleOwner, Observer {
            binding.textViewOutputMain.text = it
        })

        binding.executePendingBindings()
    }
}