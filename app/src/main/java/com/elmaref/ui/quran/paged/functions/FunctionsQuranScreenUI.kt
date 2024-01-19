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
import android.text.SpannableString
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
                        val layout: Layout? = (v as TextView).layout
                        val x = event!!.x
                        val y = event.y.toInt()
                        if (layout != null) {
                            val line: Int = layout.getLineForVertical(y) // get the line number from y position
                            var offset: Int = layout.getOffsetForHorizontal(line, x) - 1 //
                            if (offset == -1) {
                                offset = 0
                            }

                            // get the word by offset (position) and filter the words by the word code
                            val htmlEscapedVerse = verse?.replace("<font color=\"#048383\">", "")
                                ?.replace("</font>", "")
                            words.filter {
                                it.words.firstOrNull()?.code_v1?.contains(
                                    htmlEscapedVerse?.get(offset)!!
                                ) == true
                            }
                                .firstOrNull()
                                ?.let { word ->

                                    // scroll to the selected word
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
                                                    ""
                                                )

                                                offset = 0
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
fun parseSurahNamesInformationFromJson(assets: AssetManager, fileName: String): List<SurahDescription> {
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
fun Context.saveImage(bitmap: Bitmap?, onSave:(Uri?)->Unit){
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
