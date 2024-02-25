package com.github.llmaximll.test_gora.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.llmaximll.core.common.log
import com.github.llmaximll.test_gora.R
import com.github.llmaximll.test_gora.navigation.TestGoraNavHost
import com.github.llmaximll.test_gora.navigation.titleResOrNull
import com.github.llmaximll.test_gora.ui.theme.Test_GoraTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        val backStackEntry = navController.currentBackStackEntryAsState().value
        val destination = backStackEntry?.destination

        LaunchedEffect(destination) {
            log("CurrentDestination: ${destination?.route}")
        }

        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            modifier = modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                AppTopBar(
                    title = stringResource(
                        id = destination.titleResOrNull() ?: R.string.destination_title_home
                    )
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            TestGoraNavHost(
                modifier = Modifier.padding(padding),
                navController = navController,
                onArticleClick = { url ->
                    val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(urlIntent)
                },
                onShowSnackbar = { message ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AppTopBar(
        modifier: Modifier = Modifier,
        title: String
    ) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = title
                )
            }
        )
    }

    @Preview
    @Composable
    private fun AppContentPreview() {
        Test_GoraTheme {
            AppContent()
        }
    }
}