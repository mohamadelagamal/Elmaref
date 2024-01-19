package com.quranscreen.room.dao.quran.names

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quranscreen.model.quran.names.SurahDescription

@Dao
interface SurahDescriptionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllSurahDescription(surahDescriptionList: List<SurahDescription>)
    @Query("SELECT * FROM surah_description_table")
    suspend fun getAllSurahDescription(): List<SurahDescription>

    @Query("SELECT * FROM surah_description_table WHERE `index` is :index")
    suspend fun getSurahDescriptionById(index:String): List<SurahDescription>

}
