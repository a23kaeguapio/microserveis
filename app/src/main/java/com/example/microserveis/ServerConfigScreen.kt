package com.example.microserveis

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.microserveis.dao.ServerConfig
import com.example.microserveis.ConfigViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    navController: NavHostController,
    configViewModel: ConfigViewModel = viewModel()
) {
    val configs by configViewModel.configs.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingConfig by remember { mutableStateOf<ServerConfig?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuracions de Servidors") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingConfig = null
                showDialog = true
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(configs) { config ->
                    ConfigRow(
                        config = config,
                        onEdit = {
                            editingConfig = config
                            showDialog = true
                        },
                        onDelete = { configViewModel.delete(config) },
                        onSetDefault = { configViewModel.setAsDefault(config) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        ConfigDialog(
            config = editingConfig,
            onDismiss = { showDialog = false },
            onSave = {
                configViewModel.addOrUpdate(it)
                showDialog = false
            }
        )
    }
}

@Composable
fun ConfigRow(
    config: ServerConfig,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSetDefault: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(android.graphics.Color.parseColor(config.color)))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = config.label, style = MaterialTheme.typography.titleMedium)
            Text(text = "${config.host}:${config.port}")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onEdit) { Text("Editar") }
                TextButton(onClick = onDelete) { Text("Eliminar") }
                if (!config.isDefault) {
                    TextButton(onClick = onSetDefault) { Text("Predeterminada") }
                } else {
                    Text("(Predeterminada)", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigDialog(
    config: ServerConfig?,
    onDismiss: () -> Unit,
    onSave: (ServerConfig) -> Unit
) {
    var label by remember { mutableStateOf(TextFieldValue(config?.label ?: "")) }
    var color by remember { mutableStateOf(TextFieldValue(config?.color ?: "#FFFFFF")) }
    var host by remember { mutableStateOf(TextFieldValue(config?.host ?: "")) }
    var port by remember { mutableStateOf(TextFieldValue(config?.port?.toString() ?: "")) }
    var isDefault by remember { mutableStateOf(config?.isDefault ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val newConfig = ServerConfig(
                    id = config?.id ?: 0,
                    label = label.text,
                    color = color.text,
                    host = host.text,
                    port = port.text.toIntOrNull() ?: 80,
                    isDefault = isDefault
                )
                onSave(newConfig)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel·lar") }
        },
        title = { Text("Configuració") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Etiqueta") })
                OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color (#RRGGBB)") })
                OutlinedTextField(value = host, onValueChange = { host = it }, label = { Text("Host") })
                OutlinedTextField(value = port, onValueChange = { port = it }, label = { Text("Port") })
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Checkbox(checked = isDefault, onCheckedChange = { isDefault = it })
                    Text("Establir com a predeterminada")
                }
            }
        }
    )
}
