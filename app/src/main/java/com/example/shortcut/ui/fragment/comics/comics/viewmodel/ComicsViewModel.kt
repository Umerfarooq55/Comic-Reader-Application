package com.example.shortcut.ui.fragment.comics.comics.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.shortcut.interactor.BaseRxInteractor
import com.example.shortcut.interactor.LoadComicPagesInteractor
import com.example.shortcut.interactor.LoadSpecificComicInteractor
import com.example.shortcut.interactor.ToggleFavouriteInteractor
import com.example.shortcut.model.AppDialog
import com.example.shortcut.ui.config.PagesConfigProvider
import com.example.shortcut.ui.error.UiError
import com.example.shortcut.ui.fragment.comics.model.ComicAction
import com.example.shortcut.ui.model.UiComicStrip


class ComicsViewModel(
        loadComicPagesInteractor: LoadComicPagesInteractor,
        private val loadSpecificComicInteractor: LoadSpecificComicInteractor,
        private val toggleFavouriteInteractor: ToggleFavouriteInteractor,
        pagesConfigProvider: PagesConfigProvider
) : ViewModel() {
    val pagedItems: LiveData<PagedList<UiComicStrip>>
    val actionLiveData = MutableLiveData<ComicAction>().apply {
        value = ComicAction.Idle }
    val lastLoadedIndex = MutableLiveData<Int?>()

    private val interactors = listOf(
        loadSpecificComicInteractor, toggleFavouriteInteractor
    )

    init {
        class UiComicStripBoundaryCallback : PagedList.BoundaryCallback<UiComicStrip>() {
            override fun onItemAtEndLoaded(itemAtEnd: UiComicStrip) {
                super.onItemAtEndLoaded(itemAtEnd)
                if (itemAtEnd.number != 1) {
                    lastLoadedIndex.postValue(itemAtEnd.number - 1)
                }
            }
        }
        pagedItems = LivePagedListBuilder(loadComicPagesInteractor.getPages(), pagesConfigProvider.pagesCount)
            .setBoundaryCallback(UiComicStripBoundaryCallback())
            .build()
        loadComic(0)
    }

    fun loadComic(comicStripId: Int?) {
        loadSpecificComicInteractor.load(comicStripId ?: 0,
            object : LoadSpecificComicInteractor.Callback {
                override fun onSuccess(uiComicStrip: UiComicStrip) {
                    actionLiveData.postValue(ComicAction.ShowContent)
                }

                override fun onError(uiError: UiError) {
                    actionLiveData.postValue(ComicAction.ShowError(uiError))
                }
            })
    }

    fun refresh() {
        loadComic(0)
    }

    fun showAboutDialog() {
        actionLiveData.postValue(ComicAction.ShowDialog(AppDialog.About))
    }

    fun clearError() {
        actionLiveData.postValue(ComicAction.Idle)
    }

    fun toggleFavourite(comicStripId: Int, isFavourite: Boolean) {
        toggleFavouriteInteractor.toggleFavourite(comicStripId, isFavourite)
    }

    override fun onCleared() {
        interactors.forEach(BaseRxInteractor::clear)
        super.onCleared()
    }
}
