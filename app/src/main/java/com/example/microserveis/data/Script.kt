package com.example.microserveis.data

data class Script(
    val name: String
)
data class ScriptStatus(
    val scriptName: String,
    val status: String,
    val stdoutLogs: List<String>,
    val stderrLogs: List<String>
)

