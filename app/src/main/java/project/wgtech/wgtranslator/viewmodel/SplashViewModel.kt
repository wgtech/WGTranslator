package project.wgtech.wgtranslator.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import project.wgtech.wgtranslator.model.Status
import project.wgtech.wgtranslator.repository.DataRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: DataRepository,
) : ViewModel() {

    private val _status : MutableLiveData<Status> = MutableLiveData()
    val status : LiveData<Status> = _status

    init {
        _status.value = repository.initializedStatus
        initializeAllModels()
    }

    fun initializeAllModels() {
        viewModelScope.launch {
            _status.value = repository.initializeAllModels()
        }
    }


}