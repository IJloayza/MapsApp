package com.example.mapsapp.ui.theme
import androidx.compose.ui.graphics.Color

// --- COLORES PARA TEMA CLARO ---
val LightPrimary = Color(0xFF00A1A9) // Tu color primario
val LightOnPrimary = Color.White // Color del texto/iconos sobre el primario
val LightPrimaryContainer = Color(0xFF70F5FF) // Un contenedor más claro derivado del primario
val LightOnPrimaryContainer = Color(0xFF002022) // Texto/iconos sobre primaryContainer

val LightBackground = Color(0xFFF4F9FE) // Tu color de fondo
val LightOnBackground = Color(0xFF171C1D) // Color del texto/iconos sobre el fondo

val LightSurface = Color(0xC3FFFFFF) // Superficies como Cards, Sheets
val LightOnSurface = Color(0xFF171C1D) // Texto/iconos sobre superficies

// Para botones blancos en tema claro
val LightButtonContainer = Color.White
val LightButtonContent = LightPrimary // El texto del botón será de color primario

// Otros colores estándar de Material 3 para completar el esquema
val LightSecondary = Color(0xFF4A6364) // Color secundario (ajusta según tu marca)
val LightOnSecondary = Color.White
val LightSecondaryContainer = Color(0xFFCCE7E9)
val LightOnSecondaryContainer = Color(0xFF051F21)

val LightTertiary = Color(0xFF535E7D) // Color terciario (ajusta según tu marca)
val LightOnTertiary = Color.White
val LightTertiaryContainer = Color(0xFFDAE2FF)
val LightOnTertiaryContainer = Color(0xFF0E1A37)

val LightError = Color(0xFFBA1A1A)
val LightOnError = Color.White
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnErrorContainer = Color(0xFF410002)

val LightSurfaceVariant = Color(0xFFDAE4E5) // Para elementos como chips, menús
val LightOnSurfaceVariant = Color(0xFF3F4849)
val LightOutline = Color(0xFF6F797A) // Bordes


// --- COLORES PARA TEMA OSCURO (Sugeridos) ---
// Para el tema oscuro, he elegido colores que contrastan bien y son agradables a la vista.
// El primario es una variante más clara de tu primario claro.
// El fondo es un gris oscuro para reducir la fatiga visual.

val DarkPrimary = Color(0xDA4DC7E5) // Primario más brillante para destacar en fondos oscuros
val DarkOnPrimary = Color(0xFF00373A) // Texto/iconos sobre el primario oscuro
val DarkPrimaryContainer = Color(0xFF004F53)
val DarkOnPrimaryContainer = Color(0xFF70F5FF)

val DarkBackground = Color(0xFF1E2528) // Fondo gris oscuro (ligeramente azulado)
val DarkOnBackground = Color(0xFFE0E3E5) // Texto/iconos claros sobre el fondo oscuro

val DarkSurface = Color(0xFF1E2528) // Superficies (puede ser igual al fondo oscuro)
val DarkOnSurface = Color(0xFFE0E3E5) // Texto/iconos sobre superficies oscuras

// Para botones en tema oscuro:
// Opción 1: Botones blancos (podrían ser muy brillantes, pero si es un requisito)
val DarkButtonContainerWhite = Color.White
val DarkButtonContentWhite = DarkPrimary // Texto del botón con el primario oscuro

// Opción 2: Botones con color primario oscuro (más convencional)
val DarkButtonContainerThemed = DarkPrimary
val DarkButtonContentThemed = DarkOnPrimary


// Otros colores estándar de Material 3 para el tema oscuro
val DarkSecondary = Color(0xFFB0CAD0) // Secundario para tema oscuro
val DarkOnSecondary = Color(0xFF1C343A)
val DarkSecondaryContainer = Color(0xFF334A51)
val DarkOnSecondaryContainer = Color(0xFFCCE7ED)

val DarkTertiary = Color(0xFFBAC6EA) // Terciario para tema oscuro
val DarkOnTertiary = Color(0xFF25304D)
val DarkTertiaryContainer = Color(0xFF3C4664)
val DarkOnTertiaryContainer = Color(0xFFDAE2FF)

val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)

val DarkSurfaceVariant = Color(0xFF3F4849)
val DarkOnSurfaceVariant = Color(0xFFBEC8C9)
val DarkOutline = Color(0xFF899293)