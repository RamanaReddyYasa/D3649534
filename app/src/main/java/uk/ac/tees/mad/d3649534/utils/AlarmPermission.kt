package uk.ac.tees.mad.d3649534.utils

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionAlarmDialog(
    askAlarmPermission: Boolean
) {
    val context = LocalContext.current
    val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
    if (askAlarmPermission && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)) {
        val alarmPermissionState =
            rememberPermissionState(Manifest.permission.SCHEDULE_EXACT_ALARM) { isGranted ->
                Log.d("ALARM STATUS", isGranted.toString())
            }
        if (alarmManager?.canScheduleExactAlarms() == false) {
            val openAlertDialog = remember { mutableStateOf(true) }

            when {
                openAlertDialog.value -> {

                    AlertDialog(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notification"
                            )
                        },
                        title = {
                            Text(text = "Alarm Permsission")
                        },
                        text = {
                            Text(text = "Please grant alarm permission for getting notification")
                        },
                        onDismissRequest = {
                            openAlertDialog.value = false
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    Intent().also { intent ->
                                        intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                                        context.startActivity(intent)
                                    }

                                    openAlertDialog.value = false
                                }
                            ) {
                                Text("Allow")
                            }
                        }
                    )
                }
            }
        }
    }
}