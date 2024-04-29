package uk.ac.tees.mad.d3649534

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.d3649534.navigation.MedicineReminderNavigation
import uk.ac.tees.mad.d3649534.screens.MedicineDetail
import uk.ac.tees.mad.d3649534.ui.theme.MedicineReminderTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MedicineReminderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MedicineReminderNavigation()
                }
            }
        }
    }
}
