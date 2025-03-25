package com.example.mapsapp.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.mapsapp.components.AppHeader
import com.example.mapsapp.components.BottomNavigationBar
import com.example.mapsapp.components.PrimaryButton
import com.example.mapsapp.components.SpacerVertical
import com.example.mapsapp.ui.theme.MapsAppTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapsAppTheme {
                LoginScreen()
            }
        }
    }

    @Composable
    fun LoginScreen(){
        Scaffold (
            topBar = { AppHeader("Log In") },      // Barra superior
            bottomBar = { BottomNavigationBar() }, // Barra inferior
            content = { paddingValues ->
                LoginForm(paddingValues)
            }
        )
    }

    @Preview
    @Composable
    fun PreviewLoginScreen() {
        MapsAppTheme {
            LoginScreen()
        }
    }

    @Composable
    fun LoginForm(paddingValues: PaddingValues) {
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpacerVertical(32)
            Text(text = "First Name", fontSize = 20.sp, fontWeight = Bold)

            // Email Input
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text(text = "John", color = Color.Gray) }
            )

            Text(text = "Last Name", fontSize = 20.sp, fontWeight = Bold)

            // Password Input
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text(text = "Dou", color = Color.Gray) },
            )

            Text(text = "Email", fontSize = 20.sp, fontWeight = Bold)

            // Password Input
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text(text = "JohnDou@gmail.com", color = Color.Gray) },
            )

            Text(text = "Password", fontSize = 20.sp, fontWeight = Bold)

            // Password Input
            TextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text(text = "56fgKFcj%", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation()
            )

            PrimaryButton("Log In", Modifier.fillMaxWidth()){
                //Si se ha reconocido al usuario se activa el apartado user y te redirige a UserActivity
                if (userViewModel.isSuccesfull()){

                }
            }
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
}