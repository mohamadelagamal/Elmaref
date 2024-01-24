package com.elmaref.data.repository.implementation

import com.elmaref.data.model.quran.ayah.Ayah
import com.elmaref.data.model.quran.joz.Juz
import com.elmaref.data.model.quran.mark.QuranBookMark
import com.elmaref.data.model.quran.page.VersesItem
import com.elmaref.data.model.quran.tafseer.Tafseer
import com.elmaref.data.repository.interfaces.offline.QuranOfflineDataSource
import com.elmaref.data.room.tables.QuranTable
import com.quranscreen.model.quran.names.SurahDescription
import com.quranscreen.model.quran.names.SurahName
import kotlinx.coroutines.flow.Flow

class QuranOfflineDataSourceImpl(val myDataBase:QuranTable):QuranOfflineDataSource {
    override suspend fun insertQuranSurahNames(quranNames: List<SurahName>) {
        myDataBase.surahNameDao().insertAllSurahName(quranNames)
    }

    override suspend fun getQuranSurahNames(): List<SurahName> {
        return myDataBase.surahNameDao().getAllSurahName()
    }

    override suspend fun getQuranSurahNamesById(id: Int): List<SurahName> {
        return myDataBase.surahNameDao().getSurahById(id)
    }

    override suspend fun insertQuranSurahNamesDescription(quranNames: List<SurahDescription>) {
        myDataBase.surahDescriptionDao().insertAllSurahDescription(quranNames)
    }

    override suspend fun getQuranSurahNamesDescription(): List<SurahDescription> {
        return myDataBase.surahDescriptionDao().getAllSurahDescription()
    }

    override suspend fun insertJuz(juzList: List<Juz>) {
        myDataBase.juzDao().insertAllJuz(juzList)
    }

    override suspend fun getJuz(): List<Juz> {
        return myDataBase.juzDao().getAllJuz()
    }

    override suspend fun insertVerses(verses: List<VersesItem>) {
        myDataBase.quranDao().insertAllPages(verses)
    }

    override suspend fun getAllVerses(): Flow<MutableList<VersesItem>> {
        return myDataBase.quranDao().getAllVerses()
    }

    override suspend fun getAllVersesList(): MutableList<VersesItem> {
        return myDataBase.quranDao().getAllVersesList()
    }



    override suspend fun insertAyah(ayah: List<Ayah>) {
        myDataBase.ayahDao().insertAyah(ayah)
    }

    override suspend fun getAyahCount(): Int {
        return myDataBase.ayahDao().getAyahCount()
    }

    override suspend fun getAyah(surahId: Int, verseId: Int): List<Ayah> {
        return myDataBase.ayahDao().getAyah(surahId,verseId)
    }

    override suspend fun insertTfseer(tfseer: List<Tafseer>) {
        myDataBase.tfseerDao().insertAllTfseer(tfseer)
    }

    override suspend fun getTfseerByIdAndVerseNumber(surahId: Int, verseId: Int): List<Tafseer> {
        return myDataBase.tfseerDao().getATfseerByIdAndVerseNumber(surahId,verseId)
    }

    override suspend fun insertBookMark(bookMark: QuranBookMark) {
        myDataBase.quranBookMarkDao().insertBookMark(bookMark)
    }

    override suspend fun getBookMarkById(id: String): QuranBookMark? {
        return myDataBase.quranBookMarkDao().getBookmarkByIdSuspend(id)
    }

    override suspend fun deleteBookmark(id: String, type: String) {
        myDataBase.quranBookMarkDao().deleteBookmark(id,type)
    }


}