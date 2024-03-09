package com.elmaref.ui.quran.container

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.elmaref.data.room.tables.QuranTable
import com.example.muslim.ui.base.activity.BaseViewModel
import com.quranscreen.model.quran.names.SurahDescription
import kotlinx.coroutines.launch

class QuranContainerViewModel: BaseViewModel<Navigator>() {
    fun openFavoriteAyahs(){
        navigator?.openFavoriteAyahs()
    }

    fun openSearch(){
        navigator?.openSearch()
    }
}