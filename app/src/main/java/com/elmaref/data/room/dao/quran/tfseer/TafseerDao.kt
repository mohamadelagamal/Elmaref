package com.elmaref.data.room.dao.quran.tfseer

import androidx.fragment.app.Fragment
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elmaref.data.model.quran.tafseer.Tafseer
import com.elmaref.data.room.tables.QuranTable

@Dao
interface TafseerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllTfseer(tfseerList: List<Tafseer>)
    @Query("SELECT * FROM tafseer_table WHERE number IS :surahNumber AND aya IS :verseNumber")
    suspend fun getAyahBySurahIdAndVerseNumber(surahNumber: Int, verseNumber:Int): List<Tafseer>


}
fun Fragment.getTafseerDao(): TafseerDao {
    return QuranTable.buildDatabase(requireActivity()).tfseerDao()
}