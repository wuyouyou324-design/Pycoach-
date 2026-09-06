package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.screens.MainAppScreen
import com.example.ui.viewmodel.LearningViewModel
import com.example.ui.viewmodel.ThemeMode

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val viewModel: LearningViewModel = viewModel()

      val isDark = when (viewModel.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
      }

      val currentDensity = LocalDensity.current
      val fontScaleFactor = viewModel.fontSizeOption.scale
      val customDensity = Density(
        density = currentDensity.density,
        fontScale = currentDensity.fontScale * fontScaleFactor
      )

      CompositionLocalProvider(LocalDensity provides customDensity) {
        MyApplicationTheme(
          darkTheme = isDark,
          dynamicColor = viewModel.dynamicColorEnabled
        ) {
          Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainAppScreen(
              viewModel = viewModel,
              modifier = Modifier.padding(innerPadding)
            )
          }
        }
      }
    }
  }
}
