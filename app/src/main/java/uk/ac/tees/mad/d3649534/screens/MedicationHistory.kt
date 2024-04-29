package uk.ac.tees.mad.d3649534.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import uk.ac.tees.mad.d3649534.R
import uk.ac.tees.mad.d3649534.data.domain.Medication
import uk.ac.tees.mad.d3649534.navigation.NavigationDestination
import uk.ac.tees.mad.d3649534.ui.theme.green
import uk.ac.tees.mad.d3649534.utils.toFormattedDateString
import uk.ac.tees.mad.d3649534.utils.toFormattedTimeString
import uk.ac.tees.mad.d3649534.viewmodels.MedicationHistoryViewModel
import java.util.Calendar

object MedicationHistoryDestination : NavigationDestination {
    override val route: String = "medicine_history"
    override val titleRes: Int = R.string.medication_history

}

@Composable
fun MedicationHistory(
    viewModel: MedicationHistoryViewModel = hiltViewModel(),
    onMedicationItemClick: (String) -> Unit,
    onNavigateUp: () -> Unit
) {

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.clickable { onNavigateUp() })
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Reminder History",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
        MedicationList(
            uiState = viewModel.state,
            onMedicationItemClick = onMedicationItemClick,
            updateMedicationStatus = viewModel::updateMedication
        )
    }
}


@Composable
fun MedicationList(
    uiState: List<Medication>,
    onMedicationItemClick: (String) -> Unit,
    updateMedicationStatus: (Medication) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.SECOND, -1)

    val filteredMedicationList = uiState.filter {
        val oneSecondAgo = calendar.time
        it.medicationTime.time < oneSecondAgo.time
    }.sortedBy {
        it.medicationTime
    }

    if (filteredMedicationList.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "No history yet"
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(filteredMedicationList) {

                MedicationCard(
                    medicine = it,
                    navigateToMedicationDetail = { medication ->
                        onMedicationItemClick(medication)
                    },
                    updateMedicationStatus = { med ->
                        updateMedicationStatus(med)
                    }
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationCard(
    medicine: Medication,
    navigateToMedicationDetail: (String) -> Unit,
    updateMedicationStatus: (Medication) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = { navigateToMedicationDetail(medicine.medId) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray.copy(0.5f),
        )
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                if (medicine.image == null) {
                    Icon(
                        painter = painterResource(id = R.drawable.pill),
                        contentDescription = "Medicine icon",
                        modifier = Modifier.size(70.dp),
                        tint = green
                    )
                } else {

                    AsyncImage(
                        model = ImageRequest
                            .Builder(context = context)
                            .data(medicine.image)
                            .crossfade(true).build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = MaterialTheme.typography.titleSmall,
                    text = medicine.medicationTime.toFormattedDateString().uppercase(),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = medicine.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Reminded at ${medicine.medicationTime.toFormattedTimeString()}",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(Modifier.width(IntrinsicSize.Max)) {
                StatusButton(
                    isTrue = medicine.medicationTaken,
                    text = "Taken",
                    color = MaterialTheme.colorScheme.primary.copy(0.8f),
                    imageVector = Icons.Default.Check,
                    onClick = {
                        updateMedicationStatus(
                            medicine.copy(
                                medicationTaken = true
                            )
                        )
                    }
                )
                StatusButton(
                    isTrue = !medicine.medicationTaken,
                    text = "Skipped",
                    color = Color.Red.copy(0.5f),
                    imageVector = Icons.Default.Close,
                    onClick = {
                        updateMedicationStatus(
                            medicine.copy(
                                medicationTaken = false
                            )
                        )
                    }
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusButton(
    isTrue: Boolean,
    text: String,
    color: Color,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isTrue)
                color
            else
                Color.Unspecified,
            contentColor = if (isTrue) Color.White else
                color
        ),
        border = if (isTrue) BorderStroke(
            0.dp,
            Color.Unspecified
        ) else BorderStroke(1.dp, color),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = text, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}