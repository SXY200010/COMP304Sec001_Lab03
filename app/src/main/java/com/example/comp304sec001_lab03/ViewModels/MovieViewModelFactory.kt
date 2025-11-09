//Xiaoyu Shi and Angelica Cuadrado - Lab 3 assignment

package com.example.comp304sec001_lab03.ViewModels

//- A helper that creates the ViewModel with the extra parameters it needs
// (like the Application or repository). Without the factory, Android Studio
// wouldn’t know how to pass those dependencies into the ViewModel.

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieViewModelFactory(private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(application) as T
    }
}