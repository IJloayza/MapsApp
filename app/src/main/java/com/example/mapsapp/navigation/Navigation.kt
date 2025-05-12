package com.example.mapsapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.MainScreen
import com.example.mapsapp.screens.LoginScreen
import com.example.mapsapp.screens.MapsScreen
import com.example.mapsapp.screens.SettingsScreen
import com.example.mapsapp.screens.SignUpScreen
import com.example.mapsapp.screens.UserScreen
import com.example.mapsapp.ui.theme.ThemeViewModel

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val themeViewModel: ThemeViewModel = viewModel()
    NavHost(navController, startDestination = Home){
        composable<Login>{
            LoginScreen(navController)
        }

        composable<SignUp>{
            SignUpScreen(navController)
        }

        composable<Settings>{
            SettingsScreen(navController, themeViewModel)
        }

        composable<Home>{
            MainScreen(navController)
        }

        composable<Maps>{
            MapsScreen(navController)
        }
        composable<User> {
            UserScreen(navController)
        }
    }
}

