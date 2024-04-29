package uk.ac.tees.mad.d3649534

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import uk.ac.tees.mad.d3649534.data.domain.Medication
import uk.ac.tees.mad.d3649534.utils.toFormattedTimeString

class MedicationNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            intent?.getParcelableExtra<Medication>(MEDICATION_INTENT)?.let { medication ->
                showNotification(it, medication)
            }
        }
    }

    private fun showNotification(context: Context, medication: Medication) {
        val activityIntent = Intent(context, MainActivity::class.java)
        activityIntent.putExtra(MEDICATION_NOTIFICATION, true)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val receiverIntent = Intent(context, NotificationActionReceiver::class.java)
        receiverIntent.putExtra(NotificationActionReceiver.MEDICATION_INTENT, medication)
        val takenPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            context,
            NotificationManagerService.MEDICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_dose)
            .setContentTitle("Medication Reminder")
            .setContentText("It is ${medication.medicationTime.toFormattedTimeString()}, Time to take your medication, ${medication.name}. Open the app to log it.")
            .setContentIntent(activityPendingIntent)
            .addAction(
                R.drawable.ic_dose,
                "Take now",
                takenPendingIntent
            )
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(medication.id.toInt(), notification)

    }

    companion object {
        const val MEDICATION_INTENT = "medication_intent"
        const val MEDICATION_NOTIFICATION = "medication_notification"
    }

}