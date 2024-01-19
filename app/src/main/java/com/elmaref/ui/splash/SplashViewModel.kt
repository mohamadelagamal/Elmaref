package com.elmaref.ui.splash

import android.content.Context
import androidx.lifecycle.liveData
import com.elmaref.data.room.tables.QuranTable
import com.example.muslim.ui.base.activity.BaseViewModel

class SplashViewModel: BaseViewModel<Navigator>() {

    fun getPageQuran(context: Context)= liveData {
        val db = QuranTable.buildDatabase(context).quranDao().getAllVerses()
        db.collect { verses ->
            emit(verses) //
        }
    }
    fun getSurahInfoItem(context: Context) = liveData {
        val db = QuranTable.buildDatabase(context).surahDescriptionDao()
        db.getAllSurahDescription().let { surahDescriptionList ->
            emit(surahDescriptionList)
        }
    }

}