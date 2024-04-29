package uk.ac.tees.mad.d3649534.data.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uk.ac.tees.mad.d3649534.utils.Frequency

import java.util.Date

@Parcelize
data class Medication(
    val id: Long = 0,
    val medId: String = "",
    val name: String = "",
    val dosage: String = "",
    val frequency: String = Frequency.Daily.name,
    val medicationTime: Date = Date(),
    val medicationTimesList: List<Date> = listOf(),
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val medicationTaken: Boolean = false,
    val image: ByteArray? = null
): Parcelable