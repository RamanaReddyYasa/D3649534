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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Date
import kotlin.coroutines.coroutineContext

class AddMedicationViewModel(

) : ViewModel() {
    private val _addMedicineUiState = MutableStateFlow(Medication())
    val addMedicationUiState = _addMedicineUiState.asStateFlow()

    fun updateUiState(value: Medication) {
        _addMedicineUiState.value = value
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
        Log.d("Image", imageByteArray.toString())
        viewModelScope.launch {
            //insert into database
            //database.imageDao().insertImage(ImageData(image = imageByteArray))
        }
    }

    fun handleImageCapture(bitmap: Bitmap) {
        val imageByteArray = convertBitmapToByteArray(bitmap)
        _addMedicineUiState.update { med ->
            med.copy(image = imageByteArray)
        }
        CoroutineScope(Dispatchers.IO).launch {
            //insert into database
            //database.imageDao().insertImage(ImageData(image = imageByteArray))
        }
    }

    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}

data class Medication(
    val id: String = "",
    val name: String = "",
    val dosage: String = "",
    val frequency: List<String> = listOf("Mon", "Tue"),
    val intakeTimes: List<String> = listOf(),
    val startDate: Date = Date(),
    val endDate: Date? = null,
    val image: ByteArray? = null
)