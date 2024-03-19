package com.elmaref.ui.app

import android.app.Application
import com.elmaref.data.model.quran.joz.Juz
import com.elmaref.data.model.quran.page.VersesItem
import com.elmaref.data.room.tables.QuranTable
import com.elmaref.utils.Prefs
import com.elmaref.utils.praytimes.PrayerTimeHelper.getPrayerTime
import com.quranscreen.constants.LocaleProvider
import com.quranscreen.model.quran.names.SurahDescription
import com.quranscreen.model.quran.names.SurahName
import com.quranscreen.utils.io

class MyApplication : Application() {

    companion object {
        var quranValue:MutableList<VersesItem> = mutableListOf()
        var juzData :List<Juz>? = null
        var surahNameData :List<SurahName>? = null
        var surahDescription: List<SurahDescription> = listOf()

    }

    override fun onCreate() {
        super.onCreate()
        QuranTable.initializeDatabase(this@MyApplication) // initialize the database

        com.pixplicity.easyprefs.library.Prefs.Builder()
            .setContext(this)
            .build()

        io {
            Prefs.praytime= getPrayerTime(this)
        }
        LocaleProvider.init(this)
    }

}
