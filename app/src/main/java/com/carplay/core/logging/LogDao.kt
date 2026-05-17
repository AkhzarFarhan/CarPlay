package com.carplay.core.logging

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Insert
    suspend fun insert(log: LogEntry)

    @Query("SELECT * FROM logs ORDER BY timestamp DESC LIMIT 100")
    fun getAllFlow(): Flow<List<LogEntry>>

    @Query("SELECT * FROM logs ORDER BY timestamp ASC")
    suspend fun getAllSync(): List<LogEntry>

    @Query("DELETE FROM logs WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Int>)

    @Query("DELETE FROM logs")
    suspend fun clearAll()
}
