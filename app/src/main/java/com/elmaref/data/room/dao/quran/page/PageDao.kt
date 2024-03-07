package com.elmaref.data.room.dao.quran.page


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elmaref.data.model.quran.page.VersesItem
import com.elmaref.data.room.tables.QuranTable
import kotlinx.coroutines.flow.Flow

@Dao
interface PageDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE) // if the word is already exist, ignore it
    // if the word is already exist, replace it
    suspend fun insertAllPages(pagesList: List<VersesItem>)
    @Query("SELECT * FROM verse_table ")
    fun getAllVerses(): Flow<MutableList<VersesItem>>
    @Query("SELECT * FROM verse_table ")
    suspend fun getAllVersesList(): MutableList<VersesItem>
    @Query("SELECT * FROM verse_table WHERE id IS :chapterId AND verse_number IS :verseNumber")
    suspend fun getAyahBySurahIdAndVerseNumber(chapterId: Int, verseNumber:Int): List<VersesItem>
    @Query("SELECT * FROM verse_table WHERE id IS :chapterId")
    suspend fun getAyahBySurahId(chapterId:Int): List<VersesItem>
    @Query("SELECT * FROM verse_table WHERE page_number IS :page")
    suspend fun getAyahLineByPage(page:Int): List<VersesItem>


}
