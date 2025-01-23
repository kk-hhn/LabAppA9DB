package com.example.labappa9db.view


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Dashboard
 * main navController function containing the 3 necessary routes, each calling their own UI function
 * @return null
 * */

@Composable
fun Dashboard() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { navController.navigate("Unfinished_Todos") }) {
                    Text("List of unfinished Todos", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("Finished_Todos") }) {
                    Text("List of finished Todos", fontSize = 24.sp)
                }
            }
        }
        composable("Finished_Todos") {
            val context = LocalContext.current
            FinishedTodosScreen(
                context = context,
                navController = navController
            )
        }
        composable("Unfinished_Todos"){
            val context = LocalContext.current
            UnfinishedTodosScreen(context= context, navController=navController)
        }
    }
}