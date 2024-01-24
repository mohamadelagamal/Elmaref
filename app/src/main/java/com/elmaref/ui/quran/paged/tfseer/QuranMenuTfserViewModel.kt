package com.elmaref.ui.quran.paged.tfseer

import androidx.lifecycle.ViewModel
import com.elmaref.data.model.quran.ayah.Ayah
import com.elmaref.data.model.quran.mark.QuranBookMark
import com.elmaref.data.model.quran.tafseer.Tafseer
import com.elmaref.data.repository.implementation.QuranOfflineDataSourceImpl
import com.elmaref.data.repository.interfaces.offline.QuranOfflineDataSource
import com.elmaref.data.room.tables.QuranTable
import com.quranscreen.model.quran.names.SurahName

class QuranMenuTfserViewModel:ViewModel() {

    lateinit var quranOfflineDataSource: QuranOfflineDataSource
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

}