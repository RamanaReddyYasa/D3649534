package uk.ac.tees.mad.d3649534.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3649534.R
import uk.ac.tees.mad.d3649534.navigation.NavigationDestination

object MedicineDetailDestination : NavigationDestination {
    override val route = "medicineDetail"
    override val titleRes: Int = R.string.app_name
//    const val medicineIdArg = "medicineId"
//    val routeWithArgs = "$route/{$medicineIdArg}"
}

@Composable
fun MedicineDetail() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "Go back",
            tint = MaterialTheme.colorScheme.primary
        )
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
                Image(
                    contentDescription = "medicine image",
                    painter = painterResource(id = R.drawable.ic_image_placeholder),
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop

                )
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
                MedicineText(label = "Pill Name", name = "Gabapentin")
                Spacer(modifier = Modifier.height(6.dp))

                MedicineText(label = "Pill Dosage", name = "300 mg")
                Spacer(modifier = Modifier.height(6.dp))

                MedicineText(label = "Next dose", name = "3 pm")
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        Column {
            Text(
                text = "Dose", style = TextStyle(
                    color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Text(
                    text = "3 times", style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "9am, 3pm & 9pm", style = TextStyle(
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
            Row {
                Text(
                    text = "Mon", style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Wed", style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Thus", style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Sun", style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                )
            }
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
                    text = "12/10/2024", style = TextStyle(
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
                    text = "12/11/2024", style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))


        Button(
            onClick = {},
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
    MedicineDetail()
}