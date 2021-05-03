package com.example.shortcut.ui.fragment.comics.cards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shortcut.interactor.*
import com.example.shortcut.ui.config.PagesConfigProvider
import com.example.shortcut.ui.fragment.comics.cards.viewmodel.CardsViewModel
import com.michaelfotiads.xkcdreader.data.prefs.UserDataStore

import javax.inject.Inject

class CardsViewModelFactory @Inject constructor(
        private val loadCardPagesInteractor: LoadCardPagesInteractor,
        private val loadSpecificComicInteractor: LoadSpecificComicInteractor,
        private val toggleFavouriteInteractor: ToggleFavouriteInteractor,
        private val resetCardPagesInteractor: ResetCardPagesInteractor,
        private val searchProcessInteractor: SearchProcessInteractor,
        private val pagesConfigProvider: PagesConfigProvider,
        private val dataStore: UserDataStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CardsViewModel(
            loadCardPagesInteractor,
            loadSpecificComicInteractor,
            toggleFavouriteInteractor,
            resetCardPagesInteractor,
            searchProcessInteractor,
            pagesConfigProvider,
            dataStore) as T
    }
}
