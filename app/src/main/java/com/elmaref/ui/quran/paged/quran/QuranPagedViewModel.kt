package com.elmaref.ui.quran.paged.quran


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.elmaref.data.repository.implementation.QuranOfflineDataSourceImpl
import com.elmaref.data.repository.interfaces.offline.QuranOfflineDataSource
import com.elmaref.data.room.tables.QuranTable
import com.elmaref.ui.app.MyApplication
import com.example.muslim.ui.base.activity.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuranPagedViewModel : BaseViewModel<Navigator>() {

    lateinit var quranDataRepository: QuranOfflineDataSource
    init {
        quranDataRepository = QuranOfflineDataSourceImpl(QuranTable.getInstance())
        checkPageQuran()
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
