package uk.ac.tees.mad.d3649534.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3649534.data.domain.Medication
import uk.ac.tees.mad.d3649534.data.repository.MedicationRepository
import uk.ac.tees.mad.d3649534.screens.MedicineDetailDestination
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MedicineDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MedicationRepository
) : ViewModel() {
    private val medId: String =
        checkNotNull(savedStateHandle[MedicineDetailDestination.medicineIdArg])

    private val _uiState = MutableStateFlow(Medication())
    val uiState = _uiState.asStateFlow()

    private val _medState = MutableStateFlow(listOf(Medication()))
    val medState = _medState.asStateFlow()

    init {
        getItemByKey(medId)
    }

    private fun getItemByKey(medKey: String) =
        repository.getMedicationByKey(medKey).onEach {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND, -1)
            val oneSecondAgo = calendar.time
            _medState.value = it.filter {
                it.medicationTime.time > oneSecondAgo.time
            }.sortedBy {
                it.medicationTime
            }
            if (_medState.value.isNotEmpty())
                _uiState.value = _medState.value[0]

        }.launchIn(viewModelScope)

    suspend fun deleteMedicine() = viewModelScope.launch {
        repository.deleteMedication(medId)
    }
}