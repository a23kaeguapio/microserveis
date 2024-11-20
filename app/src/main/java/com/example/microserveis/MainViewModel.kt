package com.example.microserveis

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.microserveis.communication.RetrofitClient
import com.example.microserveis.data.ScriptStatus
import io.socket.client.IO
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import io.socket.client.Socket
import java.net.URISyntaxException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _scripts = MutableLiveData<List<String>>()
    val scripts: LiveData<List<String>> get() = _scripts

    private val _scriptsStatus = MutableLiveData<Map<String, ScriptStatus>>()
    val scriptsStatus: LiveData<Map<String, ScriptStatus>> get() = _scriptsStatus

    private var socket: Socket? = null

    init {
        fetchScripts()
    }

    fun fetchScripts() {
        RetrofitClient.apiService.getScripts().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    _scripts.postValue(response.body() ?: emptyList())
                }
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e("App", "Error de conexión: ${t.message}")
            }
        })
    }

    fun fetchScriptStatus(scriptName: String) {
        RetrofitClient.apiService.getScriptStatus(scriptName).enqueue(object : Callback<ScriptStatus> {
            override fun onResponse(call: Call<ScriptStatus>, response: Response<ScriptStatus>) {
                if (response.isSuccessful) {
                    val updatedStatus = _scriptsStatus.value?.toMutableMap() ?: mutableMapOf()
                    val scriptStatus = response.body()
                    if (scriptStatus != null) {
                        updatedStatus[scriptName] = scriptStatus
                        _scriptsStatus.postValue(updatedStatus)
                    }
                }
            }

            override fun onFailure(call: Call<ScriptStatus>, t: Throwable) {
                Log.e("App", "Error de conexión: ${t.message}")
            }
        })
    }

    fun toggleScript(scriptName: String) {
        val currentStatus = _scriptsStatus.value?.get(scriptName)?.status ?: "stopped"
        val action = if (currentStatus == "running") "stop" else "start"

        RetrofitClient.apiService.controlScript(scriptName, action).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    val updatedStatus = _scriptsStatus.value?.toMutableMap() ?: mutableMapOf()
                    updatedStatus[scriptName] = ScriptStatus(scriptName, if (action == "start") "running" else "stopped", emptyList(), emptyList())
                    _scriptsStatus.postValue(updatedStatus)
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("App", "Error de conexión: ${t.message}")
            }
        })
    }

    fun connectToSocket() {
        try {
            socket = IO.socket("http://10.0.2.2:3000")
            socket?.on(Socket.EVENT_CONNECT) {
                Log.d("Socket.IO", "Conexión establecida")
            }
            socket?.on("status") { args ->
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    val scriptName = data.getString("script")
                    val status = data.getString("message")
                    val updatedStatus = _scriptsStatus.value?.toMutableMap() ?: mutableMapOf()
                    updatedStatus[scriptName] = ScriptStatus(scriptName, status, emptyList(), emptyList()) // Suponemos que los logs están vacíos al inicio
                    _scriptsStatus.postValue(updatedStatus)
                }
            }
            socket?.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }
}



