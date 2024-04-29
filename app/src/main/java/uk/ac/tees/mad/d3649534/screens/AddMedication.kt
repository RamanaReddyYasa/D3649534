package uk.ac.tees.mad.d3649534.screens

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3649534.R
import uk.ac.tees.mad.d3649534.data.domain.Medication
import uk.ac.tees.mad.d3649534.screens.components.EndDatePickerDialog
import uk.ac.tees.mad.d3649534.utils.Frequency
import uk.ac.tees.mad.d3649534.utils.PermissionAlarmDialog
import uk.ac.tees.mad.d3649534.utils.getRecurrenceList
import uk.ac.tees.mad.d3649534.utils.toFormattedDateString
import uk.ac.tees.mad.d3649534.viewmodels.AddMedicationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

//enum class Week {
//    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
//}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class
)
@Composable
fun AddMedication(
    onCancel: () -> Unit,
    addMedicationViewModel: AddMedicationViewModel = hiltViewModel()
) {

    val uiState = addMedicationViewModel.addMedicationUiState.collectAsState().value
    //setting start date
    val today = Calendar.getInstance()
//    today.set(Calendar.HOUR_OF_DAY, 0)
//    today.set(Calendar.MINUTE, 0)
//    today.set(Calendar.SECOND, 0)
//    today.set(Calendar.MILLISECOND, 0)
    val currentDayMillis = today.timeInMillis
    addMedicationViewModel.updateUiState(uiState.copy(startDate = Date(currentDayMillis)))

    var frequency by rememberSaveable {
        mutableStateOf(Frequency.Daily.name)
    }

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Declaring and initializing a calendar
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    // Value for storing time as a string
    var selectedTimes = remember { mutableStateListOf<String>() }

    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val cameraPermission = rememberPermissionState(android.Manifest.permission.CAMERA)

    val alarmPermission = rememberPermissionState(android.Manifest.permission.SCHEDULE_EXACT_ALARM)
    var showAlarmPermission by remember {
        mutableStateOf(false)
    }
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
        PermissionAlarmDialog(askAlarmPermission = alarmPermission.status.isGranted)

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
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            shape = RoundedCornerShape(20.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        FrequencyDropdownMenu {
            addMedicationViewModel.updateUiState(uiState.copy(frequency = it))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Intake Times")

        Spacer(modifier = Modifier.height(8.dp))
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, selHour: Int, selMinute: Int ->
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                // Formatting the selected hour and minute into HH:mm format
                val formattedTime = timeFormat.format(Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selHour)
                    set(Calendar.MINUTE, selMinute)
                }.time)
                selectedTimes.add(formattedTime)

                val unformattedTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selHour)
                    set(Calendar.MINUTE, selMinute)
                }.time
                addMedicationViewModel.updateUiState(
                    uiState.copy(
                        medicationTimesList = uiState.medicationTimesList + unformattedTime
                    )
                )
            },
            hour,
            minute,
            false
        )

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
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f),
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
                                IconButton(onClick = { }) {
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
        EndDateTextField(endDate = {
            addMedicationViewModel.updateUiState(
                uiState.copy(
                    endDate = Date(
                        it
                    )
                )
            )
        })
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
            onClick = {
                Log.d(
                    "Perm",
                    alarmPermission.permission + ", status: " + alarmPermission.status
                )
                if (validateFields(uiState, context, uiState.medicationTimesList)) {

                    addMedicationViewModel.createMedication(context)
                    selectedTimes.removeRange(0, selectedTimes.size - 1)
                    onCancel()
                }

            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),

            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Submit")
        }
    }
}

fun validateFields(
    uiState: Medication,
    context: Context,
    medicationTimes: List<Date>
): Boolean {
    // Check if the name field is not empty
    if (uiState.name.isBlank()) {
        Toast.makeText(context, "Name field cannot be empty", Toast.LENGTH_SHORT).show()
        return false
    }

    // Check if the dosage field is not empty
    if (uiState.dosage.isBlank()) {
        Toast.makeText(context, "Dosage field cannot be empty", Toast.LENGTH_SHORT).show()
        return false
    }

    // Check if the frequency field is not empty
    if (uiState.frequency.isBlank()) {
        Toast.makeText(context, "Frequency field cannot be empty", Toast.LENGTH_SHORT).show()
        return false
    }

    // Check if the medication times list is not empty
    if (medicationTimes.isEmpty()) {
        Toast.makeText(context, "At least one medication time must be set", Toast.LENGTH_SHORT)
            .show()
        return false
    }

    // Check if the end date is not null and is after the current date
    if (uiState.endDate.before(Date())) {
        Toast.makeText(context, "End date must be set to a future date", Toast.LENGTH_SHORT).show()
        return false
    }

    // If all checks pass, return true
    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencyDropdownMenu(frequency: String? = null, recurrence: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.frequency),
            style = MaterialTheme.typography.bodyLarge
        )

        val options = getRecurrenceList().map { it.name }
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember {
            mutableStateOf(
                options[0]
            )
        }
        LaunchedEffect(frequency) {
            if (frequency != null) {
                selectedOptionText = options[options.indexOf(frequency)]
            }
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(16.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            recurrence(selectionOption)
                            expanded = false
                        },
                        modifier = Modifier.fillMaxWidth()

                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDateTextField(date: Date? = null, endDate: (Long) -> Unit) {
    Text(
        text = stringResource(id = R.string.end_date),
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(8.dp))

    var shouldDisplay by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()
    if (isPressed) {
        shouldDisplay = true
    }

    val today = Calendar.getInstance()
    today.set(Calendar.HOUR_OF_DAY, 0)
    today.set(Calendar.MINUTE, 0)
    today.set(Calendar.SECOND, 0)
    today.set(Calendar.MILLISECOND, 0)
    val currentDayMillis = today.timeInMillis

    val initialDateMillis = date?.time ?: System.currentTimeMillis()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= currentDayMillis
            }
        }
    )

    // Ensure selectedDate reflects the provided date or the current selection
    var selectedDate by rememberSaveable { mutableStateOf(date?.toFormattedDateString() ?: "") }
    LaunchedEffect(date) {
        selectedDate = date?.toFormattedDateString() ?: ""
    }
    // Update selectedDate only when a date is confirmed in the picker
    EndDatePickerDialog(
        state = datePickerState,
        shouldDisplay = shouldDisplay,
        onConfirmClicked = { selectedDateInMillis ->
            selectedDate = selectedDateInMillis.toFormattedDateString()
            endDate(selectedDateInMillis)
            shouldDisplay = false // Dismiss the dialog after selection
        },
        dismissRequest = {
            shouldDisplay = false
        }
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        value = selectedDate,
        onValueChange = {},
        shape = RoundedCornerShape(20.dp),
        trailingIcon = { Icons.Default.DateRange },
        interactionSource = interactionSource
    )
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

@Preview(showBackground = true)
@Composable
fun AddMedPrev() {
    AddMedication(onCancel = { /*TODO*/ })
}