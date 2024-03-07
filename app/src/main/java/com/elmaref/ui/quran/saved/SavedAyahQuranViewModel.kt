package com.elmaref.ui.quran.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elmaref.data.model.quran.mark.QuranBookMark
import com.elmaref.data.repository.implementation.QuranOfflineDataSourceImpl
import com.elmaref.data.repository.interfaces.offline.QuranOfflineDataSource
import com.elmaref.data.room.tables.QuranTable
import com.example.muslim.ui.base.activity.BaseViewModel
import kotlinx.coroutines.launch

class SavedAyahQuranViewModel:BaseViewModel<Navigator>() {

    var SavedAyahList= MutableLiveData<List<QuranBookMark>>()
    lateinit var quranOfflineDataSource: QuranOfflineDataSource


    init {

        quranOfflineDataSource= QuranOfflineDataSourceImpl(QuranTable.getInstance())

        getSavedAyahList()
    }
    fun getSavedAyahList(){
        viewModelScope.launch {
           quranOfflineDataSource.getSavedAyahList().observeForever {
                SavedAyahList.value=it
           }
        }

    }
    fun deleteBookmark(idString: String,type:String){
        viewModelScope.launch {
            quranOfflineDataSource.deleteBookmark(idString,type)
        }

    }
}