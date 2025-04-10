package com.example.mapsapp.screens

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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.components.AppHeader
import com.example.mapsapp.components.PrimaryButton
import com.example.mapsapp.components.TextViewButtonStyle
import com.example.mapsapp.navigation.BottomNavigationBar
import com.example.mapsapp.navigation.User
import com.example.mapsapp.ui.theme.MapsAppTheme


    @Composable
    fun UserScreen(navController: NavController){
        Scaffold(
            topBar = { AppHeader("User", navController) },
            bottomBar = { BottomNavigationBar(navController) },
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
    fun PreviewUserScreen() {
        MapsAppTheme {
            val fakeNavController = rememberNavController()
            UserScreen(fakeNavController)
        }
    }
