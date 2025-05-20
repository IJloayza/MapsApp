package com.example.mapsapp.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.MainActivity
import com.example.mapsapp.R
import com.example.mapsapp.components.AppHeader
import com.example.mapsapp.components.BottomNavigationBar
import com.example.mapsapp.components.PrimaryButton
import com.example.mapsapp.components.SpacerVertical
import com.example.mapsapp.components.TextViewButtonStyle
import com.example.mapsapp.components.LocaleManager
import com.example.mapsapp.ui.theme.MapsAppTheme
import com.example.mapsapp.ui.theme.ThemeViewModel

@Composable
fun SettingsScreen(navController: NavController, themeViewModel: ThemeViewModel){

    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppHeader(stringResource(R.string.settings), navController) },      // Barra superior
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
                    TextViewButtonStyle(stringResource(R.string.appereance))
                    SpacerVertical(16)
                    Row (
                        //Obligatorio colocar distribución de elementos
                        modifier = Modifier.fillMaxWidth(), // Ocupar ancho disponible
                        horizontalArrangement = Arrangement.SpaceEvenly // Espaciado entre los botones
                    ){
                        PrimaryButton(stringResource(R.string.themeto) + " " + if (isDarkTheme) stringResource(R.string.light) else stringResource(R.string.dark), Modifier
                            .weight(1f)
                            .height(56.dp)
                            .padding(0.dp, 0.dp, 0.dp, 16.dp)) { themeViewModel.toggleTheme() }
                    }
//                    // Posible muestra del storage ocupado por las rutas, descartado ya que se almacena en base de datos
//                    TextViewButtonStyle("Storage")
//                    SpacerVertical(16)
//                    Row (
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceEvenly
//                    ){
//                        PrimaryButton("Storage", Modifier.weight(1f)){}
//                        PrimaryButton("Quantity", Modifier.weight(1f)){}
//                    }
                    SpacerVertical(16)
                    TextViewButtonStyle(stringResource(id = R.string.language))
                    SpacerVertical(16)
                    LanguageDropdown(LocalContext.current)
                }
            }
        }
    )
}

@Composable
fun LanguageDropdown(context: Context) {
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val savedLanguage = sharedPref.getString("language", "en") ?: "en"
    val languages = listOf("en" to "English", "es" to "Español", "fr" to "Français", "ca" to "Català")

    var currentLanguage by remember { mutableStateOf(savedLanguage) }
    var expanded by remember { mutableStateOf(false) }

    Box {
        PrimaryButton(
            text = languages.firstOrNull { it.first == currentLanguage }?.second ?: "English",
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            expanded = true
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { (code, label) ->
                DropdownMenuItem(
                    modifier = Modifier.width(150.dp).height(45.dp),
                    text = { Text(label) },
                    onClick = {
                        currentLanguage = code
                        expanded = false
                        changeLanguage(context, code)
                    }
                )
            }
        }
    }
}

//Cuando se use changeLanguage notifica a mi LocalManager en components
fun changeLanguage(context: Context, languageCode: String) {
    val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    prefs.edit().putString("language", languageCode).apply()

    val updatedContext = LocaleManager.setLocale(context, languageCode)
    val intent = Intent(updatedContext, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    updatedContext.startActivity(intent)
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    MapsAppTheme(darkTheme = false) {
        val fakeNavController = rememberNavController()
        val themeViewModel = ThemeViewModel()
        SettingsScreen(fakeNavController, themeViewModel)
    }
}

