package com.example.shortcut.ui.fragment.comics.comics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shortcut.interactor.LoadComicPagesInteractor
import com.example.shortcut.interactor.LoadSpecificComicInteractor
import com.example.shortcut.interactor.ToggleFavouriteInteractor
import com.example.shortcut.ui.config.PagesConfigProvider

import javax.inject.Inject

class ComicsViewModelFactory @Inject constructor(
        private val loadComicPagesInteractor: LoadComicPagesInteractor,
        private val loadSpecificComicInteractor: LoadSpecificComicInteractor,
        private val toggleFavouriteInteractor: ToggleFavouriteInteractor,
        private val pagesConfigProvider: PagesConfigProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ComicsViewModel(
            loadComicPagesInteractor,
            loadSpecificComicInteractor,
            toggleFavouriteInteractor,
            pagesConfigProvider) as T
    }
}
