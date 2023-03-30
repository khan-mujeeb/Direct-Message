package com.example.dm.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.dm.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogUtils {
    fun buildLoadingDialog(context: Context): AlertDialog {
        val builder = MaterialAlertDialogBuilder(
            context,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
        )
        builder
            .setTitle("Please Wait")
            .setMessage("loading")
        builder.setCancelable(false)
        return builder.create()
    }
}

