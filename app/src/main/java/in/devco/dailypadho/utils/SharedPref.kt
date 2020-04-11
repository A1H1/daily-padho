package `in`.devco.dailypadho.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharedPref {
    private const val PREF_FILE = "pref_file"
    private const val CLICK_COUNT = "click_count"

    fun setClickCount(context: Context) {
        val pref = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        val prefEdit = pref.edit()
        prefEdit.putInt(CLICK_COUNT, pref.getInt(CLICK_COUNT, 0) + 1)
        prefEdit.apply()
    }

    fun getClickCount(context: Context): Int {
        val pref = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        return pref.getInt(CLICK_COUNT, 0)
    }

    fun resetClickCount(context: Context) {
        val pref = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE).edit()
        pref.putInt(CLICK_COUNT, 0)
        pref.apply()
    }
}
