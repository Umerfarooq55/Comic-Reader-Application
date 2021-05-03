package com.example.shortcut.ui.fragment.favourites.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.shortcut.interactor.BaseRxInteractor
import com.example.shortcut.interactor.ToggleFavouriteInteractor
import com.example.shortcut.model.AppDialog
import com.example.shortcut.ui.config.PagesConfigProvider
import com.example.shortcut.ui.fragment.favourites.interactor.LoadFavouritePagesInteractor
import com.example.shortcut.ui.fragment.favourites.model.FavouritesAction
import com.example.shortcut.ui.model.UiComicStrip


class FavouritesViewModel(
        private val loadFavouritePagesInteractor: LoadFavouritePagesInteractor,
        private val toggleFavouriteInteractor: ToggleFavouriteInteractor,
        private val pagesConfigProvider: PagesConfigProvider
) : ViewModel() {

    private val interactors = listOf(
        loadFavouritePagesInteractor, toggleFavouriteInteractor
    )

    val actionLiveData = MutableLiveData<FavouritesAction>().apply { value = FavouritesAction.Idle }

    val pagedList by lazy {
        LivePagedListBuilder(
            loadFavouritePagesInteractor.getPages(),
            pagesConfigProvider.pagesCount
        )
            .setBoundaryCallback(object : PagedList.BoundaryCallback<UiComicStrip>() {
            })
            .build()
    }

    fun toggleFavourite(comicStripId: Int, isFavourite: Boolean) {
        toggleFavouriteInteractor.toggleFavourite(comicStripId, isFavourite)
    }

    fun showAboutDialog() {
        actionLiveData.postValue(FavouritesAction.ShowDialog(AppDialog.About))
    }

    override fun onCleared() {
        interactors.forEach(BaseRxInteractor::clear)
        super.onCleared()
    }
}
