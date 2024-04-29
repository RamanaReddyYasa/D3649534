package uk.ac.tees.mad.d3649534.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3649534.R
import uk.ac.tees.mad.d3649534.navigation.NavigationDestination
import uk.ac.tees.mad.d3649534.ui.theme.green

object HomeScreenDestination : NavigationDestination {
    override val route = "home"
    override val titleRes: Int = R.string.app_name
}

val timeSpanList = listOf("This week", "This month", "This year")

val weeksList = listOf("Mon", "Tue", "Wed", "Thus", "Fri", "Sat", "Sun")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController? = null) {

    var selectedItem by remember {
        mutableIntStateOf(0)
    }

    var selectedWeek by remember {
        mutableIntStateOf(0)
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                shape = RoundedCornerShape(50.dp),
                containerColor = MaterialTheme.colorScheme.primary
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
                        IconButton(onClick = { /*TODO*/ }) {
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
                Divider()
                Spacer(modifier = Modifier.height(24.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    itemsIndexed(timeSpanList) { index, item ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    color = if (index == selectedItem) {
                                        Color.White
                                    } else Color.Transparent
                                )
                                .clickable {
                                    selectedItem = index
                                },
                        ) {
                            Text(
                                text = item, style = TextStyle(
                                    color = if (index == selectedItem) {
                                        green
                                    } else Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.W500,
                                    letterSpacing = 0.5.sp
                                ), modifier = Modifier.padding(
                                    horizontal = 20.dp, vertical = 7.dp
                                )
                            )
                        }
                    }
                }
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
                itemsIndexed(medicineList) { index, medicine ->
                    MedicineCard(medicine = medicine,
                        onClick = {
                            if (navController != null) {
                                navController.navigate(MedicineDetailDestination.route)
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineCard(medicine: Medicine, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.pill),
                contentDescription = "Medicine icon",
                modifier = Modifier.size(70.dp),
                tint = green
            )
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
                text = "${medicine.dose} times today",
                style = TextStyle(color = green, fontSize = 14.sp)
            )
        }
    }
}

val medicineList = listOf(
    Medicine("Atorvastatin", 3),
    Medicine("Codeine", 3),
    Medicine("Lexapro", 3),
    Medicine("Cymbaite", 3),
    Medicine("Paracetamol", 3),
    Medicine("Dolo", 3),
    Medicine("Seredon", 3),
    Medicine("Gebapantin", 3),
)

data class Medicine(
    val name: String,
    val dose: Int,
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPrev() {
    HomeScreen()
}