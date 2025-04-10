package com.example.mapsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.components.AppHeader
import com.example.mapsapp.navigation.BottomNavigationBar
import com.example.mapsapp.navigation.NavigationWrapper
import com.example.mapsapp.navigation.User
import com.example.mapsapp.ui.theme.MapsAppTheme

class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapsAppTheme {
                NavigationWrapper()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header con el nombre de la app y el botón de usuario
        AppHeader("Picotrake", navController)

        // Contenido principal de la pantalla
        LazyColumn (
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ("Bienvenido a PicoTrake!")
        }

        // BottomNavigationBar
        BottomNavigationBar(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    MapsAppTheme {
        val fakeNavController = rememberNavController()
        MainScreen(fakeNavController)
    }
}