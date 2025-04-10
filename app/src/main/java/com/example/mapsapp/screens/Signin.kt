package com.example.mapsapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.components.AppHeader
import com.example.mapsapp.components.PrimaryButton
import com.example.mapsapp.components.SpacerVertical
import com.example.mapsapp.navigation.BottomNavigationBar
import com.example.mapsapp.navigation.User
import com.example.mapsapp.ui.theme.MapsAppTheme

@Composable
fun SigninScreen(navController: NavController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Scaffold (
        topBar = { AppHeader("Sign In", navController) },      // Barra superior
        bottomBar = { BottomNavigationBar(navController) }, // Barra inferior
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpacerVertical(32)
                Text(text = "Email", fontSize = 20.sp, fontWeight = Bold)

                // Email Input
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text(text = "Value", color = Color.Gray) }
                )

                Text(text = "Password", fontSize = 20.sp, fontWeight = Bold)

                // Password Input
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text(text = "Value", color = Color.Gray) },
                    visualTransformation = PasswordVisualTransformation()
                )

                PrimaryButton("Sign In", Modifier.weight(1f)){}
                // Forgot Password Link
                Text(
                    text = "Forgot password?",
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
    )
}

@Preview
@Composable
fun PreviewSignInScreen() {
    MapsAppTheme {
        val fakeNavController = rememberNavController()
        SigninScreen(fakeNavController)
    }
}
