package project.wgtech.wgtranslator.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import project.wgtech.wgtranslator.di.IoDispatcher
import project.wgtech.wgtranslator.model.Status
import project.wgtech.wgtranslator.repository.DataRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SplashViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: DataRepository,
) : ViewModel() {

    private val _status : MutableLiveData<Status> = MutableLiveData()
    val status : LiveData<Status> = _status

    init {
        _status.value = repository.initializedStatus
        initializeAllModels()
    }

    private fun initializeAllModels() {
        viewModelScope.launch {
            repository.flowInitializeAllModels().flowOn(ioDispatcher).collect { status ->
                _status.value = status
            }
        }
    }


}