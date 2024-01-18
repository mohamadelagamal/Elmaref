package com.quranscreen.room.converter

import androidx.room.TypeConverter
import com.elmaref.model.quran.page.WordItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quranscreen.model.quran.names.SurahDescription
import com.quranscreen.model.quran.names.Translation
import java.util.Date

class MyTypeConverters {

    @TypeConverter
    fun fromDateToLong(date: Date): Long {
        return date.time
    }
    @TypeConverter
    fun fromLongToDate(date: Long): Date {
        return Date(date)
    }
    @TypeConverter
    fun fromWordItemToJSON(wordItem: WordItem): String {
        return Gson().toJson(wordItem)
    }
    @TypeConverter
    fun fromJSONToWordItem(json: String): WordItem {
        return Gson().fromJson(json, WordItem::class.java)
    }
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<WordItem>? {
        val gson = Gson()
        if (data == null) {
            return arrayListOf()
        }

        val listType = object : TypeToken<List<WordItem>?>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<WordItem>?): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun stringToSomeObjectListTranslation(data: String?): List<Translation>? {
        val gson = Gson()
        if (data == null) {
            return arrayListOf()
        }

        val listType = object : TypeToken<List<Translation>?>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToStringTranslation(someObjects: List<Translation>?): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }

    // convert SurahDescription to json
    @TypeConverter
    fun fromSuraDescriptionToJSON(suraDescription: SurahDescription): String {
        return Gson().toJson(suraDescription)
    }
    // convert json to SurahDescription
    @TypeConverter
    fun fromJSONToSuraDescription(json: String): SurahDescription {
        return Gson().fromJson(json, SurahDescription::class.java)
    }
}