package com.example.talevoice.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

@Database(entities = [LocalTaleListItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaleDatabase : RoomDatabase() {
    abstract fun taleDao(): TaleDao
}

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
