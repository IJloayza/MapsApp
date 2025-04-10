package com.example.mapsapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.mapsapp.components.SpacerVertical
import com.example.mapsapp.components.TextViewButtonStyle
import com.example.mapsapp.navigation.BottomNavigationBar
import com.example.mapsapp.navigation.User
import com.example.mapsapp.ui.theme.MapsAppTheme

@Composable
fun SettingsScreen(navController: NavController){
    Scaffold(
        topBar = { AppHeader("Settings", navController) },      // Barra superior
        bottomBar = { BottomNavigationBar(navController) }, // Barra inferior
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
                    verticalArrangement = Arrangement.Center, // Asegura que los elementos se alineen verticalmente
                    horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente los hijos
                ) {
                    TextViewButtonStyle("Appearance")
                    SpacerVertical(16)
                    Row (
                        //Obligatorio colocar distribución de elementos
                        modifier = Modifier.fillMaxWidth(), // Ocupar ancho disponible
                        horizontalArrangement = Arrangement.SpaceEvenly // Espaciado entre los botones
                    ){
                        PrimaryButton("Dark", Modifier.weight(1f)) { }
                        PrimaryButton("Light", Modifier.weight(1f)) {}
                    }
                    TextViewButtonStyle("Storage")
                    SpacerVertical(16)
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        PrimaryButton("Storage", Modifier.weight(1f)){}
                        PrimaryButton("Quantity", Modifier.weight(1f)){}
                    }
                    SpacerVertical(16)
                    TextViewButtonStyle("Language")
                    SpacerVertical(16)
                    PrimaryButton("English", Modifier.fillMaxWidth()){}
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    MapsAppTheme {
        val fakeNavController = rememberNavController()
        SettingsScreen(fakeNavController)
    }
}