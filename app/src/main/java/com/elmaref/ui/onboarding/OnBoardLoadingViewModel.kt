package com.elmaref.ui.onboarding

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elmaref.model.enums.ResultEnum
import com.elmaref.model.quran.page.VersesItem
import com.elmaref.room.tables.QuranTable
import com.elmaref.ui.quran.paged.functions.parseAyahFromJson
import com.elmaref.ui.quran.paged.functions.parseJuzFromJson
import com.elmaref.ui.quran.paged.functions.parseSurahNamesFromJson
import com.elmaref.ui.quran.paged.functions.parseSurahNamesInformationFromJson
import com.elmaref.ui.quran.paged.functions.parseTfseerFromJson
import com.example.muslim.ui.base.activity.BaseViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnBoardLoadingViewModel : BaseViewModel<Navigator>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            setQuranToDataBase() //
        }
    }

    val isQuranLoaded = MutableLiveData(ResultEnum.Loading)

    suspend fun setQuranToDataBase() {
        try {


            loopQuranDB()
            savedJuzToDB(context = context)
            savedSurahNameToDB(context = context)
            saveAyaToDB(context = context)
            tfseerToDB(context = context)
            savedSurahDescriptionToDB(context = context)
            isQuranLoaded.postValue(ResultEnum.Success)

        } catch (exception: Exception) {

        }
    }

    suspend private fun savedSurahDescriptionToDB(context: Context?) {
        val surahInfoItem = parseSurahNamesInformationFromJson(context!!.assets, "quran/surah_ui.json")
        val surahDescriptionTable = QuranTable.buildDatabase(context).surahDescriptionDao()
        surahDescriptionTable.insertAllSurahDescription(surahInfoItem)
    }

    suspend fun tfseerToDB(context: Context?) {
        val tfseer = parseTfseerFromJson(context!!.assets, "quran/tafseer.json")
        val tfseerTable = QuranTable.buildDatabase(context).tfseerDao()
        tfseerTable.insertAllTfseer(tfseer)
        Log.e("tfseer", "All tfseer is saved to database")
    }

    suspend fun saveAyaToDB(context: Context?) {
        val ayahTable = QuranTable.buildDatabase(context!!).ayahDao()
        val juzFrom_1_to_5= parseAyahFromJson(context.assets,"quran/quran1.json")
        ayahTable.putAyah(juzFrom_1_to_5)
        val juzFrom_6_to_10= parseAyahFromJson(context.assets,"quran/quran2.json")
        ayahTable.putAyah(juzFrom_6_to_10)
        val juzFrom_11_to_15= parseAyahFromJson(context.assets,"quran/quran3.json")
        ayahTable.putAyah(juzFrom_11_to_15)
        val juzFrom_16_to_20= parseAyahFromJson(context.assets,"quran/quran4.json")
        ayahTable.putAyah(juzFrom_16_to_20)
        val juzFrom_21_to_25= parseAyahFromJson(context.assets,"quran/quran5.json")
        ayahTable.putAyah(juzFrom_21_to_25)
        val juzFrom_26_to_30= parseAyahFromJson(context.assets,"quran/quran6.json")
        ayahTable.putAyah(juzFrom_26_to_30)
        if (ayahTable.getAyahCount()!= 6236)
        {
            saveAyaToDB(context = context)
            return
        }
    }

    suspend fun savedSurahNameToDB(context: Context?) {
        val surahName = parseSurahNamesFromJson(context!!.assets, "quran/surah.json")
        val surahTable = QuranTable.buildDatabase(context).surahNameDao()
        surahTable?.insertAllSurahName(surahName)
    }

    suspend fun loopQuranDB() {
        for (i in 1..604) {
            // save each page to database
            Log.e("contextQuranViewModel", "${context}")
            context?.let { savedPageToDB(context = it, page = i) }
            // return all verses from database
        }
    }

    suspend fun savedJuzToDB(context: Context?) {
        val juzNumber = parseJuzFromJson(context!!.assets, "quran/juz.json")
        val juzTable = QuranTable.buildDatabase(context).juzDao()
        juzTable.insertAllJuz(juzNumber)
    }

    suspend fun savedPageToDB(context: Context, page: Int) {
        val json = context.assets.open("json/page$page.json").bufferedReader().use { it.readText() }
        // convert json to object of VersesItem
        var versesItem =
            Gson().fromJson<List<VersesItem>>(json, object : TypeToken<List<VersesItem>>() {}.type)
        // get all items from versesItem
        versesItem.map { it.page = page } // set page number for each item
        Log.e("versesItem", "${page}")
        val quran = QuranTable.buildDatabase(context)
        quran?.quranDao()?.insertAllPages(versesItem)


    }


}