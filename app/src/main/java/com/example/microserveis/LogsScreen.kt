package com.example.microserveis

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(viewModel: MainViewModel, scriptName: String) {
    val scriptStatus by viewModel.scriptsStatus.observeAsState()

    LaunchedEffect(scriptName) {
        viewModel.fetchScriptStatus(scriptName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Logs de Salida") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues -> // Capturamos el paddingValues del Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aplicamos el padding del Scaffold
                .padding(top = 16.dp) // Ajustamos un margen superior adicional para separación
        ) {
            scriptStatus?.get(scriptName)?.let { logs ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(logs.stdoutLogs) { log ->
                        Text(
                            text = log,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } ?: Text(
                "Cargando logs...",
                modifier = Modifier.align(Alignment.Center), // Centramos el mensaje de carga
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

