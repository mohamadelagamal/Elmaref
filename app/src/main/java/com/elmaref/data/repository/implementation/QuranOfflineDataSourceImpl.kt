package com.elmaref.data.repository.implementation

import androidx.lifecycle.LiveData
import com.elmaref.data.model.quran.ayah.Ayah
import com.elmaref.data.model.quran.joz.Juz
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
        myDataBase.ayahDao().putAyah(ayah)
    }

    override suspend fun getAyahCount(): Int {
        return myDataBase.ayahDao().getAyahCount()
    }

    override suspend fun insertTfseer(tfseer: List<Tafseer>) {
        myDataBase.tfseerDao().insertAllTfseer(tfseer)
    }


}