package com.example.shortcut.ui.fragment.comics.model

import com.example.shortcut.model.AppDialog
import com.example.shortcut.ui.error.UiError
import com.example.shortcut.ui.model.UiComicStrip

sealed class ComicAction {
    object Idle : ComicAction()
    object ShowContent : ComicAction()
    data class ShowError(val uiError: UiError) : ComicAction()
    data class ShowDialog(val appDialog: AppDialog) : ComicAction()
    data class Share(val uiComicStrip: UiComicStrip) : ComicAction()
}
