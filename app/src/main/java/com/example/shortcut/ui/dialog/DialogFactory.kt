package com.example.shortcut.ui.dialog

import android.app.Activity
import android.text.InputType
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.example.shortcut.R

import com.yarolegovich.lovelydialog.LovelyInfoDialog
import com.yarolegovich.lovelydialog.LovelyTextInputDialog
import javax.inject.Inject

internal class DialogFactory @Inject constructor() {

    fun showSearch(activity: Activity, maxStripIndex: Int, callback: (String) -> Unit) {
        LovelyTextInputDialog(activity)
            .setTopColorRes(R.color.primary_dark)
            .setIcon(R.drawable.ic_search_black_24dp)
            .setIconTintColor(ContextCompat.getColor(activity, R.color.white))
            .setMessage(activity.getString(R.string.dialog_search_title))
            .setHint(activity.getString(R.string.dialog_search_hint, maxStripIndex.toString()))
            .setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED)
            .setConfirmButtonColor(
                ContextCompat.getColor(
                    activity,
                    R.color.secondary_text
                )
            )
            .setConfirmButton(android.R.string.ok, callback::invoke)
            .show()
    }


}
