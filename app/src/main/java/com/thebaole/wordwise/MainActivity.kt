package com.thebaole.wordwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.thebaole.wordwise.ui.navigation.WordWiseApp
import com.thebaole.wordwise.ui.theme.WordwiseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WordwiseTheme {
                WordWiseApp()
            }
        }
    }
}