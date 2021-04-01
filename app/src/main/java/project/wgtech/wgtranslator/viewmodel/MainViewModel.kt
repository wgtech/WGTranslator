package project.wgtech.wgtranslator.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import project.wgtech.wgtranslator.Util
import project.wgtech.wgtranslator.model.Status
import project.wgtech.wgtranslator.repository.DataRepository
import project.wgtech.wgtranslator.view.LanguageSpinnerAdapter
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val repository: DataRepository,
    private val util: Util
) : ViewModel() {
    private val _translated : MutableLiveData<String> = MutableLiveData()
    val translated : LiveData<String> = _translated

    private val languageModels = repository.getLanguageModels()
    var languageSpinnerAdapter = LanguageSpinnerAdapter(context, languageModels)

    fun refreshTranslateData(sourceLanguageNumber: Int, targetLanguageNumber: Int, targetText: String): String? {
        var result: String? = null
        viewModelScope.launch {
            Translation.getClient(TranslatorOptions.Builder()
                    .setSourceLanguage(languageModels[sourceLanguageNumber - 1].code)
                    .setTargetLanguage(languageModels[targetLanguageNumber - 1].code)
                    .build())
                    .translate(targetText)
                    .addOnSuccessListener {
                        _translated.value = it
                        result = it
                        util.logd("refreshTranslateData (addOnSuccessListener) --> ${_translated.value}")
                    }
                    .addOnFailureListener {
                        _translated.value = it.message
                        result = it.message
                        util.loge("refreshTranslateData (addOnFailureListener) --> ${_translated.value}")
                    }
        }
        return result
    }
}