package uk.ac.tees.mad.d3649534.data.database

import androidx.room.TypeConverter
import java.util.Date

class Convertor {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}