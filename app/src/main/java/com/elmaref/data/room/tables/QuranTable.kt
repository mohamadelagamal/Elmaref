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

// Database is used to create a database and it's used to create a database that will be created in the device
@Database(
    // entities is used to create the tables that will be created in the database
    entities = [VersesItem::class, Juz::class, SurahName::class,
        Ayah::class, Tafseer::class, SurahDescription::class,
        QuranBookMark::class],
    // version is used to create a new version of the database
    version = 3,
    // exportSchema is used to export the schema of the database to a file
    exportSchema = false
)
// TypeConverters is used to convert the data type to another data type
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

    // companion object is used to create a singleton object from the class
    companion object {
        // database name that will be created in the device
        const val DATABASE_NAME = "quran.db"

        @Volatile // volatile is used to make the variable visible to all threads
        var INSTANCE: QuranTable? = null // instance of the database
            // getDatabase is used to get the database instance and if the instance is null it will create a new instance
        fun initializeDatabase(context: Context): QuranTable =
            // if the instance is null it will create a new instance and if the instance is not null it will return the instance
            INSTANCE ?: synchronized(this) { // synchronized is used to make the threads work in order
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        // buildDatabase is used to create a new instance of the database
        fun buildDatabase(context: Context) =
            // databaseBuilder is used to create a new instance of the database
            Room.databaseBuilder(context, QuranTable::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration() // fallbackToDestructiveMigration is used to delete the old database and create a new database
                .build() // build used to build the database


        fun getInstance(): QuranTable {
            return INSTANCE!!
        }

    }
}