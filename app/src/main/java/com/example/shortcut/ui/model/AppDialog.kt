package com.example.shortcut.model

sealed class AppDialog {
    data class Search(val maxStripIndex: Int) : AppDialog()
    object About : AppDialog()
}
