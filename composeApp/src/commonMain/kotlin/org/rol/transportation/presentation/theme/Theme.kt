package org.rol.transportation.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = YellowPrimary,
    onPrimary = DarkText,

    // Background gris muy claro para que las Cards blancas resalten
    background = LightGrayBg,
    onBackground = DarkText,

    // Cards blancas en modo claro
    surface = Color.White,
    onSurface = DarkText,

    // Contenedores secundarios (Cabeceras) en Negro para máximo contraste
    secondaryContainer = HeaderBlackLight,
    onSecondaryContainer = YellowPrimary,

    // Slot para elementos grisáceos o bordes
    surfaceVariant = HeaderGrayLight,
    onSurfaceVariant = GrayText,

    outline = CardBorder,
    outlineVariant = GrayPlaceholder
)

    private val DarkColorScheme = darkColorScheme(
    primary = YellowPrimary,
    onPrimary = DarkText,

    // Fondo Negro Puro
    background = DarkBg,
    onBackground = Color.White,

    // Cards gris oscuro (CardGrayList)
    surface = CardGrayList,
    onSurface = Color.White,

    // Cabeceras de cards gris intermedio (HeaderGrayDark)
    secondaryContainer = HeaderGrayDark,
    onSecondaryContainer = YellowPrimary,

    // Elementos de detalle
    surfaceVariant = CardGrayDetail,
    onSurfaceVariant = GrayText,

    outline = Color(0xFF323234),
    outlineVariant = Color(0xFF323234)
)


/*@Composable
fun TransportationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}*/
@Composable
fun TransportationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val cardButtonsColor = if (darkTheme) DarkCustomColors else LightCustomColors

    CompositionLocalProvider(LocalTransportationColors provides cardButtonsColor) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object TransportationTheme {
    val myColors: TransportationColors
        @Composable
        get() = LocalTransportationColors.current
}


@Immutable
data class TransportationColors(
    val bgDeparture: Color = Color.Unspecified,
    val textDeparture: Color = Color.Unspecified,
    val bgArrival: Color = Color.Unspecified,
    val textArrival: Color = Color.Unspecified
)

val LocalTransportationColors = staticCompositionLocalOf { TransportationColors() }

private val LightCustomColors = TransportationColors(
    bgDeparture = ActionBlueBgLight,
    textDeparture = ActionBlueTextLight,
    bgArrival = ActionGreenBgLight,
    textArrival = ActionGreenTextLight
)

private val DarkCustomColors = TransportationColors(
    bgDeparture = ActionBlueBgDark,
    textDeparture = ActionBlueTextDark,
    bgArrival = ActionGreenBgDark,
    textArrival = ActionGreenTextDark
)