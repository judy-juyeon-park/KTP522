package com.example.talevoice.data.source.local
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Tale"
)
data class LocalTaleListItem(
    @PrimaryKey val taleId: String,
    val title: String,
    val version: Int
)
