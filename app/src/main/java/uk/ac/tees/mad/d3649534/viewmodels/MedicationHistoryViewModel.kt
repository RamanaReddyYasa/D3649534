package uk.ac.tees.mad.d3649534.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3649534.data.domain.Medication
import uk.ac.tees.mad.d3649534.data.repository.MedicationRepository
import javax.inject.Inject

@HiltViewModel
class MedicationHistoryViewModel @Inject constructor(
    private val repository: MedicationRepository
) : ViewModel() {

    var state by mutableStateOf(listOf<Medication>())
        private set

    init {
        loadMedications()
    }

    fun loadMedications() {
        viewModelScope.launch {
            repository.getAllMedications().onEach { medicationList ->
                state = medicationList
            }.launchIn(viewModelScope)
        }
    }

    fun updateMedication(medication: Medication) = viewModelScope.launch {
        repository.updateMedication(medication)
    }
}