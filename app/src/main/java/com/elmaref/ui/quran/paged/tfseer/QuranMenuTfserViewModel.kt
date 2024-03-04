package com.elmaref.ui.quran.paged.tfseer

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmaref.data.api.ApiManager
import com.elmaref.data.model.quran.ayah.Ayah
import com.elmaref.data.model.quran.mark.QuranBookMark
import com.elmaref.data.model.quran.tafseer.Tafseer
import com.elmaref.data.repository.implementation.QuranOfflineDataSourceImpl
import com.elmaref.data.repository.interfaces.offline.QuranOfflineDataSource
import com.elmaref.data.room.tables.QuranTable
import com.elmaref.ui.app.MyApplication
import com.example.muslim.ui.base.activity.BaseViewModel
import com.quranscreen.model.quran.names.SurahName
import kotlinx.coroutines.launch

class QuranMenuTfserViewModel:BaseViewModel<Navigator>() {


    lateinit var quranOfflineDataSource: QuranOfflineDataSource
    var audioUrl: String? = null

    init {
        quranOfflineDataSource= QuranOfflineDataSourceImpl(QuranTable.getInstance())


    }
   suspend fun getAyahId(surahId:Int, verseId:Int): Int? {
       return quranOfflineDataSource.getAyah(surahId,verseId).firstOrNull()?.id
    }
    suspend fun getAyah(surahId:Int, verseId:Int): List<Ayah> {
        return quranOfflineDataSource.getAyah(surahId,verseId)
    }
    suspend fun getBookMarkDao(id:String):Boolean{
        return quranOfflineDataSource.getBookMarkById(id) != null // if not null return true else return false
    }
    suspend fun insertBookMark(idString: String,type:String){
        val quranBookMark = QuranBookMark(idString = idString,type = type)
        quranOfflineDataSource.insertBookMark(quranBookMark)
    }
    suspend fun deleteBookMark(idString: String,type:String){
        quranOfflineDataSource.deleteBookmark(idString,type)
    }
    suspend fun getSurahById(id:Int):List<SurahName>{
       return quranOfflineDataSource.getQuranSurahNamesById(id)
    }
    suspend fun getTfseerByIdAndVerseNumber(surahId:Int,verseId:Int):List<Tafseer>{
        return quranOfflineDataSource.getTfseerByIdAndVerseNumber(surahId=surahId,verseId=verseId)
    }

    fun getAyahAudio(number: Int) {
        viewModelScope.launch {
            try {
                val api = ApiManager.getApi()
                val response = api.getSpecificAya(7, "$number")
                Log.e("Ayahs", "Response: ${response.audioFiles?.get(0)?.url}")
                 audioUrl = response.audioFiles?.get(0)?.url


            } catch (e: Exception) {
                Log.e("Ayahs", "Error: ${e.message}")
            }
    }
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}