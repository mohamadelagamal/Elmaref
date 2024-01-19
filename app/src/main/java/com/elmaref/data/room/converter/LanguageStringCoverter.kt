package com.quranscreen.room.converter

import androidx.room.TypeConverter
import com.elmaref.data.model.quran.ayah.LanguageString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class LanguageStringConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): ArrayList<LanguageString>? {
        val gson = Gson()
        if (data == null) {
            return arrayListOf()
        }

        val listType = object : TypeToken<ArrayList<LanguageString>?>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: ArrayList<LanguageString>?): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }



}