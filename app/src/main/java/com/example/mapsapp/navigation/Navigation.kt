package com.example.mapsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.MainActivity
import com.example.mapsapp.screens.LoginActivity
import com.example.mapsapp.screens.MapsActivity
import com.example.mapsapp.screens.SettingsActivity
import com.example.mapsapp.screens.UserActivity

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login){
        composable<Login>{
            LoginActivity(){navController.navigate()}
        }
        composable<Settings>{
            SettingsActivity()
        }
        composable<Home>{
            MainActivity()
        }
        composable<Maps>{
            MapsActivity()
        }
        composable<User>{
            UserActivity()
        }
    }
}