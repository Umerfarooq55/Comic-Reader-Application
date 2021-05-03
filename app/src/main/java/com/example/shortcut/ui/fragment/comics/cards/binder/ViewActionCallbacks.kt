package com.example.shortcut.ui.fragment.comics.cards.binder

import com.example.shortcut.ui.model.UiComicStrip


interface ViewActionCallbacks {
    fun onItemAppeared(uiComicStrip: UiComicStrip)
    fun onErrorShown()
    fun toggleFavourite(comicStripId: Int, isFavourite: Boolean)
}
