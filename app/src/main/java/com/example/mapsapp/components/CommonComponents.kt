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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapsapp.ui.theme.MapsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(title: String) {
    TopAppBar(
        title = { Text(title,color = MaterialTheme.colorScheme.onPrimary, fontWeight = Bold) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = { /* Acción del botón de usuario */ }) {
                Icon(Icons.Default.Person, contentDescription = "User",
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
            AppHeader("PICOTRAKE")
            PrimaryButton("Button", Modifier.weight(1f), { 2 + 2 })
            TextViewButtonStyle("Mamaguevo")
            BottomNavigationBar()
        }
    }
}

@Composable
fun BottomNavigationBar() {
    // Estado para la opción seleccionada
    var selectedItem by remember { mutableStateOf(0) }

    // Lista de opciones
    val items = listOf("Home", "Map", "Settings", "Saved")
    val icons = listOf(Icons.Default.Home, Icons.Default.Map, Icons.Default.Settings, Icons.Default.Favorite)

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item, tint = MaterialTheme.colorScheme.onPrimary) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}