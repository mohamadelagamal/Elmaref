package com.elmaref.data.room.dao.quran.juz

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elmaref.data.model.quran.joz.Juz

@Dao
interface JuzDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllJuz(juzList: List<Juz>)

    @Query("SELECT * FROM juz_table")
     fun getAllJuz():List<Juz>

}