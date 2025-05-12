package com.example.mapsapp.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.R
import com.example.mapsapp.components.AppHeader
import com.example.mapsapp.components.BottomNavigationBar
import com.example.mapsapp.components.PrimaryButton
import com.example.mapsapp.navigation.Home
import com.example.mapsapp.navigation.Login
import com.example.mapsapp.retrofit.models.LoginRequest
import com.example.mapsapp.retrofit.models.UserViewModel
import com.example.mapsapp.ui.theme.MapsAppTheme
import com.example.mapsapp.ui.theme.ThemeViewModel


@Composable
    fun LoginScreen(navController: NavController) {
        Scaffold (
            topBar = { AppHeader(stringResource(R.string.login), navController) },      // Barra superior
            bottomBar = { BottomNavigationBar(navController) }, // Barra inferior
            content = { paddingValues ->
                LoginForm(paddingValues, navController)
            }
        )
    }

@Composable
fun LoginForm(paddingValues: PaddingValues, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(10.dp)
                .wrapContentSize(Alignment.Center),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){

            Text(text = stringResource(R.string.email), fontSize = 20.sp, fontWeight = Bold)

            // Password Input
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text(text = stringResource(R.string.testUserEmail), color = Color.Gray) },
            )

            Text(text = stringResource(R.string.password), fontSize = 20.sp, fontWeight = Bold)

            // Password Input
            TextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text(text = stringResource(R.string.testUserPassword), color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation()
            )

            PrimaryButton(text = stringResource(R.string.login), modifier = Modifier.fillMaxWidth()) {
                if (email.isNotBlank() && password.isNotBlank()) {
                    val request = LoginRequest(email = email, contrasena = password)
                    userViewModel.login(request) { success ->
                        if (success) {
                            navController.navigate(Home) {
                                popUpTo(Login) { inclusive = true }
                            }
                        } else {
                            val userunknown = context.getString(R.string.UserUnknown)
                            Toast.makeText(context, userunknown, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val noAllFields = context.getString(R.string.NoAllFields)
                    Toast.makeText(context, noAllFields, Toast.LENGTH_SHORT).show()
                }
            }
            // Forgot Password Link
            Text(
                text = stringResource(R.string.forgotpassword),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { /* Acción para recuperar contraseña */ },
                fontSize = 14.sp,
                fontWeight = Bold,
                color = Color.Black,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}


@Preview
@Composable
fun PreviewLoginScreen() {
    val themeViewModel: ThemeViewModel = viewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    MapsAppTheme(darkTheme = isDarkTheme) {
        val fakeNavController = rememberNavController()
        LoginScreen(fakeNavController)
    }
}

