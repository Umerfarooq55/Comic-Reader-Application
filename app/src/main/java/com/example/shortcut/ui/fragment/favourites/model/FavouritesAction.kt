package com.example.shortcut.ui.fragment.favourites.model

import com.example.shortcut.model.AppDialog


sealed class FavouritesAction {
    object Idle : FavouritesAction()
    data class ShowDialog(val appDialog: AppDialog) : FavouritesAction()
}
