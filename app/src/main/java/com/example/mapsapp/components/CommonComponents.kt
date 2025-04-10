package com.example.mapsapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.navigation.BottomNavigationBar
import com.example.mapsapp.navigation.User
import com.example.mapsapp.ui.theme.MapsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(title: String, navController: NavController) {
    TopAppBar(
        title = { Text(title,color = MaterialTheme.colorScheme.onPrimary, fontWeight = Bold) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = { navController.navigate(User) }) {
                Icon(Icons.Default.Person, contentDescription = "User navigation from header",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(40.dp))
            }
        }
    )
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
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary)
    ) {
        Text(text = text, fontSize = 24.sp, color = MaterialTheme.colorScheme.primary, fontWeight = Bold)
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
    MapsAppTheme {
        Column {
            val fakeNavController = rememberNavController()
            AppHeader("PICOTRAKE", fakeNavController)
            PrimaryButton("Button", Modifier.weight(1f), { 2 + 2 })
            TextViewButtonStyle("Mamaguevo")
            BottomNavigationBar(fakeNavController)
        }
    }
}

@Composable
fun LoadImageWithGlide(url: String) {

    //    GlideImage(
    //        model = url,
    //        contentDescription = "Image from URL"
    //    )
}