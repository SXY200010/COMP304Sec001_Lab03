//Xiaoyu Shi and Angelica Cuadrado - Lab 3 assignment

package com.example.comp304sec001_lab03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp304sec001_lab03.ViewModels.MovieViewModel
import com.example.comp304sec001_lab03.ViewModels.MovieViewModelFactory
import com.example.comp304sec001_lab03.ui.navigation.AppNavigation

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(application))
            val windowSizeClass = calculateWindowSizeClass(this)

            Surface(color = MaterialTheme.colorScheme.background) {
                AppNavigation(
                    navController = navController,
                    viewModel = viewModel,
                    windowSizeClass = windowSizeClass
                )
            }
        }
    }
}
