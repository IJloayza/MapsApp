package com.example.mapsapp.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHost
import com.example.mapsapp.components.AppHeader
import com.example.mapsapp.components.BottomNavigationBar
import com.example.mapsapp.components.PrimaryButton
import com.example.mapsapp.components.TextViewButtonStyle
import com.example.mapsapp.navigation.Navigation
import com.example.mapsapp.ui.theme.MapsAppTheme

class UserActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavHost()
            MapsAppTheme {
                UserScreen()
            }
        }
    }
    @Composable
    fun UserScreen(){
        Scaffold(
            topBar = { AppHeader("User") },      // Barra superior
            bottomBar = { BottomNavigationBar() }, // Barra inferior
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()  // Ocupa toda la pantalla
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center // Centra el contenido
                ) {
                    Column(
                        modifier = Modifier
                            .width(width = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente los hijos
                    ) {
                        TextViewButtonStyle("User: John Dou")
                        Spacer(modifier = Modifier.height(32.dp)) // Espacio entre botones
                        TextViewButtonStyle("Email: JohnDou@gmail.com")
                        Spacer(modifier = Modifier.height(32.dp)) // Espacio entre botones
                        TextViewButtonStyle("Storage")
                        Spacer(modifier = Modifier.height(32.dp))
                        PrimaryButton("Log Out", Modifier.fillMaxWidth()){} //Retrofit logout
                    }
                }
            }
        )
    }

    @Preview
    @Composable
    fun PreviewSettingsScreen() {
        MapsAppTheme {
            UserScreen()
        }
    }
}