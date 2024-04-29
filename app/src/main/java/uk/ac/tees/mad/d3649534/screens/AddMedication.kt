package uk.ac.tees.mad.d3649534.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3649534.R
import uk.ac.tees.mad.d3649534.components.CheckboxList
import uk.ac.tees.mad.d3649534.viewmodels.AddMedicationViewModel
import java.util.Calendar

//enum class Week {
//    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
//}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class
)
@Composable
fun AddMedication(onCancel: () -> Unit) {
    val addMedicationViewModel: AddMedicationViewModel = viewModel()
    val uiState = addMedicationViewModel.addMedicationUiState.collectAsState().value

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Declaring and initializing a calendar
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    // Value for storing time as a string
    val selectedTimes = remember { mutableStateListOf<String>() }

    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val cameraPermission = rememberPermissionState(android.Manifest.permission.CAMERA)

    //Taking images from gallary
    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            selectedImage = uri
            if (uri != null) {
                addMedicationViewModel.handleImageSelection(uri, context)
            }
        }

    val requestCameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let { addMedicationViewModel.handleImageCapture(it) }
        }

    Column(modifier = Modifier.padding(24.dp)) {
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
                windowInsets = WindowInsets.ime
            ) {
                // Sheet content
                PhotoPickerOption(
                    onGalleryClick = {
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                        galleryLauncher.launch("image/*")
                    },
                    onCameraClick = {
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                        if (cameraPermission.status.isGranted) {
                            requestCameraLauncher.launch(null)
                        } else {
                            cameraPermission.launchPermissionRequest()
                            if (cameraPermission.status.isGranted) {
                                requestCameraLauncher.launch(null)
                            }
                        }
                    }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add New Medicine",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            IconButton(onClick = onCancel) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
        }
        OutlinedTextField(
            value = uiState.name,
            onValueChange = {
                addMedicationViewModel.updateUiState(uiState.copy(name = it))
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            shape = RoundedCornerShape(20.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.dosage,
            onValueChange = { addMedicationViewModel.updateUiState(uiState.copy(dosage = it)) },
            label = { Text("Dosage") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            shape = RoundedCornerShape(20.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Frequency")
        CheckboxList(weeksList) {
            addMedicationViewModel.updateUiState(
                uiState.copy(
                    frequency = it
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Intake Times")

        Spacer(modifier = Modifier.height(8.dp))
        val timePickerDialog = TimePickerDialog(context, { _, selHour: Int, selMinute: Int ->
            selectedTimes.add("$selHour:$selMinute")
        }, hour, minute, false)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(20.dp)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedTimes.isEmpty()) {
                Text(
                    text = "Not selected",
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Gray
                )
            } else {
                LazyRow(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(selectedTimes) { time ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = time,
                                    fontSize = 20.sp,
                                    style = TextStyle(color = Color.White),
                                    modifier = Modifier.padding(8.dp)
                                )
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Delete time",
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            IconButton(onClick = { timePickerDialog.show() }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add time",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Add a spacer of 10dp
        Spacer(modifier = Modifier.size(10.dp))

        if (uiState.image == null) {
            Card(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFdedede))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_image_placeholder),
                    contentDescription = "Select Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Add medicine image", modifier = Modifier.padding(8.dp))
                }
            }
        } else {
//            val bitmap = byteArrayToBitmap(uiState.image)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).crossfade(true)
                    .data(uiState.image)
                    .build(),
                contentDescription = "Selected image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .height(240.dp)
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {}, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),

            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun PhotoPickerOption(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .clickable {
                    onCameraClick()
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CameraEnhance,
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .size(35.dp)
            )

            Text(
                text = "Camera",
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        }
        Row(
            modifier = Modifier
                .clickable {
                    onGalleryClick()
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Image, contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .size(35.dp)
            )
            Text(
                text = "Gallery",
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        }
    }
}

fun byteArrayToBitmap(byteArray: ByteArray): ImageBitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
}

@Preview(showBackground = true)
@Composable
fun AddMedPrev() {
    AddMedication {

    }
}