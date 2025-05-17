package com.example.mapsapp.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mapsapp.navigation.Home
import com.example.mapsapp.retrofit.models.MapViewModel
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import com.example.mapsapp.R
import com.example.mapsapp.retrofit.models.Mountain
import com.example.mapsapp.retrofit.models.MountainViewModel
import com.google.android.gms.location.LocationServices
import org.osmdroid.views.CustomZoomButtonsController

@Composable
fun MapsScreen(navController: NavController) {
    // Utilizar un scaffold para colocar el header de navegación y en el cuerpo
    // Colocar el mapa con unos botones emergentes de soporte de navegación
    val context = LocalContext.current
    Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
    // Importante para no ser baneado de OSM
    Configuration.getInstance().userAgentValue = context.packageName

    val mountainViewModel: MountainViewModel = viewModel()
    val mountains by mountainViewModel.mountains

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    //Los valores predeterminados del mapa son cargados con remember
    //Para evitar volver a construirlo innecesariamente urante ejecución
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(9.0)
            controller.setCenter(GeoPoint(41.8, 2.2))

            minZoomLevel = 5.0
            maxZoomLevel = 18.0
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        }
    }

    var selectedMountain by remember { mutableStateOf<Mountain?>(null) }
    var markerDestination: Marker? by remember { mutableStateOf(null) }

    LaunchedEffect(mountains) {
        mountains.forEach { mountain ->
            mountain.geojson?.let { jsonElement ->
                try {
                    val geoJsonObject = JSONObject(jsonElement.toString())
                    parseMountainGeoJsonAndAddToMap(
                        mapView,
                        geoJsonObject,
                        mountain,
                        onPolygonClick = { selectedMountain = mountain }
                    )
                } catch (e: Exception) {
                    Log.e("MountainGeoJSON", "Error parsing mountain geojson", e)
                }
            }
        }
    }

    val viewModel: MapViewModel = viewModel()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

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

                                // Eliminar el marcador anterior si existe
                                mapView.overlays.remove(markerDestination)

                                // Crear un nuevo marcador en la ubicación de destino
                                val defaultMarker = ResourcesCompat.getDrawable(
                                    context.resources,
                                    org.osmdroid.library.R.drawable.marker_default,
                                    null
                                ) as BitmapDrawable
                                //Se puede cambiar color de marker con:
                                //.setTint
                                val newMarker = Marker(mapView)
                                newMarker.position = geoPoint
                                newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                newMarker.title = lugar.display_name
                                newMarker.icon = defaultMarker
                                mapView.overlays.add(newMarker)

                                // Guardar la referencia al nuevo marcador
                                markerDestination = newMarker

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
        // Mostrar un Alert Dialog al picar en un area con poligono
        if (selectedMountain != null) {
            AlertDialog(
                onDismissRequest = { selectedMountain = null },
                confirmButton = {
                    Button(onClick = { selectedMountain = null }) {
                        Text(stringResource(R.string.close))
                    }
                },
                //Debo inflar aqui los datos de la montaña o parque natural recuperado
                title = { Text(text = selectedMountain?.nombre_montanya ?: stringResource(R.string.mountainName)) },
                text = {
                    Box(
                        modifier = Modifier
                            .heightIn(max = 300.dp) // Limita la altura máxima
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            //Obliga a comprobar si la montaña seleccionada existe antes de usar los valores
                            selectedMountain?.let { mountain ->
                                mountain.descripcion?.let {
                                    Text(text = stringResource(R.string.mountainDescription) + ": $it")
                                }
                                mountain.dificultad?.let {
                                    Text(text = stringResource(R.string.mountainDificulty) + ": $it")
                                }
                                Text(text = stringResource(R.string.campingAllowed) + ": ${if (mountain.acampar) stringResource(R.string.afirmation) else stringResource(R.string.negation)}")
                                Text(text = stringResource(R.string.sleepingAllowed) + ": ${if (mountain.pernoctar) stringResource(R.string.afirmation) else stringResource(R.string.negation)}")
                                Text(text = stringResource(R.string.dangerousSpecies) + ": ${if (mountain.especies_peligrosas) stringResource(R.string.afirmation) else stringResource(R.string.negation)}")
                            }
                        }
                    }
                }
            )
        }
        // Mapa
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clipToBounds()
        ) {
            AndroidView(factory = { mapView })
        }

        // Botones
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(50.dp),

        ) {
            FloatingActionButton(
                onClick = { mapView.controller.zoomIn() },
                containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, contentDescription = "Zoom in")
            }
            FloatingActionButton(
                onClick = { mapView.controller.zoomOut() },
                containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom out")
            }
            FloatingActionButton(
                onClick = { navController.navigate(Home){ popUpTo(Home) } },
                containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Home, contentDescription = "Go to Home")
            }
            FloatingActionButton(onClick = {
                // Pedir ubicación actual
                if (ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            val userLocation = GeoPoint(it.latitude, it.longitude)
                            mapView.controller.animateTo(userLocation)
                        }
                    }
                } else {
                    Toast.makeText(context, "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show()
                }
            },
                containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.MyLocation, contentDescription = "Go to Current Location")
            }
        }
    }
}

//Programar un debounce para esperar unos milis antes de empezar la busqueda hasta que el usuario haya acabado de escribir
@Composable
fun searchField(viewModel: MapViewModel){
    val searchText by viewModel.searchText.collectAsState()

    OutlinedTextField(
        value = searchText,
        onValueChange = { viewModel.onSearchTextChange(it) },
        label = { Text(stringResource(R.string.search)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .padding(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            cursorColor = MaterialTheme.colorScheme.onPrimary,
            focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary
        ),
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { viewModel.onSearchTextChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Boton para borrar el contenido",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )

}

fun parseMountainGeoJsonAndAddToMap(
    mapView: MapView,
    geoJson: JSONObject,
    mountain: Mountain,
    onPolygonClick: () -> Unit
) {
    if (geoJson.getString("type") != "FeatureCollection") {
        Log.e("MountainGeoJSON", "Invalid format: not a FeatureCollection")
        return
    }

    val color = mountainColor(mountain)

    val features = geoJson.getJSONArray("features")
    for (i in 0 until features.length()) {
        val feature = features.getJSONObject(i)
        val geometry = feature.getJSONObject("geometry")
        val geometryType = geometry.getString("type")

        when (geometryType) {
            "Polygon" -> {
                val polygon = parsePolygonWithColor(
                    geometry,
                    feature,
                    mapView.context,
                    mountain.nombre_montanya ?: "Desconocido",
                    color,
                    onPolygonClick
                )
                mapView.overlays.add(polygon)
            }
            "MultiPolygon" -> {
                parseMultiPolygonWithColor(
                    geometry,
                    feature,
                    mapView,
                    mountain.nombre_montanya ?: "Desconocido",
                    color,
                    onPolygonClick
                )
            }
            "LineString", "MultiLineString" -> {
                val polyline = parseLineGeometry(geometry, color)
                mapView.overlays.add(polyline)
            }
        }
    }

    mapView.invalidate()
}

fun parseLineGeometry(geometry: JSONObject, color: Int): Polyline {
    val polyline = Polyline()
    val geoPoints = mutableListOf<GeoPoint>()

    when (geometry.getString("type")) {
        "LineString" -> {
            val coordinates = geometry.getJSONArray("coordinates")
            for (j in 0 until coordinates.length()) {
                val point = coordinates.getJSONArray(j)
                geoPoints.add(GeoPoint(point.getDouble(1), point.getDouble(0)))
            }
        }
        "MultiLineString" -> {
            val multi = geometry.getJSONArray("coordinates")
            for (i in 0 until multi.length()) {
                val segment = multi.getJSONArray(i)
                for (j in 0 until segment.length()) {
                    val point = segment.getJSONArray(j)
                    geoPoints.add(GeoPoint(point.getDouble(1), point.getDouble(0)))
                }
            }
        }
    }

    polyline.setPoints(geoPoints)
    polyline.color = color
    polyline.width = 6f
    return polyline
}

fun parsePolygonWithColor(
    geometry: JSONObject,
    feature: JSONObject,
    context: Context,
    name: String,
    color: Int,
    onPolygonClick: () -> Unit
): Polygon {
    val coordinates = geometry.getJSONArray("coordinates").getJSONArray(0)
    val geoPoints = mutableListOf<GeoPoint>()
    for (i in 0 until coordinates.length()) {
        val point = coordinates.getJSONArray(i)
        geoPoints.add(GeoPoint(point.getDouble(1), point.getDouble(0)))
    }

    val polygon = Polygon()
    polygon.setPoints(geoPoints)
    polygon.fillColor = ColorUtils.setAlphaComponent(color, 100)
    polygon.strokeColor = color
    polygon.strokeWidth = 4f

    polygon.setOnClickListener { _, _, _ ->
        onPolygonClick()
        true
    }

    return polygon
}


fun parseMultiPolygonWithColor(
    geometry: JSONObject,
    feature: JSONObject,
    mapView: MapView,
    name: String,
    color: Int,
    onPolygonClick: () -> Unit
) {
    val coordinates = geometry.getJSONArray("coordinates")
    for (i in 0 until coordinates.length()) {
        val polygonCoordinates = coordinates.getJSONArray(i).getJSONArray(0)
        val geoPoints = mutableListOf<GeoPoint>()
        for (j in 0 until polygonCoordinates.length()) {
            val point = polygonCoordinates.getJSONArray(j)
            geoPoints.add(GeoPoint(point.getDouble(1), point.getDouble(0)))
        }

        val polygon = Polygon()
        polygon.setPoints(geoPoints)
        polygon.fillColor = ColorUtils.setAlphaComponent(color, 100)
        polygon.strokeColor = color
        polygon.strokeWidth = 4f

        polygon.setOnClickListener { _, _, _ ->
            onPolygonClick()
            true
        }

        mapView.overlays.add(polygon)
    }
}

fun parsePolygon(
    geometry: JSONObject,
    feature: JSONObject,
    context: Context,
    onPolygonClick: (String) -> Unit
): Polygon {
    val coordinates = geometry.getJSONArray("coordinates").getJSONArray(0)
    val geoPoints = mutableListOf<GeoPoint>()

    for (i in 0 until coordinates.length()) {
        val point = coordinates.getJSONArray(i)
        val longitude = point.getDouble(0)
        val latitude = point.getDouble(1)
        geoPoints.add(GeoPoint(latitude, longitude))
    }

    val polygon = Polygon()
    polygon.setPoints(geoPoints)
    polygon.fillColor = android.graphics.Color.argb(100, 0, 0, 255)
    polygon.strokeColor = android.graphics.Color.BLUE
    polygon.strokeWidth = 4f

    val name = feature.optJSONObject("properties")?.optString("name") ?: "Parque desconocido"

    polygon.setOnClickListener { _, _ , _->
        onPolygonClick(name)
        true
    }

    return polygon
}

fun parseMultiPolygon(
    geometry: JSONObject,
    feature: JSONObject,
    mapView: MapView
) {
    val coordinates = geometry.getJSONArray("coordinates")
    val name = feature.optJSONObject("properties")?.optString("name") ?: "Parque desconocido"

    for (i in 0 until coordinates.length()) {
        val polygonCoordinates = coordinates.getJSONArray(i)
        val geoPoints = mutableListOf<GeoPoint>()
        for (j in 0 until polygonCoordinates.getJSONArray(0).length()) {
            val point = polygonCoordinates.getJSONArray(0).getJSONArray(j)
            val longitude = point.getDouble(0)
            val latitude = point.getDouble(1)
            geoPoints.add(GeoPoint(latitude, longitude))
        }

        val polygon = Polygon()
        polygon.setPoints(geoPoints)
        polygon.fillColor = android.graphics.Color.argb(100, 0, 0, 255)
        polygon.strokeColor = android.graphics.Color.BLUE
        polygon.strokeWidth = 4f

        polygon.setOnClickListener { _, _ , _->
            Toast.makeText(mapView.context, name, Toast.LENGTH_SHORT).show()
            true
        }

        mapView.overlays.add(polygon)
    }
}

fun mountainColor(mountain: Mountain): Int {
    return when (mountain.dificultad?.lowercase()) {
        "fácil", "facil", "baja" -> Color.GREEN
        "media", "moderada"-> Color.YELLOW
        "difícil", "dificil", "alta" -> Color.RED
        else -> Color.GRAY
    }
}