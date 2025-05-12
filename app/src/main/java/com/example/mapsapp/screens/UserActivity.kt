package com.example.mapsapp.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.auth0.android.jwt.JWT
import com.example.mapsapp.components.AppHeader
import com.example.mapsapp.components.BottomNavigationBar
import com.example.mapsapp.components.PrimaryButton
import com.example.mapsapp.components.TextViewButtonStyle
import com.example.mapsapp.navigation.Login
import com.example.mapsapp.navigation.SignUp
import com.example.mapsapp.navigation.User
import com.example.mapsapp.retrofit.AuthState
import com.example.mapsapp.retrofit.AuthTokenJWTViewModel
import com.example.mapsapp.retrofit.models.UserViewModel
import com.example.mapsapp.ui.theme.MapsAppTheme
import com.example.mapsapp.R

@Composable
fun UserScreen(navController: NavController) {
    val context = LocalContext.current
    var userViewModel: UserViewModel = viewModel()
    var authViewModel: AuthTokenJWTViewModel = viewModel()
    val user by userViewModel.user.observeAsState()
    val authState by authViewModel.authState.collectAsState()

    // Obtener el token desde SharedPreferences
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val userId = sharedPref.getInt("userId", -1)
    val token = sharedPref.getString("accessToken", null)

    // Cargar el usuario si está autenticado y aún no se ha hecho
    LaunchedEffect(authState) {
        if (authState == AuthState.AUTHENTICATED && user == null) {
            token?.let {
                try {
                    val jwt = JWT(it)
                    //de JWT debo extraer el sub de ahi el Int id de usuario y ya poder hacer un getUserById(id)
                    if (userId != -1) {
                        userViewModel.getUser(userId)
                    }else{
                        Log.e("UserScreen", "Error reading JWT: No userId found")
                    }
                } catch (e: Exception) {
                    Log.e("UserScreen", "Error reading JWT: ${e.message}")
                }
            }
        }
    }

    Scaffold(
        topBar = { AppHeader(stringResource(R.string.user), navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (authState) {
                AuthState.AUTHENTICATED -> {
                    val currentUser = user
                    //Mirar solo currentUser ya que by no asegura que llegue el user hasta el siguiente uso
                    //Decido no usar el logOut del viewModel para eliminar al usuario sino solo cambiar las preferencias y olvidar el token
                    if (currentUser != null) {
                        Column(
                            modifier = Modifier.width(300.dp),
                            verticalArrangement = Arrangement.spacedBy(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TextViewButtonStyle(stringResource(R.string.user)+ currentUser.nombre)
                            TextViewButtonStyle(stringResource(R.string.email)+ currentUser.email)
                            TextViewButtonStyle(stringResource(R.string.registeredsince) + currentUser.fecha_registro)
                            PrimaryButton(stringResource(R.string.logout), Modifier.fillMaxWidth()) {
                                authViewModel.logout()
                                navController.navigate(Login) {
                                    popUpTo(0)
                                }
                            }
                        }
                    } else {
                        CircularProgressIndicator()
                    }
                }

                AuthState.EXPIRED -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.ExpiredToken))
                        Spacer(modifier = Modifier.height(16.dp))
                        PrimaryButton(stringResource(R.string.login), Modifier.fillMaxWidth()) {
                            navController.navigate(Login) {
                                popUpTo(User) { inclusive = true }
                            }
                        }
                    }
                }

                AuthState.UNAUTHENTICATED -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.NoRegistered))
                        Spacer(modifier = Modifier.height(16.dp))
                        PrimaryButton(stringResource(R.string.signup), Modifier.fillMaxWidth()) {
                            navController.navigate(SignUp) {
                                popUpTo(User) { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    }
}

    @Preview
    @Composable
    fun PreviewUserScreen() {
        MapsAppTheme(darkTheme = false) {
            val fakeNavController = rememberNavController()
            UserScreen(fakeNavController)
        }
    }
