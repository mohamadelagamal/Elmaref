package com.quranscreen.room.dao.quran.names

import androidx.fragment.app.Fragment
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elmaref.room.tables.QuranTable
import com.quranscreen.model.quran.names.SurahName

@Dao

interface SurahNameDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllSurahName(surahNameList: List<SurahName>)

    @Query("SELECT * FROM surah_name_table")
      fun getAllSurahName(): List<SurahName>

    @Query("SELECT * FROM surah_name_table WHERE id is :id")
    suspend fun getSurahById(id:Int): List<SurahName>
}
fun Fragment.getSurahDao(): SurahNameDao {
    return QuranTable.buildDatabase(requireActivity()).surahNameDao()
}