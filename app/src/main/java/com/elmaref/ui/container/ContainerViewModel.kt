package com.elmaref.ui.container

import android.content.Context
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.elmaref.data.repository.implementation.QuranOfflineDataSourceImpl
import com.elmaref.data.repository.interfaces.offline.QuranOfflineDataSource
import com.elmaref.data.room.tables.QuranTable
import com.elmaref.ui.app.MyApplication
import com.example.muslim.ui.base.activity.BaseViewModel
import com.quranscreen.model.quran.names.SurahDescription
import com.quranscreen.model.quran.names.SurahName
import kotlinx.coroutines.launch

class ContainerViewModel: BaseViewModel<Navigator>() {

    lateinit var quranDataRepository: QuranOfflineDataSource
    init {
        quranDataRepository = QuranOfflineDataSourceImpl(QuranTable.getInstance())
        getSurahNames()
        getJuz()
        checkPageQuran()

    }

    private fun getJuz() {
        viewModelScope.launch {
            val juz = quranDataRepository.getJuz()
            MyApplication.juzData = juz
        }
    }
    suspend  fun getSurahName():List<SurahName>{
           return quranDataRepository.getQuranSurahNames()
    }
    suspend fun getSurahDescription():List<SurahDescription>{
        return quranDataRepository.getQuranSurahNamesDescription()
    }
    fun getSurahNames(){
        viewModelScope.launch {
            val surahName = quranDataRepository.getQuranSurahNames()
            MyApplication.surahNameData = surahName
        }
        viewModelScope.launch {
            val surahDescription = quranDataRepository.getQuranSurahNamesDescription()
            MyApplication.surahDescription = surahDescription
        }
    }
    fun checkPageQuran() {
            if (!MyApplication.quranValue.size.equals(6236)) {
                viewModelScope.launch {
                    val quran = quranDataRepository.getAllVersesList()
                    MyApplication.quranValue = quran
                }
            }
        }
}