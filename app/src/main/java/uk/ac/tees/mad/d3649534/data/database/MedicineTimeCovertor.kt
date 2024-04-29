package uk.ac.tees.mad.d3649534.data.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MedicationTimeConverter {

    @TypeConverter
    fun fromMedicationTimeList(medicationTimes: List<Date>): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
        return medicationTimes.joinToString(separator = ",") { format.format(it) }
    }

    @TypeConverter
    fun toMedicationTimeList(medicationTimesString: String): List<Date> {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
        return medicationTimesString.split(",").map { format.parse(it) }
    }
}