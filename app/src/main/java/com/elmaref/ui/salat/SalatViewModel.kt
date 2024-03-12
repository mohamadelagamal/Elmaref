package com.elmaref.ui.salat

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.elmaref.ui.quran.paged.functions.toArabicFormat
import com.elmaref.ui.quran.paged.functions.toArabicNumber
import com.elmaref.utils.FileUtils
import com.example.muslim.ui.base.activity.BaseViewModel
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import java.text.SimpleDateFormat
import java.time.InstantSource.tick
import java.util.Calendar
import java.util.Locale

class SalatViewModel :BaseViewModel<Navigator>() {


    var ringFajr = 2
    var ringDhuhr = 2
    var ringAsr = 2
    var ringMaghrib = 2
    var ringIsya = 2


    fun getGregorianDate(): String {
        return SimpleDateFormat("EEE,d MMM , yyyy").format(Calendar.getInstance().time).toArabicFormat()
    }
    fun getHijriDate(): String {
        val cal = UmmalquraCalendar()
        cal.time = Calendar.getInstance().time
        val timer= "${cal.get(Calendar.DAY_OF_MONTH)} ${
            cal.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale("ar"))} , " +
                " ${cal.get(Calendar.YEAR)}"
        return timer.toArabicNumber().toString()
    }



}


