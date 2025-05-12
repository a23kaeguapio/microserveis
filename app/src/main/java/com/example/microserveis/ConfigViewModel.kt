package com.example.microserveis

import android.app.Application
import androidx.lifecycle.*
import com.example.microserveis.dao.ServerConfig
import com.example.microserveis.dao.ServerConfigRepository
import com.example.microserveis.data.*
import kotlinx.coroutines.launch

class ConfigViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ServerConfigRepository
    val allConfigs: LiveData<List<ServerConfig>>
    val defaultConfig: LiveData<ServerConfig?>

    init {
        val dao = AppDatabase.getDatabase(application).configDao()
        repository = ServerConfigRepository(dao)
        allConfigs = repository.allConfigs
        defaultConfig = repository.defaultConfig
    }

    fun insert(config: ServerConfig) = viewModelScope.launch {
        repository.insert(config)
    }

    fun update(config: ServerConfig) = viewModelScope.launch {
        repository.update(config)
    }

    fun delete(config: ServerConfig) = viewModelScope.launch {
        repository.delete(config)
    }

    fun setAsDefault(config: ServerConfig) = viewModelScope.launch {
        repository.setAsDefault(config)
    }
}
