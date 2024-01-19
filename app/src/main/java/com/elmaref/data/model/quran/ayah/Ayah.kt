package com.elmaref.data.model.quran.ayah

import android.content.Context
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elmaref.data.model.quran.mark.BookmarkType
import com.elmaref.data.model.quran.mark.QuranBookMark
import com.elmaref.data.room.dao.quran.mark.getBookmarkDao
import com.quranscreen.utils.io
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "Ayah")
data class Ayah(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val verse_number:Int,
    @ColumnInfo(name = "text")
    var text: ArrayList<LanguageString> = ArrayList(),
    @ColumnInfo(name = "juz")
    var juz:Int,
    @ColumnInfo(name = "hizb")
    var hizb:Float,
    @ColumnInfo(name = "use_bismillah")
    var use_bismillah:Boolean?=false,
    @ColumnInfo(name = "chapter_id")
    var chapterId:Int
){
    companion object{
        fun Ayah.bookMark(context: Context){
            context.apply{
                io{
                    val isBookmarked = getBookmarkDao().getBookmarkByIdSuspend(id.toString()) != null
                    if (!isBookmarked) {
                        getBookmarkDao().insertBookMark(
                            QuranBookMark(
                                idString = id.toString(),
                                type = BookmarkType.QURAN
                            )
                        )

                    } else {
                        getBookmarkDao()
                            .deleteBookmark(id.toString(), BookmarkType.QURAN)
                    }
                }
            }
        }
    }
}
@Parcelize
data class LanguageString(
    var text: String? = null,
    var language: String? = null
): Parcelable
