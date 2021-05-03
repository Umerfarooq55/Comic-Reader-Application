package com.example.shortcut.ui.fragment.comics.comics.binder

interface ViewActionCallbacks {
    fun onErrorShown()
    fun toggleFavourite(comicStripId: Int, isFavourite: Boolean)
}
