package com.glopez.driverassignments.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glopez.driverassignments.domain.model.DriverAssignment
import com.glopez.driverassignments.domain.repository.ShipmentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriversViewModel @Inject constructor(
    private val repo: ShipmentsRepository
) : ViewModel() {
    private var _driverAssignments = MutableLiveData<UiState<List<DriverAssignment>>>()
    val driverAssignments: LiveData<UiState<List<DriverAssignment>>>
        get() = _driverAssignments

    init {
        viewModelScope.launch {
            _driverAssignments.value = UiState.Loading()

            repo.getDriverAssignments()
                .catch { exception ->
                    _driverAssignments.value = UiState.Error(exception.message)
                }
                .collect {
                    if (it.isEmpty()) {
                        _driverAssignments.value = UiState.Loading()
                    } else {
                        _driverAssignments.value = UiState.Success(it)
                    }
                }
        }
    }
}