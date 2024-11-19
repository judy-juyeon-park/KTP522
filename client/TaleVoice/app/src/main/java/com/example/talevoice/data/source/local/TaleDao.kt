package com.example.talevoice.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.talevoice.data.TaleListItem

@Dao
interface TaleDao {

    @Transaction
    @Query("SELECT * FROM Tale")
    suspend fun getTaleList(): List<TaleListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTaleList(taleList: List<LocalTaleListItem>)

}