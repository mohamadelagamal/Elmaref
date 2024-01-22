package com.elmaref.data.repository.interfaces.offline

import androidx.lifecycle.LiveData
import com.elmaref.data.model.quran.ayah.Ayah
import com.elmaref.data.model.quran.joz.Juz
import com.elmaref.data.model.quran.page.VersesItem
import com.elmaref.data.model.quran.tafseer.Tafseer
import com.quranscreen.model.quran.names.SurahDescription
import com.quranscreen.model.quran.names.SurahName
import kotlinx.coroutines.flow.Flow

interface QuranOfflineDataSource {
    suspend fun insertQuranSurahNames(quranNames: List<SurahName>)
    suspend fun getQuranSurahNames(): List<SurahName>
    suspend fun insertQuranSurahNamesDescription(quranNames: List<SurahDescription>)
    suspend fun getQuranSurahNamesDescription(): List<SurahDescription>
    suspend fun insertJuz(juzList: List<Juz>)
    suspend fun getJuz(): List<Juz>
    suspend fun insertVerses(verses: List<VersesItem>)
    suspend fun getAllVerses(): Flow<MutableList<VersesItem>>
    suspend fun getAllVersesList(): MutableList<VersesItem>
    suspend fun insertAyah(ayah: List<Ayah>)
    suspend fun getAyahCount():Int
    suspend fun insertTfseer(tfseer: List<Tafseer>)

}