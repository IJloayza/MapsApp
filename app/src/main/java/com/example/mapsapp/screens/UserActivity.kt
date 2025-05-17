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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
    val userViewModel: UserViewModel = viewModel()
    val authViewModel: AuthTokenJWTViewModel = viewModel()
    val user by userViewModel.user.observeAsState()
    val authState by authViewModel.authState.collectAsState()

    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val userId = sharedPref.getInt("userId", -1)
    val token = sharedPref.getString("accessToken", null)

    // Cargar usuario una sola vez cuando se autentique
    LaunchedEffect(authState) {
        if (authState == AuthState.AUTHENTICATED && user == null && userId != -1) {
            userViewModel.getUser(userId)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppHeader(stringResource(R.string.user), navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

            Column(
                modifier = contentModifier,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (authState) {
                    AuthState.AUTHENTICATED -> {
                        if (user != null) {
                            TextViewButtonStyle(stringResource(R.string.user) + ": " + user!!.nombre)
                            TextViewButtonStyle(stringResource(R.string.email) + ": "+ user!!.email)
                            TextViewButtonStyle(stringResource(R.string.registeredsince) + ": "+ user!!.fecha_registro)
                            PrimaryButton(stringResource(R.string.logout), Modifier.fillMaxWidth().height(60.dp)) {
                                authViewModel.logout()
                                navController.navigate(Login) {
                                    popUpTo(0)
                                }
                            }
                        } else {
                            CircularProgressIndicator()
                        }
                    }

                    AuthState.EXPIRED -> {
                        Text(stringResource(R.string.ExpiredToken))
                        PrimaryButton(stringResource(R.string.login), Modifier.fillMaxWidth().height(60.dp)) {
                            navController.navigate(Login) {
                                popUpTo(User) { inclusive = true }
                            }
                        }
                    }

                    AuthState.UNAUTHENTICATED -> {
                        Text(stringResource(R.string.NoRegistered))
                        PrimaryButton(stringResource(R.string.signup), Modifier.fillMaxWidth().height(60.dp)) {
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
