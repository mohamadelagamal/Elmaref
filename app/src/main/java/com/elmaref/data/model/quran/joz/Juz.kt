package com.elmaref.data.model.quran.joz

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Juz_table")
data class Juz(
    @PrimaryKey(autoGenerate = true)
    var juz: Int ?= null,
    @ColumnInfo(name = "surah")
    val surah: String ?= null,
    @ColumnInfo(name = "surah_id")
    val surah_id: Int ?= null,
    @ColumnInfo(name = "verse")
    val verse: Int ?= null,
    @ColumnInfo(name = "verse_id")
    val start_page: Int ?= null,
    @ColumnInfo(name = "end_page")
    val end_page: Int ?= null,
    @ColumnInfo(name = "hizb")
    var isHizbShown: Boolean = false,
    @ColumnInfo(name = "juzNameArabic")
    var juz_name_arabic:String ?= null,
) : Parcelable

