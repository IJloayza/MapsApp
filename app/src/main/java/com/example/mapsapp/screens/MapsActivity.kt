package com.example.mapsapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mapsapp.navigation.Home
import com.example.mapsapp.retrofit.MapViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun MapsScreen(navController: NavController) {
    // Utilizar un scaffold para colocar el header de navegación y en el cuerpo
    // Colocar el mapa con unos botones emergentes de soporte de navegación
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
        }
    }

    val viewModel: MapViewModel = viewModel()
    Column(modifier = Modifier.fillMaxSize()) {

        // Buscador
        searchField(viewModel)

        if (viewModel.suggestions.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .padding(horizontal = 8.dp)
            ) {
                viewModel.suggestions.take(5).forEach { lugar ->
                    Text(
                        text = lugar.display_name ?: "No name",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val geoPoint = GeoPoint(lugar.lat.toDouble(), lugar.lon.toDouble())
                                mapView.controller.setCenter(geoPoint)
                                mapView.controller.setZoom(15.0)
                                viewModel.onSearchTextChange(lugar.display_name ?: "")
                                viewModel.cleanSugerencies()
                            }
                            .padding(8.dp)
                    )
                }
            }
        }

        // Mapa
        AndroidView(factory = { mapView }, modifier = Modifier.weight(1f))

        // Botones
        buttonsMap(navController, mapView)
    }
}

@Composable
fun searchField(viewModel: MapViewModel){
    OutlinedTextField(
        value = viewModel.searchText,
        onValueChange = { viewModel.onSearchTextChange(it) },
        label = { Text("Buscar lugar...") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .padding(8.dp)
    )
}

@Composable
fun buttonsMap(navController: NavController, mapView: MapView){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { navController.navigate(Home) }) { Text("Inicio") }
        Button(onClick = { mapView.controller.zoomIn() }) { Text("+") }
        Button(onClick = { mapView.controller.zoomOut() }) { Text("-") }
    }
}
//@Composable
//fun OsmdroidMapView() {
//    val context = LocalContext.current
//    AndroidView(
//        modifier = Modifier.fillMaxSize(),
//        factory = { context ->
//            val mapView = MapView(context)
//            mapView.setTileSource(TileSourceFactory.MAPNIK)
//            mapView.setBuiltInZoomControls(true)
//            mapView.setMultiTouchControls(true)
//            mapView
//        }
//    )
//}