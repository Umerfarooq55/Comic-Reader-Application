package com.example.shortcut.ui.fragment.comics.cards.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.shortcut.interactor.*
import com.example.shortcut.model.AppDialog
import com.example.shortcut.ui.config.PagesConfigProvider
import com.example.shortcut.ui.error.UiError
import com.example.shortcut.ui.fragment.comics.model.ComicAction
import com.example.shortcut.ui.model.UiComicStrip
import com.michaelfotiads.xkcdreader.data.prefs.UserDataStore



class CardsViewModel(
        loadCardPagesInteractor: LoadCardPagesInteractor,
        private val loadSpecificComicInteractor: LoadSpecificComicInteractor,
        private val toggleFavouriteInteractor: ToggleFavouriteInteractor,
        private val resetPagesInteractor: ResetCardPagesInteractor,
        private val searchProcessInteractor: SearchProcessInteractor,
        pagesConfigProvider: PagesConfigProvider,
        private val dataStore: UserDataStore
) : ViewModel() {
    val pagedItems: LiveData<PagedList<UiComicStrip>>
    val actionLiveData = MutableLiveData<ComicAction>().apply { value = ComicAction.Idle }
    val lastLoadedIndex = MutableLiveData<Int?>()

    private val interactors = listOf(
        loadSpecificComicInteractor, toggleFavouriteInteractor, resetPagesInteractor
    )
    private var searchQueryId = 0
    private var currentItem: UiComicStrip? = null

    init {
        resetPagesInteractor.resetData()
        class UiComicStripBoundaryCallback : PagedList.BoundaryCallback<UiComicStrip>() {
            override fun onZeroItemsLoaded() {
                super.onZeroItemsLoaded()
                lastLoadedIndex.postValue(searchQueryId)
                searchQueryId = 0
            }

            override fun onItemAtEndLoaded(itemAtEnd: UiComicStrip) {
                super.onItemAtEndLoaded(itemAtEnd)
                if (itemAtEnd.number != 1) {
                    lastLoadedIndex.postValue(itemAtEnd.number - 1)
                }
            }
        }
        pagedItems = LivePagedListBuilder(loadCardPagesInteractor.getPages(), pagesConfigProvider.pagesCount)
            .setBoundaryCallback(UiComicStripBoundaryCallback())
            .build()
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

    fun showSearch() {
        dataStore.maxStripIndex.let { index ->
            if (index > 0) {
                showSearchDialog(index)
            }
        }
    }

    fun setSearchParameter(query: String?) {
        val stripNumber = searchProcessInteractor.process(query)
        if (stripNumber != null) {
            searchQueryId = stripNumber
            refresh()
        } else {
            actionLiveData.postValue(ComicAction.ShowError(UiError.SEARCH_NOT_FOUND))
        }
    }

    fun refresh() {
        resetPagesInteractor.resetData()
    }

    private fun showSearchDialog(comicStripId: Int) {
        actionLiveData.postValue(ComicAction.ShowDialog(AppDialog.Search(comicStripId)))
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

    fun shareCurrentItem() {
        currentItem?.run {
            actionLiveData.postValue(ComicAction.Share(this))
        }
    }

    fun setCurrentItem(uiComicStrip: UiComicStrip?) {
        this.currentItem = uiComicStrip
    }

    override fun onCleared() {
        interactors.forEach(BaseRxInteractor::clear)
        super.onCleared()
    }
}
