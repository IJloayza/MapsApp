package com.example.mapsapp.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.MainActivity
import com.example.mapsapp.MainScreen
import com.example.mapsapp.screens.LoginScreen
import com.example.mapsapp.screens.MapsScreen
import com.example.mapsapp.screens.SettingsScreen
import com.example.mapsapp.screens.UserScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Main){
        composable<Login>{
            LoginScreen(navController)
        }
        composable<Settings>{
            SettingsScreen(navController)
        }
        composable<Main>{
            MainScreen(navController)
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

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(Home, Maps, Settings, User)
    val icons = listOf(Icons.Default.Home, Icons.Default.Map, Icons.Default.Settings, Icons.Default.Person)

    // Estado para la opción seleccionada
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = "Icono bottombar", tint = MaterialTheme.colorScheme.onPrimary) },
                selected = currentRoute == item,
                onClick = {
                    navController.navigate(item) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true } // Evita duplicados
                        launchSingleTop = true // No volver a crear si ya está en la misma pantalla
                        restoreState = true // Mantiene el estado anterior
                    }
                }
            )
        }
    }
}