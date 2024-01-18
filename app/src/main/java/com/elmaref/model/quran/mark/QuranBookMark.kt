package com.elmaref.model.quran.mark

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "Quran_bookmark")
@Parcelize
data class QuranBookMark(
    @PrimaryKey(autoGenerate = false)
    val id:Int?=null, // id used for quran ayah and page bookmark only
    @ColumnInfo(name = "idString")
    var idString: String?=null, // this ayah number in quran
    @ColumnInfo(name = "type")
    var type:String = BookmarkType.QURAN, // type using to define bookmark type (quran, hadith, prayer)
    @ColumnInfo(name = "highlighted")
    var highlighted:Boolean = false, // this is used to define if the bookmark is highlighted or not
    @ColumnInfo(name = "group_id")
    var groupId:Int?=null // this is used to define the group of bookmark (quran, hadith, prayer)
):Parcelable


// this is used to define the group of bookmark (quran, hadith, prayer)
object BookmarkType{
    const val QURAN = "quran"
    const val QURANPAGE = "quranPage"
    const val HADITH = "hadith"
    const val PRAYER = "prayer"
}
// this is used to define if the bookmark is highlighted or not
data class BookmarkHighlightUpdate(val id:Int, val highlighted: Boolean)
