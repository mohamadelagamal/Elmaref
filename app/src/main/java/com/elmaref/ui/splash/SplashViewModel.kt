package com.elmaref.ui.splash

import android.content.Context
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.elmaref.data.repository.implementation.QuranOfflineDataSourceImpl
import com.elmaref.data.repository.interfaces.offline.QuranOfflineDataSource
import com.elmaref.data.room.tables.QuranTable
import com.elmaref.ui.app.MyApplication
import com.example.muslim.ui.base.activity.BaseViewModel
import com.quranscreen.utils.io
import com.quranscreen.utils.main
import kotlinx.coroutines.launch

class SplashViewModel: BaseViewModel<Navigator>() {

    lateinit var quranRepository:QuranOfflineDataSource
    init {
        quranRepository = QuranOfflineDataSourceImpl(QuranTable.getInstance())
    }

    fun getPageQuran()= liveData {
        quranRepository.getAllVerses().collect { verses ->
            emit(verses) //
        }
    }


}