package project.wgtech.wgtranslator.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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

    var languageSpinnerAdapter = LanguageSpinnerAdapter(context, repository.getLanguageModels())

    fun getData() {

    }


}