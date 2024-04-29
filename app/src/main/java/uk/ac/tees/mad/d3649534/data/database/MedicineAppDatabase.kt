package uk.ac.tees.mad.d3649534.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [MedicationEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Convertor::class, MedicationTimeConverter::class)
abstract class MedicineAppDatabase : RoomDatabase() {
    abstract fun getMedicineDao(): MedicineAppDao
}