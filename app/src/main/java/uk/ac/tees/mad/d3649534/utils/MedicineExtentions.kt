package uk.ac.tees.mad.d3649534.utils

import android.util.Log
import uk.ac.tees.mad.d3649534.data.database.MedicationEntity
import uk.ac.tees.mad.d3649534.data.domain.Medication
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun List<Medication>.getNextDosageMedicine(): Medication {
    for (med in this) {
        if (med.medicationTime.time > System.currentTimeMillis()) {
            Log.d("Upcoming Medication Time", med.medicationTime.toString())
            return med
        }
    }
    return Medication()
}

fun filterByCurrentMonth(medications: List<Medication>): List<Medication> {
    val sdf = SimpleDateFormat("MMyyyy", Locale.getDefault())
    val currentMonthYear = sdf.format(Date())

    return medications.filter { med ->
        val medicationMonthYear = sdf.format(med.medicationTime)
        medicationMonthYear.substring(0, 2) == currentMonthYear.substring(0, 2)
    }.distinctBy { it.name }
}

fun filterByCurrentYear(medications: List<Medication>): List<Medication> {
    val sdf = SimpleDateFormat("yyyy", Locale.getDefault())
    val currentYear = sdf.format(Date())

    return medications.filter { med ->
        val medicationYear = sdf.format(med.medicationTime)
        medicationYear == currentYear
    }.distinctBy { it.name }
}