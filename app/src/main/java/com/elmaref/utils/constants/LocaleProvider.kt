package com.quranscreen.constants

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.pixplicity.easyprefs.library.Prefs

class LocaleProvider(private var context: Context?) {


    private var mStringMap: HashMap<String, String>? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var ins: LocaleProvider? = null

        @Synchronized
        fun getInstance(): LocaleProvider {
            return ins!!
        }

        @Synchronized
        fun getString(key:String):String{
            return ins!!.getString(key)
        }

        fun init(context: Context) {
            ins = LocaleProvider(context)
        }
        const val PREF_KEY_LANGUAGE = "pref_language"
    }
    var language: String
        get() = Prefs.getString(PREF_KEY_LANGUAGE, "english")
        set(value) {
            Prefs.putString(PREF_KEY_LANGUAGE, value)
        }
    fun getString(key: String): String {
        val language = language
        val jsonString = context?.resources?.getIdentifier("locale_$language", "raw", context?.packageName)?.let {
            context?.resources?.openRawResource(it)?.bufferedReader().use { it?.readText() }
        }
        val jsonElement = Gson().fromJson(jsonString, JsonElement::class.java)
        val json = jsonElement.asJsonObject
        for (entry in json.entrySet()) {
            if (entry.key == key) {
                return entry.value.asString
            }
        }
        return key
    }
}
