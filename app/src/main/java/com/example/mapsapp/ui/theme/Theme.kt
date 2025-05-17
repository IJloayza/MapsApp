package com.example.mapsapp.ui.theme
import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.ui.theme.AppTypography

private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    inverseOnSurface = Color(0xFFEFF1F2), // Mantenido de tu código
    inverseSurface = Color(0xFF2C3132),   // Mantenido de tu código
    inversePrimary = Color(0xFF4DDDE5),   // Mantenido de tu código
    scrim = Color.Black,                  // Mantenido de tu código
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    inverseOnSurface = Color(0xFF171C1D), // Mantenido de tu código
    inverseSurface = Color(0xFFE0E3E5),   // Mantenido de tu código
    inversePrimary = Color(0xFF00686E),   // Mantenido de tu código
    scrim = Color.Black,                  // Mantenido de tu código
)

@Immutable
data class ColorFamily( // Esta estructura no se usa actualmente en MapsAppTheme, pero está bien tenerla.
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

// Asumo que tienes un archivo Typography.kt con AppTypography definido, por ejemplo:
// val AppTypography = Typography(...)

@Composable
fun MapsAppTheme(
    darkTheme: Boolean, // Ya no se usa isSystemInDarkTheme() directamente aquí, se pasa desde el ViewModel
    dynamicColor: Boolean = false, // Has puesto true por defecto, lo cambio a false para que use tus colores custom a menos que se active.
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    //Bloque faltante para cambio de tema necesaria ayuda externa porque sino fue imposible
    val view = LocalView.current
    if (!view.isInEditMode) {
        // ESTE BLOQUE ES CRUCIAL para que los colores de la barra de estado y navegación
        // se actualicen según el tema de la aplicación.
        SideEffect {
            val window = (view.context as Activity).window
            // Establece el color de la barra de estado.
            window.statusBarColor = colorScheme.background.toArgb()

            // Controla si los iconos de la barra de estado son claros u oscuros.
            // Si el tema es oscuro, los iconos deberían ser claros (isAppearanceLightStatusBars = false).
            // Si el tema es claro, los iconos deberían ser oscuros (isAppearanceLightStatusBars = true).
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Asegúrate de que AppTypography esté definido
        // shapes = AppShapes, // Si tienes Shapes.kt y quieres usarlo
        content = content
    )
}
