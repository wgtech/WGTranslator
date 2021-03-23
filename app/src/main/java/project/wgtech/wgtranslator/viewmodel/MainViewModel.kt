package project.wgtech.wgtranslator.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import project.wgtech.wgtranslator.Util
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

    init {
    }

    private val languageModels = repository.getLanguageModels()
    var languageSpinnerAdapter = LanguageSpinnerAdapter(context, languageModels)

    fun refreshTranslateData(sourceLanguageNumber: Int, targetLanguageNumber: Int, targetText: String) {
        viewModelScope.launch {
//            _translated.value = repository.getTranslateData(
//                languageModels[sourceLanguageNumber - 1],
//                languageModels[targetLanguageNumber - 1], targetText
//            )
            Translation.getClient(TranslatorOptions.Builder()
                    .setSourceLanguage(languageModels[sourceLanguageNumber - 1].code)
                    .setTargetLanguage(languageModels[targetLanguageNumber - 1].code)
                    .build())
                    .translate(targetText)
                    .addOnSuccessListener {
                        _translated.value = it
                        Log.d("wgtech", "provideTranslateData (addOnSuccessListener) --> ${_translated.value}")
                    }
                    .addOnFailureListener {
                        _translated.value = it.message
                        Log.e("wgtech", "provideTranslateData (addOnFailureListener) --> ${_translated.value}")
                    }
//            withContext(Dispatchers.IO) {
//
//            }
        }
    }
}