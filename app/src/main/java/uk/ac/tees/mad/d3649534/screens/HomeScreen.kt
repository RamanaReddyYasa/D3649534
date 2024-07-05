package uk.ac.tees.mad.d3649534.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3649534.R
import uk.ac.tees.mad.d3649534.data.domain.Medication
import uk.ac.tees.mad.d3649534.navigation.NavigationDestination
import uk.ac.tees.mad.d3649534.ui.theme.green
import uk.ac.tees.mad.d3649534.viewmodels.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Date

object HomeScreenDestination : NavigationDestination {
    override val route = "home"
    override val titleRes: Int = R.string.app_name
}

val weeksList = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController? = null) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current
    var selectedItem by remember {
        mutableIntStateOf(0)
    }


    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()

    var filteredMedications by remember { mutableStateOf(listOf<Medication>()) }
    val sdf = SimpleDateFormat("EE")
    val sdfForDayIndex = SimpleDateFormat("u")
    val d = Date()
    val dayOfTheWeek: String = sdf.format(d)
    var selectedWeek by remember {
        mutableIntStateOf(sdfForDayIndex.format(d).toInt() - 1)
    }
    filteredMedications = homeViewModel.homeState.filter { med ->
        val medicationDay = sdf.format(med.medicationTime)
        medicationDay == dayOfTheWeek
    }.distinctBy { it.name }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                shape = RoundedCornerShape(50.dp),
                containerColor = green
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add new medicine")
            }
        }
    ) { innerPadding ->

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
                windowInsets = WindowInsets.ime
            ) {
                // Sheet content

                AddMedication(
                    onCancel = {
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(green)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)

                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            navController?.navigate(ProfileDestination.route)
                        }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "User Icon",
                                modifier = Modifier.size(250.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 30.sp,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.W500
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider()
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                itemsIndexed(weeksList) { index, item ->
                    Text(
                        text = item,
                        color = if (index == selectedWeek) Color.Black else Color.Gray,
                        style = TextStyle(
                            color = if (index == selectedItem) {
                                green
                            } else Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W500,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier
                            .padding(
                                horizontal = 10.dp, vertical = 24.dp
                            )
                            .clickable {
                                selectedWeek = index
                                filteredMedications = homeViewModel.homeState
                                    .filter { med ->
                                        sdf.format(med.medicationTime) == item
                                    }
                                    .distinctBy { it.name }
                            }
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.background(Color(0xFFF7F8FC)),
                contentPadding = PaddingValues(24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(filteredMedications.toList()) { index, medicine ->
                    MedicineCard(
                        medicine = medicine,
                        onClick = {
                            navController?.navigate(MedicineDetailDestination.route + "/" + medicine.medId)
                        },
                        context = context
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineCard(medicine: Medication, onClick: () -> Unit, context: Context) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (medicine.image == null) {
                    Icon(
                        painter = painterResource(id = R.drawable.pill),
                        contentDescription = "Medicine icon",
                        modifier = Modifier
                            .size(70.dp)
                            .rotate(-90f),
                        tint = green
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(20.dp))
                    ) {
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
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = medicine.name,
                    style = TextStyle(
                        color = green,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W700
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = medicine.dosage,
                    style = TextStyle(color = green, fontSize = 14.sp)
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPrev() {
    HomeScreen()
}