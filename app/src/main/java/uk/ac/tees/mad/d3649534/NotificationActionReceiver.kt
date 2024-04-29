package uk.ac.tees.mad.d3649534

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import dagger.hilt.EntryPoints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.ac.tees.mad.d3649534.data.domain.Medication
import uk.ac.tees.mad.d3649534.data.repository.MedicationRepository
import uk.ac.tees.mad.d3649534.data.repository.MedicationRepositoryEntryPoint

class NotificationActionReceiver : BroadcastReceiver() {
    private lateinit var repository: MedicationRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val hiltEntryPoint =
                EntryPoints.get(it.applicationContext, MedicationRepositoryEntryPoint::class.java)
            repository = hiltEntryPoint.medicationRepository()

            intent?.getParcelableExtra<Medication>(MEDICATION_INTENT)?.let { medication ->
                val updatedMedication = medication.copy(
                    medicationTaken = true
                )
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("UPDATED", updatedMedication.toString())
                    repository.updateMedication(updatedMedication)
                    withContext(Dispatchers.Main) {
                        updateNotification(context, medication)
                    }
                }
            }
        }
    }


    private fun updateNotification(context: Context, medication: Medication) {
        val notification = NotificationCompat.Builder(
            context,
            NotificationManagerService.MEDICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_dose)
            .setContentTitle("Medication Reminder")
            .setContentText("You have taken your medication, ${medication.name}.")
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(medication.id.toInt(), notification)
    }

    companion object {
        const val MEDICATION_INTENT = "medication_intent"
    }
}