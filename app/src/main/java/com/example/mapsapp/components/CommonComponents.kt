package com.example.mapsapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.R
import com.example.mapsapp.navigation.Home
import com.example.mapsapp.navigation.Login
import com.example.mapsapp.navigation.Maps
import com.example.mapsapp.navigation.Settings
import com.example.mapsapp.navigation.SignUp
import com.example.mapsapp.navigation.User
import com.example.mapsapp.retrofit.AuthState
import com.example.mapsapp.retrofit.AuthTokenJWTViewModel
import com.example.mapsapp.ui.theme.MapsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(title: String, navController: NavController) {
    val jwtViewModel: AuthTokenJWTViewModel = viewModel()
    val authState by jwtViewModel.authState.collectAsState()

    TopAppBar(
        title = {
            Text(
                title,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                when (authState) {
                    AuthState.AUTHENTICATED -> {
                        //Posible cambio para boton para hacerse premium
                        Text(
                            text = stringResource(R.string.HeaderWelcome),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    AuthState.EXPIRED -> {
                        PrimaryButton(
                            text = stringResource(R.string.login),
                            modifier = Modifier.height(40.dp),
                            onClick = {
                                navController.navigate(Login) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }

                    AuthState.UNAUTHENTICATED -> {
                        PrimaryButton(
                            text = stringResource(R.string.signup),
                            modifier = Modifier.height(40.dp),
                            onClick = {
                                navController.navigate(SignUp) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    )
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

@Composable
fun SpacerVertical(size: Int) {
    Spacer(modifier = Modifier.height(size.dp))
}

@Composable
fun PrimaryButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary)
    ) {
        Text(text = text, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary, fontWeight = Bold)
    }
}


@Composable
fun TextViewButtonStyle(text: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(8.dp, shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary, // Color del texto igual al color de fondo del botón
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = Bold,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Preview
@Composable
fun PrimaryView() {
    MapsAppTheme(darkTheme = false) {
        Column {
            val fakeNavController = rememberNavController()
            AppHeader("PICOTRAKE", fakeNavController)
            PrimaryButton("Button", Modifier.weight(1f), { 2 + 2 })
            TextViewButtonStyle("Text")
            BottomNavigationBar(fakeNavController)
        }
    }
}

//Posible consumo de imagenes por url
//@Composable
//fun LoadImageWithGlide(url: String) {
//
//        GlideImage(
//            model = url,
//            contentDescription = "Image from URL"
//       )
//}