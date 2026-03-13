package com.smartstoragear.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme()
private val DarkScheme = darkColorScheme()

@Composable
fun SmartStorageTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (false) DarkScheme else LightScheme,
        content = content
    )
}
