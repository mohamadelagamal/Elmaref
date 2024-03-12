package com.elmaref.ui.quran.paged.functions


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.media.MediaScannerConnection
import android.net.Uri
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.core.widget.TextViewCompat
import com.elmaref.R
import com.elmaref.databinding.LineSurahHeaderBinding
import com.elmaref.data.model.quran.ayah.Ayah
import com.elmaref.data.model.quran.ayah.LanguageString
import com.elmaref.data.model.quran.joz.Juz
import com.elmaref.data.model.quran.page.SeparatorItem
import com.elmaref.data.model.quran.page.VersesItem
import com.elmaref.data.model.quran.page.WordItem
import com.elmaref.data.model.quran.tafseer.Tafseer
import com.elmaref.data.room.tables.QuranTable
import com.elmaref.ui.quran.paged.tfseer.QuranMenuTfser
import com.elmaref.ui.quran.paged.tfseer.options.bitmap.SaveBitmapToFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quranscreen.model.quran.names.SurahDescription
import com.quranscreen.model.quran.names.SurahName
import com.quranscreen.utils.dpToPx
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun getVerseByLine(line: Int, words: List<WordItem>): String {
    // filter is used to filter the list of words by line number
    return words.filter { it.line_number == line }.sortedBy { it.id }.map {
        // code_v1 is the arabic text with tashkeel and punctuation marks (Uthmani)
        it.code_v1
    }.joinToString("") // joinToString is used to join the list of string to one string
}

suspend fun getSeparatorByLineAndPage(context: Context, page: Int, line: Int): SeparatorItem? {
    // getSeparatorByLineAndPage is used to get the separator item model by page and line
    return getSeparator(context).filter { it.page == page && it.line == line }.firstOrNull()
}

// getSeparator is used to get the separator json file from assets folder and convert it to list of separator item model
suspend fun getSeparator(context: Context): List<SeparatorItem> {
    return suspendCoroutine { suspended ->
        context.assets.open("json/separator.json").bufferedReader().use {
            val separator = Gson().fromJson<ArrayList<SeparatorItem>>(it.readLine(), object :
                TypeToken<ArrayList<SeparatorItem>>() {}.type)
            suspended.resume(separator)
        }
    }
}

@SuppressLint("ClickableViewAccessibility")
fun getLine(
    page: Int,
    verse: String? = null,
    context: Context,
    words: List<VersesItem>,
    line: Int
): View {
    return TextView(context).apply {
        id =
            line // id is used to get the view by id in the fragment to scroll to it when click on the verse in the menu bottom sheet  (QuranMenuTfser)
        Log.e("idItemVerse", "$line")
        typeface = Typeface.createFromAsset(context.assets, "fonts/quran/p$page.ttf")
        text = verse
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0
        ).apply {
            weight = 2f
            setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        var word = words.flatMap { ayah->
                            ayah.words.apply {
                                forEach {
                                    it.verse_key = ayah.verse_key
                                    it.page = ayah.page
                                }
                            }

                        }
                        Log.e("The event Actions Moment",    "  words: $word  " )

                        val layout: Layout? = (v as TextView).layout
                        val x = event!!.x
                        val y = event.y.toInt()
                        if (layout != null) {
                            val line: Int =
                                layout.getLineForVertical(y) // get the line number from y position
                            var offset: Int = layout.getOffsetForHorizontal(line, x) - 1 //

                            if (offset == -1) {
                                offset = 0
                            }

                            // get the word by offset (position) and filter the words by the word code
                            val htmlEscapedVerse = verse?.replace("<font color=\"#048383\">", "")
                                ?.replace("</font>", "")
                            word.filter { it.code_v1?.contains(htmlEscapedVerse!![offset]) == true }
                                .firstOrNull()
                                ?.let { word ->
                                    word.verse_key?.split(":")?.get(0)?.toInt().let { surah ->
                                        word.verse_key?.split(":")?.get(1)?.toInt()
                                            .let { ayah ->
                                                Log.e("AYAlINE", "ACTION_MOMENT: $surah $ayah")
                                                QuranMenuTfser.newInstance(
                                                    surah ?: 1,
                                                    ayah ?: 1,
                                                    page
                                                ).show(
                                                    (context as AppCompatActivity).supportFragmentManager,
                                                    "")
                                            }
                                    }
                                }
                        }
                    }
                }
                true
            }
        }
        //  setTextIsSelectable(true)
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        setTextColor(context.resources.getColor(R.color.quran_names_light_mode))
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            this,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )


    }
}
suspend fun getWords(page: Int): List<WordItem> {
    return QuranTable.getInstance().quranDao().getAyahLineByPage(page).flatMap { ayah ->
        ayah.words.apply {
            forEach {
                it.verse_key = ayah.verse_key
                it.page = ayah.page
            }
        }
    }
}

fun getSurah(surah: String, context: Context): View {
    return LineSurahHeaderBinding.inflate(LayoutInflater.from(context)).apply {
        text.text =
            surah + "\uE903" // \uE903 is the unicode for the surah icon (賈) in the font file (Surah.ttf) which is used to show the surah name in the separator line
        text.typeface = Typeface.createFromAsset(
            context?.assets,
            "fonts/Surah.ttf"
        ) // set the font to Surah.ttf which contains the surah icon (賈) and the surah name  (الفاتحة)
        context?.resources?.let { text.setTextColor(it.getColor(R.color.quran_names_light_mode)) }

    }.root
}

fun getBlank(context: Context): View {
    return View(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0
        ).apply {
            weight = 1f
        }
    }
}

fun getBismillah(context: Context): View {
    return TextView(context).apply {
        typeface = Typeface.createFromAsset(context.assets, "fonts/Bismillah.ttf")
        text = "﷽"
        setTextIsSelectable(true)
        setTextColor(resources.getColor(R.color.quran_names_light_mode))
        // copy the text to clipboard when long click on it
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            this,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0
        ).apply {
            val surahDao = QuranTable.buildDatabase(context = context!!).quranDao()
            weight =
                1.5f // weight is used to make the view height equal to 1/15 of the screen height
            setPadding(dpToPx(2))
        }
    }
}

fun Any.toArabicNumber(): CharSequence? {
    return this.toString().replace("0", "٠")
        .replace("1", "١")
        .replace("2", "٢")
        .replace("3", "٣")
        .replace("4", "٤")
        .replace("5", "٥")
        .replace("6", "٦")
        .replace("7", "٧")
        .replace("8", "٨")
        .replace("9", "٩")
}

fun parseSurahNamesFromJson(assets: AssetManager, fileName: String): List<SurahName> {
    val inputStream: InputStream = assets.open(fileName)
    val reader = InputStreamReader(inputStream)

    val gson = Gson()
    val surahListType = object : TypeToken<List<SurahName>>() {}.type
    val surahList: List<SurahName> = gson.fromJson(reader, surahListType)

    return surahList
}

fun parseSurahNamesInformationFromJson(
    assets: AssetManager,
    fileName: String
): List<SurahDescription> {
    val inputStream: InputStream = assets.open(fileName)
    val reader = InputStreamReader(inputStream)

    val gson = Gson()
    val surahListType = object : TypeToken<List<SurahDescription>>() {}.type
    val surahList: List<SurahDescription> = gson.fromJson(reader, surahListType)

    return surahList
}

fun parseAyahFromJson(assets: AssetManager, fileName: String): List<Ayah> {
    val inputStream: InputStream = assets.open(fileName)
    val reader = InputStreamReader(inputStream)

    val gson = Gson()
    val ayah = object : TypeToken<List<Ayah>>() {}.type
    val ayahList: List<Ayah> = gson.fromJson(reader, ayah)

    return ayahList
}

fun parseJuzFromJson(assets: AssetManager, fileName: String): List<Juz> {
    val inputStream: InputStream = assets.open(fileName)
    val reader = InputStreamReader(inputStream)

    val gson = Gson()
    val juzListType = object : TypeToken<List<Juz>>() {}.type
    val juzList: List<Juz> = gson.fromJson(reader, juzListType)

    return juzList
}

fun parseTfseerFromJson(assets: AssetManager, fileName: String): List<Tafseer> {
    val inputStream: InputStream = assets.open(fileName)
    val reader = InputStreamReader(inputStream)

    val gson = Gson()
    val tfser = object : TypeToken<List<Tafseer>>() {}.type
    val tfserList: List<Tafseer> = gson.fromJson(reader, tfser)

    return tfserList
}

fun Context.Copy(arabicText: String, context: Context) {
    val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("arabic", arabicText)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "نُسخ النص ", Toast.LENGTH_SHORT).show()
}

fun ArrayList<LanguageString>.getArabic(): String {
    return filter { it.language == "arabic" }.firstOrNull()?.text.toString().replace("،", "")
}

fun ArrayList<LanguageString>.getText(): String {
    return filter { it.language == "arabic" }.firstOrNull()?.text ?: ""
}

// fun Context.shareImage(arabic:String, translation:String, source:String, title:String?=null){ // this function is used to share the ayah image to other apps like whatsapp, facebook, etc.
fun Context.saveImage(bitmap: Bitmap?, onSave: (Uri?) -> Unit) {
    bitmap?.let {
        SaveBitmapToFile.getInstance(this, it) { file ->
            if (file != null) {
                MediaScannerConnection.scanFile(
                    this,
                    arrayOf(file.absolutePath),
                    null
                ) { path: String?, uri: Uri? ->
                    onSave(uri)
                }
            }
            null
        }.execute()
    }
}

fun returnPagesNumber(): ArrayList<Int> {
    val pages = arrayListOf<Int>().apply {
        for (i in 1..604) {
            add(i)
        }
    }.toCollection(arrayListOf())
    return pages
}

fun findCurrentPage(pageNumber: Int?): Int? {
    val soraNumbersQuran = mapOf(
        1 to 0,
        2 to 1,
        3 to 49,
        4 to 76,
        5 to 105,
        6 to 127,
        7 to 150,
        8 to 176,
        9 to 186,
        10 to 207,
        11 to 220,
        12 to 234,
        13 to 248,
        14 to 254,
        15 to 261,
        16 to 266,
        17 to 281,
        18 to 292,
        19 to 304,
        20 to 311,
        21 to 321,
        22 to 331,
        23 to 341,
        24 to 349,
        25 to 358,
        26 to 366,
        27 to 376,
        28 to 384,
        29 to 395,
        30 to 403,
        31 to 410,
        32 to 414,
        33 to 417,
        34 to 427,
        35 to 433,
        36 to 439,
        37 to 445,
        38 to 452,
        39 to 457,
        40 to 466,
        41 to 476,
        42 to 482,
        43 to 489,
        44 to 495,
        45 to 498,
        46 to 501,
        47 to 506,
        48 to 510,
        49 to 514,
        50 to 517,
        51 to 519,
        52 to 522,
        53 to 525,
        54 to 527,
        55 to 530,
        56 to 533,
        57 to 536,
        58 to 541,
        59 to 544,
        60 to 548,
        61 to 550,
        62 to 552,
        63 to 553,
        64 to 555,
        65 to 557,
        66 to 559,
        67 to 561,
        68 to 563,
        69 to 565,
        70 to 567,
        71 to 569,
        72 to 571,
        73 to 573,
        74 to 574,
        75 to 576,
        76 to 577,
        77 to 579,
        78 to 581,
        79 to 582,
        80 to 584,
        81 to 585,
        82 to 586,
        83 to 586,
        84 to 588,
        85 to 589,
        86 to 590,
        87 to 590,
        88 to 591,
        89 to 592,
        90 to 593,
        91 to 594,
        92 to 594,
        93 to 595,
        94 to 595,
        95 to 596,
        96 to 596,
        97 to 597,
        98 to 597,
        99 to 598,
        100 to 598,
        101 to 599,
        102 to 599,
        103 to 600,
        104 to 600,
        105 to 600,
        106 to 601,
        107 to 601,
        108 to 601,
        109 to 602,
        110 to 602,
        111 to 602,
        112 to 603,
        113 to 603,
        114 to 603
    )
    if (pageNumber in soraNumbersQuran) // if pageNumber in map
        return soraNumbersQuran[pageNumber] // return value of pageNumber
    else
        return null
}
fun String.toArabicFormat(): String {
    return this.replace("Sun", "الأحد")
        .replace("Mon", "الإثنين")
        .replace("Tue", "الثلاثاء")
        .replace("Wed", "الأربعاء")
        .replace("Thu", "الخميس")
        .replace("Fri", "الجمعة")
        .replace("Sat", "السبت")
        .replace("Jan", "يناير")
        .replace("Feb", "فبراير")
        .replace("Mar", "مارس")
        .replace("Apr", "أبريل")
        .replace("May", "مايو")
        .replace("Jun", "يونيو")
        .replace("Jul", "يوليو")
        .replace("Aug", "أغسطس")
        .replace("Sep", "سبتمبر")
        .replace("Oct", "أكتوبر")
        .replace("Nov", "نوفمبر")
        .replace("Dec", "ديسمبر")
        .replace("AM", "صَبَاحًا")
        .replace("PM", "مَسَاءًا")
        .replace("0", "٠")
        .replace("1", "١")
        .replace("2", "٢")
        .replace("3", "٣")
        .replace("4", "٤")
        .replace("5", "٥")
        .replace("6", "٦")
        .replace("7", "٧")
        .replace("8", "٨")
        .replace("9", "٩")


}
