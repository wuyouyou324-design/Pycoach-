package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFF7DCFFF),
  onPrimary = Color(0xFF00344A),
  primaryContainer = Color(0xFF004D6A),
  onPrimaryContainer = Color(0xFFC4E7FF),
  secondary = Color(0xFFB2CADB),
  onSecondary = Color(0xFF1D3342),
  secondaryContainer = Color(0xFF334959),
  onSecondaryContainer = Color(0xFFCEE5F8),
  background = Color(0xFF191C1E),
  onBackground = Color(0xFFE2E2E5),
  surface = Color(0xFF191C1E),
  onSurface = Color(0xFFE2E2E5),
  surfaceVariant = Color(0xFF41484D),
  onSurfaceVariant = Color(0xFFC1C7CE),
  outline = Color(0xFF8B9297),
)

private val LightColorScheme = lightColorScheme(
  primary = Color(0xFF00668B),
  onPrimary = Color(0xFFFFFFFF),
  primaryContainer = Color(0xFFC4E7FF),
  onPrimaryContainer = Color(0xFF001E2F),
  secondary = Color(0xFF4B6171),
  onSecondary = Color(0xFFFFFFFF),
  secondaryContainer = Color(0xFFCEE5F8),
  onSecondaryContainer = Color(0xFF071E2B),
  background = Color(0xFFF8F9FC),
  onBackground = Color(0xFF191C1E),
  surface = Color(0xFFF8F9FC),
  onSurface = Color(0xFF191C1E),
  surfaceVariant = Color(0xFFDDE3EA),
  onSurfaceVariant = Color(0xFF41484D),
  outline = Color(0xFF71787E),
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+, enabled by default to use device wallpaper colors
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
