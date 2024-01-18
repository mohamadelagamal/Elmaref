package com.quranscreen.model.quran.names

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Surah_name_table")
data class SurahName(
    @PrimaryKey(autoGenerate = true)
    val id: Int ?= null,
    @ColumnInfo(name = "surah_name")
    val name: String ?= null,
    @ColumnInfo(name="translation")
    val translation: List<Translation> ?= null,
    @ColumnInfo(name = "revelation")
    val revelation: String ?= null,
    @ColumnInfo(name = "verses")
    val verses: Int ?= null,
    @ColumnInfo(name = "page")
    val page: Int ?= null,
):Parcelable

@Parcelize
data class Translation(
    val language: String ?= null,
    val text: String ?= null,
):Parcelable