package uk.ac.tees.mad.d3649534

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import uk.ac.tees.mad.d3649534.data.domain.Medication

class NotificationManagerService(
    private val context: Context

) {

    fun scheduleNotification(medication: Medication) {
        val intent = Intent(context, MedicationNotificationReceiver::class.java)
        intent.putExtra(MedicationNotificationReceiver.MEDICATION_INTENT, medication)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medication.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val alarmService = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = medication.medicationTime.time

        try {
            alarmService.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        } catch (exception: SecurityException) {
            Log.w("NOTIFICATION EXC", exception)
        }
        Log.d("NOTIFICATION", medication.toString())

    }

    companion object {
        const val MEDICATION_CHANNEL_ID = "medication_channel"
    }

}