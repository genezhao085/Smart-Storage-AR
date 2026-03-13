package com.smartstoragear.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    val demoMode = remember { mutableStateOf(true) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings")
        Text("Demo Mode")
        Switch(checked = demoMode.value, onCheckedChange = { demoMode.value = it })
    }
}
