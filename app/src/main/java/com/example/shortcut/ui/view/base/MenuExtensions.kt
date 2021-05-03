package com.example.shortcut.ui.view.base

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.Menu
import androidx.core.content.ContextCompat
import com.example.shortcut.R


internal fun Menu.extensionTintMenuItems(context: Context) {
    val tintColor = ContextCompat.getColor(context, R.color.white)
    val colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP)
    for (i in 0 until this.size()) {
        this.getItem(i).icon?.run {
            mutate()
            setColorFilter(colorFilter)
        }
    }
}
