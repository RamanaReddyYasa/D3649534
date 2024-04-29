package uk.ac.tees.mad.d3649534.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity
data class MedicationEntity(
    @PrimaryKey
    val id: Long,
    val medId: String,
    val name: String,
    val dosage: String,
    val frequency: String,
    @TypeConverters(Convertor::class)
    val medicationTime: Date,
    @TypeConverters(MedicationTimeConverter::class)
    val medicationTimesList: List<Date>,
    val startDate: Date,
    val endDate: Date,
    val medicationTaken: Boolean,
    val image: ByteArray? = null
)

