package com.smartstoragear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.smartstoragear.navigation.AppNavHost
import com.smartstoragear.ui.theme.SmartStorageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartStorageTheme {
                Surface {
                    AppNavHost()
                }
            }
        }
    }
}
