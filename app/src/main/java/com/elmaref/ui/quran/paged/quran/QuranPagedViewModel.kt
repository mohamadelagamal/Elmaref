package com.elmaref.ui.quran.paged.quran


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.elmaref.app.MyApplication
import com.example.muslim.ui.base.activity.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuranPagedViewModel : BaseViewModel<Navigator>() {

    val surahName = MutableLiveData<String>()
    val surahId = MutableLiveData<Int>()
    fun getData2()= liveData {
        val db = MyApplication.verses
        db.collect { verses ->
            emit(verses) //
        }
    }
    fun getSurahName(id:Int){
        viewModelScope.launch(Dispatchers.IO){
          //    surahName.postValue(PageDao.getSurahById(id).firstOrNull()?.name)
            surahId.postValue(id)
        }
    }
}
