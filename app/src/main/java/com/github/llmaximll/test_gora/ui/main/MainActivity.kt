package com.github.llmaximll.test_gora.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.github.llmaximll.test_gora.navigation.TestGoraNavHost
import com.github.llmaximll.test_gora.ui.theme.Test_GoraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Test_GoraTheme {
                AppContent()
            }
        }
    }

    @Composable
    private fun AppContent(
        modifier: Modifier = Modifier
    ) {
        val navController = rememberNavController()

        Scaffold(
            modifier = modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { padding ->
            TestGoraNavHost(
                modifier = Modifier.padding(padding),
                navController = navController
            )
        }
    }

    @Preview
    @Composable
    private fun AppContentPreview() {
        Test_GoraTheme {
            AppContent()
        }
    }
}