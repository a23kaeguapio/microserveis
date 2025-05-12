package com.example.microserveis.dao

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ServerConfigDao {
    @Query("SELECT * FROM server_config")
    fun getAll(): LiveData<List<ServerConfig>>

    @Query("SELECT * FROM server_config WHERE isDefault = 1 LIMIT 1")
    fun getDefault(): LiveData<ServerConfig?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(config: ServerConfig): Long

    @Update
    suspend fun update(config: ServerConfig)

    @Delete
    suspend fun delete(config: ServerConfig)

    @Query("UPDATE server_config SET isDefault = 0")
    suspend fun unsetAllDefault()
}
