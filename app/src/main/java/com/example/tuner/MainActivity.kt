package com.example.tuner


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.tuner.ui.TunerScreen
import com.example.tuner.ui.theme.TunerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissions = arrayOf(
            "android.permission.RECORD_AUDIO",
        )
        val requestCode = 200
        requestPermissions(permissions, requestCode)
        setContent {
            TunerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TunerScreen(
                        context = applicationContext
                    )
                }
            }
        }
    }
}

