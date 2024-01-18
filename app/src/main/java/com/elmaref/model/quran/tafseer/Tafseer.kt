package com.elmaref.model.quran.tafseer

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Tafseer_table")
data class Tafseer (
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    @ColumnInfo
    val number:String?=null,
    @ColumnInfo
    val aya:String?=null,
    @ColumnInfo
    val text:String?=null,
): Parcelable {
}