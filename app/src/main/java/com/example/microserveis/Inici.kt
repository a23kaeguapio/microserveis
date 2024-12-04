package com.example.microserveis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inici(navController: NavHostController, viewModel: MainViewModel) {
    val scripts by viewModel.scripts.observeAsState(emptyList())
    val scriptsStatus by viewModel.scriptsStatus.observeAsState(emptyMap())

    LaunchedEffect(Unit) {
        viewModel.fetchScripts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Microserveis") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn {
                items(scripts) { script ->
                    ScriptRow(
                        scriptName = script,
                        status = scriptsStatus[script]?.status ?: "stopped",
                        onStartStop = { viewModel.toggleScript(script) },
                        onViewLogs = { navController.navigate("logs/output/$script") },
                        onViewErrorLogs = { navController.navigate("logs/error/$script") }
                    )
                }
            }
        }
    }
}

@Composable
fun ScriptRow(
    scriptName: String,
    status: String,
    onStartStop: () -> Unit,
    onViewLogs: () -> Unit,
    onViewErrorLogs: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = scriptName, modifier = Modifier.weight(1f))

        Button(
            onClick = onStartStop,
            modifier = Modifier.padding(horizontal = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (status == "running") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        ) {
            Text(if (status == "running") "Detener" else "Iniciar")
        }

        Button(onClick = onViewLogs, modifier = Modifier.padding(horizontal = 4.dp)) {
            Text("Logs")
        }

        Button(onClick = onViewErrorLogs, modifier = Modifier.padding(horizontal = 4.dp)) {
            Text("Errores")
        }
    }
}
