package uk.ac.tees.mad.d3649534.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3649534.NotificationManagerService
import uk.ac.tees.mad.d3649534.data.domain.Medication
import uk.ac.tees.mad.d3649534.data.repository.MedicationRepository
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AddMedicationViewModel @Inject constructor(
    private val repository: MedicationRepository
) : ViewModel() {

    private val _addMedicineUiState = MutableStateFlow(Medication())
    val addMedicationUiState = _addMedicineUiState.asStateFlow()

    fun updateUiState(value: Medication) {
        _addMedicineUiState.value = value
    }

    fun createMedication(context: Context) {
        val interval = when (_addMedicineUiState.value.frequency) {
            "Daily" -> 1
            "Weekly" -> 7
            "Monthly" -> 30
            else -> throw IllegalArgumentException("Invalid recurrence: ${_addMedicineUiState.value.frequency}")
        }

        println("Interval: $interval")
        val oneDayInMillis = 86400 * 1000 // Number of milliseconds in one day
        val numOccurrences =
            ((_addMedicineUiState.value.endDate.time + oneDayInMillis - _addMedicineUiState.value.startDate.time) / (interval * oneDayInMillis)).toInt() + 1
        val medId = UUID.randomUUID()
        val medications = mutableListOf<Medication>()
        val calendar = Calendar.getInstance()
        calendar.time = _addMedicineUiState.value.startDate
        for (i in 0 until numOccurrences) {
            for (medicationTime in _addMedicineUiState.value.medicationTimesList) {
                println("Medication time = ${getMedicationTime(medicationTime, calendar)}")
                val medication = Medication(
                    id = Random.nextLong(),
                    medId = medId.toString(),
                    name = _addMedicineUiState.value.name,
                    dosage = _addMedicineUiState.value.dosage,
                    frequency = _addMedicineUiState.value.frequency,
                    endDate = _addMedicineUiState.value.endDate,
                    medicationTaken = false,
                    image = _addMedicineUiState.value.image,
                    medicationTime = getMedicationTime(medicationTime, calendar),
                    medicationTimesList = _addMedicineUiState.value.medicationTimesList
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
            _addMedicineUiState.value = Medication()
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
        _addMedicineUiState.update { med ->
            med.copy(image = imageByteArray)
        }
    }

    fun handleImageCapture(bitmap: Bitmap) {
        val imageByteArray = convertBitmapToByteArray(bitmap)
        _addMedicineUiState.update { med ->
            med.copy(image = imageByteArray)
        }
    }

    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}

fun getMedicationTime(medicationTime: Date, calendar: Calendar): Date {
    val medicationCalendar = Calendar.getInstance()
    medicationCalendar.time = medicationTime
    calendar.set(Calendar.HOUR_OF_DAY, medicationCalendar.get(Calendar.HOUR_OF_DAY))
    calendar.set(Calendar.MINUTE, medicationCalendar.get(Calendar.MINUTE))
    return calendar.time
}