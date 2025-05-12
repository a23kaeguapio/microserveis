package com.example.microserveis.dao


class ServerConfigRepository(private val dao: ServerConfigDao) {
    val allConfigs = dao.getAll()
    val defaultConfig = dao.getDefault()

    suspend fun insert(config: ServerConfig): Long = dao.insert(config)

    suspend fun update(config: ServerConfig) = dao.update(config)

    suspend fun delete(config: ServerConfig) = dao.delete(config)

    suspend fun setAsDefault(config: ServerConfig) {
        dao.unsetAllDefault()
        dao.update(config.copy(isDefault = true))
    }
}
