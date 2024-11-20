package com.example.microserveis

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun LogsError(viewModel: MainViewModel, scriptName: String) {
    val scriptStatus by viewModel.scriptsStatus.observeAsState()

    LaunchedEffect(scriptName) {
        viewModel.fetchScriptStatus(scriptName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Logs de Error") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) {
        scriptStatus?.get(scriptName)?.let { logs ->
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(logs.stderrLogs) { log ->
                    Text(text = log, modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.error)
                }
            }
        } ?: Text("Cargando logs de error...")
    }
}
