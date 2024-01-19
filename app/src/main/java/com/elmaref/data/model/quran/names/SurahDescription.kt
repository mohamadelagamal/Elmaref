package com.quranscreen.model.quran.names

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity (tableName = "Surah_description_table")
data class SurahDescription(
    @PrimaryKey(autoGenerate = true)
    val id: Int ?= null,
    @ColumnInfo
    val place: String ?= null,
    @ColumnInfo
    val type: String ?= null,
    @ColumnInfo
    val count: Int ?= null,
    @ColumnInfo
    val title: String ?= null,
    @ColumnInfo
    val titleAr: String ?= null,
    @ColumnInfo
    val index: String ?= null,
    @ColumnInfo
    val pages:String ?= null,
   // @ColumnInfo
   // val juz: List<JuzDescription> ?= null,
): Parcelable
@Parcelize
data class JuzDescription(
    val index:String ?= null,
    val verse:List<VerseDescription>
): Parcelable
@Parcelize
data class VerseDescription(
    val start:String ?= null,
    val end:String ?= null,
): Parcelable