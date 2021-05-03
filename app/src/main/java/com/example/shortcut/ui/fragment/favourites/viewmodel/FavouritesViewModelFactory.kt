package com.example.shortcut.ui.fragment.favourites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shortcut.interactor.ToggleFavouriteInteractor
import com.example.shortcut.ui.config.PagesConfigProvider
import com.example.shortcut.ui.fragment.favourites.interactor.LoadFavouritePagesInteractor

import javax.inject.Inject

internal class FavouritesViewModelFactory @Inject constructor(
        private val loadFavouritePagesInteractor: LoadFavouritePagesInteractor,
        private val toggleFavouriteInteractor: ToggleFavouriteInteractor,
        private val pagesConfigProvider: PagesConfigProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FavouritesViewModel(
            loadFavouritePagesInteractor,
            toggleFavouriteInteractor,
            pagesConfigProvider
        ) as T
    }
}
