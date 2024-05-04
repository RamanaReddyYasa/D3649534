package uk.ac.tees.mad.d3649534.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3649534.NotificationManagerService
import uk.ac.tees.mad.d3649534.data.domain.Medication
import uk.ac.tees.mad.d3649534.data.repository.MedicationRepository
import uk.ac.tees.mad.d3649534.screens.EditMedicationDestination
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class EditMedicationViewModel @Inject constructor(
    private val repository: MedicationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val medId: String = checkNotNull(savedStateHandle[EditMedicationDestination.medicineIdArg])


    private val _uiState = MutableStateFlow(Medication())
    val uiState = _uiState.asStateFlow()

    fun updateUiState(value: Medication) {
        _uiState.value = value
    }

    init {
        getItemByKey(medId)
    }

    private fun getItemByKey(medKey: String) =
        repository.getMedicationByKey(medKey).onEach {

            if (it.isNotEmpty())
                _uiState.value = it[0]

        }.launchIn(viewModelScope)


    suspend fun updateMedication(context: Context) {
        repository.deleteMedication(medId)

        val interval = when (_uiState.value.frequency) {
            "Daily" -> 1
            "Weekly" -> 7
            "Monthly" -> 30
            else -> throw IllegalArgumentException("Invalid recurrence: ${_uiState.value.frequency}")
        }

        println("Interval: $interval")
        val oneDayInMillis = 86400 * 1000 // Number of milliseconds in one day
        val numOccurrences =
            ((_uiState.value.endDate.time + oneDayInMillis - _uiState.value.startDate.time) / (interval * oneDayInMillis)).toInt() + 1
        val medId = UUID.randomUUID()
        val medications = mutableListOf<Medication>()
        val calendar = Calendar.getInstance()
        calendar.time = _uiState.value.startDate
        for (i in 0 until numOccurrences) {
            for (medicationTime in _uiState.value.medicationTimesList) {
                println("Medication time = ${getMedicationTime(medicationTime, calendar)}")
                val medication = Medication(
                    id = Random.nextLong(),
                    medId = medId.toString(),
                    name = _uiState.value.name,
                    dosage = _uiState.value.dosage,
                    frequency = _uiState.value.frequency,
                    endDate = _uiState.value.endDate,
                    medicationTaken = false,
                    image = _uiState.value.image,
                    medicationTime = getMedicationTime(medicationTime, calendar),
                    medicationTimesList = _uiState.value.medicationTimesList
                )
                medications.add(medication)
                viewModelScope.launch {
                    val service = NotificationManagerService(context)
                    service.scheduleNotification(medication)
                    Log.d("NOTIFICATION SCHEDULED", "Notification Scheduled for $medication")
                }
            }
            calendar.add(Calendar.DAY_OF_YEAR, interval)
        }

        println("Medications: $medications")
        viewModelScope.launch {
            repository.insertMedications(medications)
        }.invokeOnCompletion {
            _uiState.value = Medication()
        }

    }

    fun handleImageSelection(uri: Uri, context: Context) {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images
                .Media
                .getBitmap(context.contentResolver, uri)

        } else {
            val source = ImageDecoder
                .createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
        val imageByteArray = convertBitmapToByteArray(bitmap)
        _uiState.update { med ->
            med.copy(image = imageByteArray)
        }
    }

    fun handleImageCapture(bitmap: Bitmap) {
        val imageByteArray = convertBitmapToByteArray(bitmap)
        _uiState.update { med ->
            med.copy(image = imageByteArray)
        }
    }

    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}