package com.sa.mytodo.data.data_sources.local

import android.content.Context
import android.content.SharedPreferences

object LocalSession {
    private const val appName: String ="My_Todo_APP"
    private const val CURRENT_PAGE_SKIP="current_page_skip"

    fun setCurrentPageSkip(c: Context, value: Int) {
        val editor: SharedPreferences.Editor? = c.getSharedPreferences(
            appName,
            Context.MODE_PRIVATE
        ).edit()
        editor?.putInt(CURRENT_PAGE_SKIP, value)
        editor?.apply()
        editor?.commit()
    }
    fun getCurrentPageSkip(c: Context, defaultValue: Int=-1): Int {
        val prefs: SharedPreferences = c.getSharedPreferences(
            appName,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(CURRENT_PAGE_SKIP, defaultValue)
    }
}