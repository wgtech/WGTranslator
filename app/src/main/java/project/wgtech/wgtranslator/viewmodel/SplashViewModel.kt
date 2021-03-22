package project.wgtech.wgtranslator.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import project.wgtech.wgtranslator.Util
import project.wgtech.wgtranslator.repository.DataRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val repository: DataRepository,
    private val util: Util
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.initializeAllModels()
        }
    }
}