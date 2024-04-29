package uk.ac.tees.mad.d3649534.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3649534.R
import uk.ac.tees.mad.d3649534.navigation.NavigationDestination
import uk.ac.tees.mad.d3649534.utils.toFormattedDateString
import uk.ac.tees.mad.d3649534.utils.toFormattedTimeString
import uk.ac.tees.mad.d3649534.viewmodels.MedicineDetailViewModel

object MedicineDetailDestination : NavigationDestination {
    override val route = "medicineDetail"
    override val titleRes: Int = R.string.app_name
    const val medicineIdArg = "medicineId"
    val routeWithArgs = "$route/{$medicineIdArg}"
}

@Composable
fun MedicineDetail(
    onNavigateUp: () -> Unit,
    onChangeSchedule: (String) -> Unit,
) {
    val medicineDetailViewModel: MedicineDetailViewModel = hiltViewModel()
    val uiState = medicineDetailViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showDeleteAlertDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        if (showDeleteAlertDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteAlertDialog = false },
                confirmButton = {
                    Text(
                        text = "Confirm",
                        color = Color.Red.copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                medicineDetailViewModel.deleteMedicine()
                                Toast.makeText(context, "Medicine Deleted", Toast.LENGTH_SHORT)
                                    .show()
                                onNavigateUp()
                            }
                        }
                    )
                },
                title = {
                    Text(text = "Delete medicine")
                },
                text = {
                    Text(text = "Are you sure to delete the medicine?")
                }
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go back",
                modifier = Modifier.clickable {
                    onNavigateUp()
                }
            )
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "Delete",
                tint = Color.Red.copy(alpha = 0.5f),
                modifier = Modifier.clickable {
                    showDeleteAlertDialog = true
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .weight(1f)
            ) {
                if (uiState.value.image == null) {

                    Image(
                        contentDescription = "medicine image",
                        painter = painterResource(id = R.drawable.ic_image_placeholder),
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(context = context)
                            .data(uiState.value.image)
                            .crossfade(true).build(),
                        contentDescription = null,
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
                    .height(250.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(6.dp))
                MedicineText(label = "Pill Name", name = uiState.value.name)
                Spacer(modifier = Modifier.height(6.dp))

                MedicineText(label = "Pill Dosage", name = uiState.value.dosage)
                Spacer(modifier = Modifier.height(6.dp))

                MedicineText(
                    label = "Next dose",
                    name = uiState.value.medicationTime.toFormattedTimeString()
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        Column {
            Text(
                text = uiState.value.medicationTimesList.size.toString() + " times",
                style = TextStyle(
                    color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row {

                Text(
                    text = buildAnnotatedString {
                        uiState.value.medicationTimesList.forEachIndexed { index, date ->
                            append(date.toFormattedTimeString())
                            if (uiState.value.medicationTimesList.size - 1 != index)
                                append(", ")
                        }
                    }, style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            Text(
                text = "Program", style = TextStyle(
                    color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = uiState.value.frequency, style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Column {
            Text(
                text = "Start Date", style = TextStyle(
                    color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Text(
                    text = uiState.value.startDate.toFormattedDateString(), style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(26.dp))

        Column {
            Text(
                text = "End Date", style = TextStyle(
                    color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Text(
                    text = uiState.value.endDate.toFormattedDateString(), style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))


        Button(
            onClick = { onChangeSchedule(uiState.value.medId) },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Change Schedule", style = TextStyle(
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun MedicineText(
    label: String, name: String
) {
    Column {
        Text(
            text = label, style = TextStyle(
                color = Color.Gray,
                fontSize = 16.sp,
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name, style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview
@Composable
private fun MedDetail() {
    MedicineDetail(onNavigateUp = {}, onChangeSchedule = {})
}