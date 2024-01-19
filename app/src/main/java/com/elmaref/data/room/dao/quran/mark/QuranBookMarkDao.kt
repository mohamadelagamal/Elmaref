package com.elmaref.data.room.dao.quran.mark

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.elmaref.data.model.quran.mark.BookmarkHighlightUpdate
import com.elmaref.data.model.quran.mark.QuranBookMark
import com.elmaref.data.room.tables.QuranTable

@Dao
interface QuranBookMarkDao {

    //
    @Query("SELECT * FROM quran_bookmark") // get all bookmark from quran_bookmark table in database using live data
    fun getBookmarks(): LiveData<List<QuranBookMark>>
    @Query("SELECT * FROM quran_bookmark WHERE highlighted IS :highlighted")
    // get all bookmark from quran_bookmark table in database using live data where highlighted is true
    fun getHighlights(highlighted:Boolean?=true): LiveData<List<QuranBookMark>>
    @Query("SELECT * FROM quran_bookmark WHERE highlighted IS :highlighted")
    // get all bookmark from quran_bookmark table in database using suspend function where highlighted is true
    suspend fun getHighlightsSuspend(highlighted:Boolean?=true): List<QuranBookMark>

    @Query("SELECT * FROM quran_bookmark WHERE type IS :type")
    // get all bookmark from quran_bookmark table in database using live data where type is quran
    fun getBookmarksByType(type:String): LiveData<List<QuranBookMark>>

    @Query("SELECT * FROM quran_bookmark WHERE type IS :type")
    // get all bookmark from quran_bookmark table in database using suspend function where type is quran
    suspend fun getBookmarksByTypeSuspend (type:String): List<QuranBookMark>

    @Query("SELECT * FROM quran_bookmark WHERE idString IS :ids")
    // get all bookmark from quran_bookmark table in database using suspend function where idString is ids
    suspend fun getBookmarkByIdSuspend(ids:String): QuranBookMark?

    @Query("SELECT * FROM quran_bookmark WHERE idString IS :ids")
    // get all bookmark from quran_bookmark table in database using live data where idString is ids
    fun getBookmarkById(ids:String): LiveData<QuranBookMark?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // insert bookmark to quran_bookmark table in database using suspend function
    suspend fun insertBookMark(bookmark: QuranBookMark):Long

    @Query("DELETE FROM quran_bookmark WHERE :ids IS idString AND type IS :type")
    // delete bookmark from quran_bookmark table in database using suspend function where idString is ids and type is quran
    suspend fun deleteBookmark(ids:String, type:String)

    @Update(entity = QuranBookMark::class)
    // update bookmark from quran_bookmark table in database using suspend function
    suspend fun updateHighlight(update: BookmarkHighlightUpdate)


}

fun Context.getBookmarkDao(): QuranBookMarkDao {
    return QuranTable.getDatabase(this).quranBookMarkDao()
}