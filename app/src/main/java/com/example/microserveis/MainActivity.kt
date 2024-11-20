package com.example.microserveis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()

            val navController = rememberNavController()
                AppNavigation (navController,viewModel)
        }
    }
}
@Composable
fun AppNavigation (navController: NavHostController,viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = "pantallaIni"){
        composable("pantallaIni") { Inici(navController, viewModel) }
        composable("logs/output/{scriptName}") { backStackEntry ->
            val scriptName = backStackEntry.arguments?.getString("scriptName") ?: ""
            LogsScreen(viewModel, scriptName)
        }
        composable("logs/error/{scriptName}") { backStackEntry ->
            val scriptName = backStackEntry.arguments?.getString("scriptName") ?: ""
            LogsError(viewModel, scriptName)
        }
    }
}

