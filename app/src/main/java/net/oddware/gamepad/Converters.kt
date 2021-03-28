package net.oddware.gamepad

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun ts2date(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun date2ts(date: Date?): Long? {
        return date?.time
    }
}