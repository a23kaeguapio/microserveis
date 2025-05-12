package com.example.microserveis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.microserveis.dao.ServerConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConfiguracions(
    navController: NavController,
    viewModel: ConfigViewModel
) {
    val configuracions by viewModel.allConfigs.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuracions de Servidor") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Enrere")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.insert(
                    ServerConfig(
                        label = "Nou entorn",
                        color = "#2196F3",
                        host = "127.0.0.1",
                        port = 3000
                    )
                )
            }) {
                Icon(Icons.Default.Add, contentDescription = "Afegir Configuració")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyColumn {
                items(configuracions) { config ->
                    ConfigRow(config, viewModel)
                }
            }
        }
    }
}

@Composable
fun ConfigRow(config: ServerConfig, viewModel: ConfigViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(android.graphics.Color.parseColor(config.color)))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Etiqueta: ${config.label}")
            Text("Host: ${config.host}")
            Text("Port: ${config.port}")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { viewModel.setAsDefault(config) }) {
                    Text(if (config.isDefault) "Per defecte ✔" else "Fer per defecte")
                }
                Button(onClick = { viewModel.delete(config) }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Eliminar")
                }
            }
        }
    }
}
