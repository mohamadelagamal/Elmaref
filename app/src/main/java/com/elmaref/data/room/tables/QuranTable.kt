package com.elmaref.data.room.tables

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elmaref.data.model.quran.ayah.Ayah
import com.elmaref.data.model.quran.joz.Juz
import com.elmaref.data.model.quran.mark.QuranBookMark
import com.elmaref.data.model.quran.page.VersesItem
import com.elmaref.data.model.quran.tafseer.Tafseer
import com.elmaref.data.room.dao.quran.ayah.AyahDao
import com.elmaref.data.room.dao.quran.juz.JuzDao
import com.elmaref.data.room.dao.quran.mark.QuranBookMarkDao
import com.elmaref.data.room.dao.quran.page.PageDao
import com.elmaref.data.room.dao.quran.tfseer.TafseerDao
import com.quranscreen.model.quran.names.SurahDescription
import com.quranscreen.model.quran.names.SurahName
import com.quranscreen.room.converter.LanguageStringConverter
import com.quranscreen.room.converter.MyTypeConverters
import com.quranscreen.room.dao.quran.names.SurahNameDao
import com.quranscreen.room.dao.quran.names.SurahDescriptionDao

@Database(
    entities = [VersesItem::class, Juz::class, SurahName::class,
        Ayah::class, Tafseer::class, SurahDescription::class,
        QuranBookMark::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    MyTypeConverters::class,
    LanguageStringConverter::class
)
abstract class QuranTable : RoomDatabase() {

    abstract fun quranDao(): PageDao
    abstract fun juzDao(): JuzDao
    abstract fun ayahDao(): AyahDao
    abstract fun surahNameDao(): SurahNameDao
    abstract fun tfseerDao(): TafseerDao
    abstract fun quranBookMarkDao(): QuranBookMarkDao
    abstract fun surahDescriptionDao(): SurahDescriptionDao

    companion object {
        const val DATABASE_NAME = "quran.db"

        @Volatile
        var INSTANCE: QuranTable? = null
        fun getDatabase(context: Context): QuranTable =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, QuranTable::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration() //
                .build()


        fun returnInstance(): QuranTable {
            return INSTANCE!!
        }

    }
}