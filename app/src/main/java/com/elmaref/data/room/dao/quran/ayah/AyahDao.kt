package com.elmaref.data.room.dao.quran.ayah

import androidx.fragment.app.Fragment
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elmaref.data.model.quran.ayah.Ayah
import com.elmaref.data.room.tables.QuranTable

@Dao
interface AyahDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAyah(book:List<Ayah>)
    @Query("Select count() from ayah")
    suspend fun getAyahCount():Int
    @Query("SELECT * FROM ayah WHERE chapter_id IS :chapterId AND verse_number IS :verseNumber")
    suspend fun getAyah(chapterId: Int, verseNumber:Int): List<Ayah>


}
