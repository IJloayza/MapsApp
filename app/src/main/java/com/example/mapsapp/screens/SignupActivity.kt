package com.example.mapsapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import com.example.mapsapp.components.SpacerVertical
import com.example.mapsapp.navigation.Login
import com.example.mapsapp.navigation.User
import com.example.mapsapp.retrofit.models.UserCreate
import com.example.mapsapp.retrofit.models.UserViewModel
import com.example.mapsapp.ui.theme.MapsAppTheme
import com.example.mapsapp.ui.theme.ThemeViewModel

@Composable
fun SignUpScreen(navController: NavController){
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val userViewModel: UserViewModel = viewModel()
    val user by userViewModel.user.observeAsState()

    Scaffold (
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppHeader(stringResource(R.string.signup), navController) },      // Barra superior
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

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(10.dp),
                        //.wrapContentSize(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    //Nombre input
                    Text(text = stringResource(R.string.firstname), fontSize = 20.sp, fontWeight = Bold)
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        placeholder = { Text(text = stringResource(R.string.testUserName), color = Color.Gray) },
                    )
                    //Apellido input
                    Text(text = stringResource(R.string.lastname), fontSize = 20.sp, fontWeight = Bold)
                    TextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        placeholder = { Text(text = stringResource(R.string.testUserLastName), color = Color.Gray) },
                    )
                    // Email Input
                    Text(text = stringResource(R.string.email), fontSize = 20.sp, fontWeight = Bold)
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        placeholder = { Text(text = stringResource(R.string.testUserEmail), color = Color.Gray) }
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

                    PrimaryButton("Sign Up", Modifier.height(75.dp).width(250.dp)) {
                        if (name.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && password.length >= 6) {
                            val userCreate = UserCreate(
                                nombre = name,
                                apellido = lastName,
                                email = email,
                                contrasena = password
                            )
                            userViewModel.createUser(userCreate)
                            navController.navigate(Login) {
                                popUpTo(User) { inclusive = true }
                            }
                        } else {
                            Log.e("SignUpScreen", "All the elements must not be empty an the password must be longer tha 6 characters")
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewSignInScreen() {
    val themeViewModel: ThemeViewModel = viewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    MapsAppTheme(darkTheme = isDarkTheme) {
        val fakeNavController = rememberNavController()
        SignUpScreen(fakeNavController)
    }
}