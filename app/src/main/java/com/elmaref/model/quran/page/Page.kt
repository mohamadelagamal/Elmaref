package com.elmaref.model.quran.page


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize // this annotation is used to make the class parcelable
data class WordItem(// this is the model for word json
    val line_number: Int? = null,
    val id: Int? = null,
    val position: Int? = null,
    var verse_key: String? = null,
    val code_v1: String? = null, // arabic text with tashkeel and punctuation marks (Uthmani)
    var page: Int? = null,
    var chapter_number: Int, // surah number
    val is_bismillah: Boolean? = false // this is for bismillah verse only
) : Parcelable

@Entity(tableName = "separator_table")
@Parcelize
data class SeparatorItem(// this is the model for separator json
    @PrimaryKey(autoGenerate = true) // autoGenerate is used to make the id auto increment
    val page: Int,
    @ColumnInfo(name = "surah")
    val surah: Int? = null,
    @ColumnInfo(name = "line")
    val line: Int,
    @ColumnInfo(name = "bismillah")
    val bismillah: Boolean? = false,
    @ColumnInfo(name = "unicode")
    val unicode: String? = "" // used to get the separator text (arabic text) from unicode
) : Parcelable

@Parcelize
@Entity(tableName = "verse_table")
data class VersesItem(
    // this is the model for page json
    @ColumnInfo(name = "words")
    val words: List<WordItem> = arrayListOf(),
    @PrimaryKey(autoGenerate = true) // autoGenerate is used to make the id auto increment
    val id: Int? = null,
    @ColumnInfo(name = "verse_number")
    val verse_number: Int? = null,
    @ColumnInfo(name = "verse_key")
    val verse_key: String? = null,
    @ColumnInfo(name = "juz_numberj")
    val juz_number: Int? = null,
    @ColumnInfo(name = "page_number")
    var page: Int? = null,
    @ColumnInfo(name = "hizb_number")
    val hizb_number: Int? = null,
) : Parcelable
