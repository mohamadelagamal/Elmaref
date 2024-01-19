package com.elmaref.ui.app

import android.app.Application
import com.elmaref.data.model.quran.joz.Juz
import com.elmaref.data.model.quran.page.VersesItem
import com.elmaref.data.room.tables.QuranTable
import com.quranscreen.model.quran.names.SurahDescription
import com.quranscreen.model.quran.names.SurahName
import kotlinx.coroutines.flow.Flow

class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
        lateinit var verses: Flow<MutableList<VersesItem>> // this is the list of verses that will be used in the app
        var quranFlowValue:MutableList<VersesItem> = mutableListOf()
        var juzFLowData :List<Juz>? = null
        var surahNameFlowData :List<SurahName>? = null

        lateinit var quranTable: QuranTable
        var surahDescriptionObserver: List<SurahDescription> = listOf()

    }

    override fun onCreate() {
        super.onCreate()
        quranTable = QuranTable.buildDatabase(this@MyApplication)
        verses = quranTable.quranDao().getAllVerses()
        instance = this

    }

}
// coroutineScope is used to run the code in coroutine scope and wait until it finish then continue the code after it finish in the same thread that it was called from (in this case IO thread) and it's used to run suspend function from non suspend function like onCreate() function in Application class or main() function in Activity class
// runBlocking is used to run the code in coroutine scope and wait until it finish then continue the code after it finish in the same thread that it was called from (in this case IO thread) and it's used to run suspend function from non suspend function like onCreate() function in Application class or main() function in Activity class