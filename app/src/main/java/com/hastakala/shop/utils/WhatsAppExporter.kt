package com.hastakala.shop.utils

import android.content.Context
import android.content.Intent

object WhatsAppExporter {

    fun shareSummary(context: Context, summaryText: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, summaryText)
        }
        val chooserIntent = Intent.createChooser(shareIntent, "Share Weekly Summary")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }
}
