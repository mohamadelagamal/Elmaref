package com.elmaref.room.dao.quran.ayah

import androidx.fragment.app.Fragment
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elmaref.model.quran.ayah.Ayah
import com.elmaref.room.tables.QuranTable

@Dao
interface AyahDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putAyah(book:List<Ayah>)
    @Query("Select count() from ayah")
    suspend fun getAyahCount():Int
    @Query("SELECT * FROM ayah WHERE chapter_id IS :chapterId AND verse_number IS :verseNumber")
    suspend fun getAyahBySurahIdAndVerseNumber(chapterId: Int, verseNumber:Int): List<Ayah>


}
fun Fragment.getAyahDao(): AyahDao {
    return QuranTable.buildDatabase(requireActivity()).ayahDao()
}