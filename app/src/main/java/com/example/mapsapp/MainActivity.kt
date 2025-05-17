package com.example.mapsapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.components.AppHeader
import com.example.mapsapp.components.BottomNavigationBar
import com.example.mapsapp.navigation.NavigationWrapper
import com.example.mapsapp.components.LocaleManager
import com.example.mapsapp.navigation.Maps
import com.example.mapsapp.ui.theme.MapsAppTheme
import com.example.mapsapp.ui.theme.ThemeViewModel

class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //Verificar los permisos para poder usar el internet
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 0)
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            //Pasando el tema actual a partir de un viewModel para manejo global del tema
            MapsAppTheme(darkTheme = isDarkTheme) {
                NavigationWrapper()
            }
        }
    }

    //Cambiar el Manager general de Android por mi LocalManager
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.applyLocale(newBase!!))
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
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
            item {
                Text(
                    text = stringResource(R.string.HeaderWelcome),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                AdCard(
                    title = stringResource(R.string.new_route_title),
                    description = stringResource(R.string.new_route_description),
                    imageResId = R.drawable.newroute,
                    onClick = {
                        navController.navigate(Maps){
                            popUpTo(Maps){
                                inclusive = true
                            }
                        }
                    }
                )
            }

            item {
                AdCard(
                    title = stringResource(R.string.advice_camping_title),
                    description = stringResource(R.string.advice_camping_description),
                    imageResId = R.drawable.advicecamping,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://koa.com/blog/camping-safety-tips/"))
                        context.startActivity(intent)
                    }
                )
            }

            item {
                AdCard(
                    title = stringResource(R.string.best_summer_mountains_title),
                    description = stringResource(R.string.best_summer_mountains_description),
                    imageResId = R.drawable.mountainsummer,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://entremontanas.com/noticia/5-rutas-para-el-verano-en-cataluna"))
                        context.startActivity(intent)
                    }
                )
            }

            item {
                AdCard(
                    title = stringResource(R.string.ideal_equipment_title),
                    description = stringResource(R.string.ideal_equipment_description),
                    imageResId = R.drawable.epi,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://hikingguy.com/best-hiking-gear/"))
                        context.startActivity(intent)
                    }
                )
            }
        }
        //Nav bar de abajo
        BottomNavigationBar(navController)
    }
}

@Composable
fun AdCard(title: String, description: String, imageResId: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick() }
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                ,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Añadiendo Imagen de 200x120 para mis anuncios
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Fit
            )

            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = description, style = MaterialTheme.typography.bodyMedium)

            Text(
                text = stringResource(R.string.learnmore),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    MapsAppTheme(darkTheme = false) {
        val fakeNavController = rememberNavController()
        MainScreen(navController = fakeNavController)
    }
}