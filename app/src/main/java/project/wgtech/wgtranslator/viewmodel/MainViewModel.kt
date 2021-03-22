package project.wgtech.wgtranslator.viewmodel

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
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
        val models = repository.getLanguageModels()
        viewModelScope.launch {
            repository.getTranslateData(models[0], models[1], "")
        }
    }

    private val languageModels = repository.getLanguageModels()
    var languageSpinnerAdapter = LanguageSpinnerAdapter(context, languageModels)

    fun refreshTranslateData(sourceLanguageNumber: Int, targetLanguageNumber: Int, targetText: String) {
        viewModelScope.launch {
            _translated.value = repository.getTranslateData(
                languageModels[sourceLanguageNumber - 1],
                languageModels[targetLanguageNumber - 1], targetText
            )
        }
    }
}