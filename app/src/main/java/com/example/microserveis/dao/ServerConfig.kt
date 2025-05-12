package com.example.microserveis.dao

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "server_config")
data class ServerConfig(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val color: String,
    val host: String,
    val port: Int,
    val isDefault: Boolean = false
)

