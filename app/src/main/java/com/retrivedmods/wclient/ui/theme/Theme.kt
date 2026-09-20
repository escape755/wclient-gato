package com.retrivedmods.wclient.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


object WColors {
    // Primary reds
    val Primary = Color(0xFFFF5CA6)
    val PrimaryLight = Color(0xFFFF8FC5)
    val PrimaryDark = Color(0xFFD6407F)
    val OnPrimary = Color(0xFFFFFFFF)

    // Secondary for contrast (cool slate)
    val Secondary = Color(0xFFB88098)
    val SecondaryVariant = Color(0xFF8C6478)
    val SecondaryLight = Color(0xFFD9AFC2)
    val OnSecondary = Color(0xFFFFFFFF)

    // Accent red/pink for glow effects
    val Accent = Color(0xFFFF7CB8)
    val AccentLight = Color(0xFFFFB3D9)
    val AccentDark = Color(0xFFC23573)


    val Background = Color(0xFFFFFFFF)
    val Surface = Color(0xFFFFF3F8)
    val SurfaceVariant = Color(0xFFFFE6F0)
    val SurfaceContainer = Color(0xFFFFEAF3)

    val OnBackground = Color(0xFF4A2438)
    val OnSurface = Color(0xFF4A2438)
    val OnSurfaceVariant = Color(0xFF9C7288)

    val Error = Color(0xFFFF4D4D)
    val ErrorLight = Color(0xFFFF8080)

    val Border = Color(0xFFFFC2DD)
    val BorderLight = Color(0xFFFF8FC0)

    val Overlay = Color(0x80000000)


    val MinimapBackground = Color(0xCC000000)
    val MinimapGrid = Color(0x66A9A9A9)
    val MinimapCrosshair = Color(0x80808080)
    val MinimapPlayerMarker = Color(0xFFFFFFFF)
    val MinimapNorth = Color(0xFFFF5CA6)   // match primary red
    val MinimapEntityClose = Color(0xFFFF3B3B)
    val MinimapEntityFar = Color(0xFFFFD166)
    val MinimapZoom = 1.0f
    val MinimapDotSize = 5
}

object ClickGUIColors {
    val PrimaryBackground = Color(0xFFFFFFFF)
    val SecondaryBackground = Color(0xFFFFF3F8)

    val AccentColor = WColors.Primary
    val AccentColorVariant = WColors.AccentLight

    val PrimaryText = Color(0xFF4A2438)
    val SecondaryText = Color(0xFF9C7288)

    val PanelBackground = Color(0xF0FFF3F8)
    val PanelBorder = Color(0x60FF5CA6)

    val ModuleEnabled = AccentColor
    val ModuleDisabled = Color(0xFFFFE0EE)

    val SliderTrack = Color(0xFFFFC2DD)
    val SliderThumb = AccentColor
    val SliderFill = AccentColor

    val CheckboxBorder = AccentColor
    val CheckboxFill = AccentColor
}

private val WDarkColorScheme = darkColorScheme(
    primary = WColors.Primary,
    onPrimary = WColors.OnPrimary,
    primaryContainer = WColors.PrimaryDark,
    onPrimaryContainer = WColors.PrimaryLight,

    secondary = WColors.Secondary,
    onSecondary = WColors.OnSecondary,
    secondaryContainer = WColors.SecondaryVariant,
    onSecondaryContainer = WColors.SecondaryLight,

    tertiary = WColors.Accent,
    onTertiary = Color.White,
    tertiaryContainer = WColors.AccentDark.copy(alpha = 0.25f),
    onTertiaryContainer = WColors.AccentLight,

    background = WColors.Background,
    onBackground = WColors.OnBackground,
    surface = WColors.Surface,
    onSurface = WColors.OnSurface,
    surfaceVariant = WColors.SurfaceVariant,
    onSurfaceVariant = WColors.OnSurfaceVariant,
    surfaceContainer = WColors.SurfaceContainer,

    error = WColors.Error,
    onError = Color.White,
    errorContainer = WColors.Error.copy(alpha = 0.22f),
    onErrorContainer = WColors.ErrorLight,

    outline = WColors.Border,
    outlineVariant = WColors.BorderLight.copy(alpha = 0.55f),

    scrim = WColors.Overlay,
    inverseSurface = WColors.OnSurface,
    inverseOnSurface = WColors.Surface,
    inversePrimary = WColors.PrimaryDark
)

private val WLightColorScheme = lightColorScheme(
    primary = WColors.Primary,
    onPrimary = WColors.OnPrimary,
    primaryContainer = WColors.Primary.copy(alpha = 0.12f),
    onPrimaryContainer = WColors.Primary,

    secondary = WColors.Secondary,
    onSecondary = WColors.OnSecondary,
    secondaryContainer = WColors.Secondary.copy(alpha = 0.12f),
    onSecondaryContainer = WColors.Secondary,

    tertiary = WColors.Accent,
    onTertiary = WColors.OnPrimary,
    tertiaryContainer = WColors.Accent.copy(alpha = 0.12f),
    onTertiaryContainer = WColors.Accent,

    background = Color(0xFFFFF3F8),
    onBackground = Color(0xFF4A2438),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF4A2438),
    surfaceVariant = Color(0xFFFFE6F0),
    onSurfaceVariant = Color(0xFF9C7288),
    surfaceContainer = Color(0xFFFFEAF3),

    error = WColors.Error,
    onError = WColors.OnPrimary,
    outline = Color(0xFFFFC2DD),
    outlineVariant = Color(0xFFFFE0EE)
)


val WTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp
    )
)

@Composable
fun WClientTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> WDarkColorScheme
        else -> WLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = WTypography,
        content = content
    )
}