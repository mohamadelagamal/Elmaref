package com.elmaref.ui.salat

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elmaref.data.model.salat.PrayerTime
import com.elmaref.ui.quran.paged.functions.toArabicFormat
import com.elmaref.ui.quran.paged.functions.toArabicNumber
import com.elmaref.utils.FileUtils.tick
import com.elmaref.utils.praytimes.PrayerTimeHelper.getCurrentPrayerTimeString
import com.elmaref.utils.Prefs
import com.elmaref.utils.changeFormat
import com.elmaref.utils.convertTo12HourFormat
import com.elmaref.utils.praytimes.PrayerTimeHelper
import com.elmaref.utils.praytimes.PrayerTimeHelper.countTimeLight
import com.elmaref.utils.praytimes.PrayerTimeHelper.getPrayerTime
import com.elmaref.utils.praytimes.PrayerTimeHelper.getTimeUntilNextPrayerString
import com.elmaref.utils.removeZeroFromLeft
import com.example.muslim.ui.base.activity.BaseViewModel
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.quranscreen.constants.LocaleConstants
import com.quranscreen.constants.LocaleConstants.FAJR
import com.quranscreen.constants.LocaleProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SalatViewModel : BaseViewModel<Navigator>() {


    var ringFajr = 2
    var ringDhuhr = 2
    var ringAsr = 2
    var ringMaghrib = 2
    var ringIsya = 2

    var prayerTime = MutableLiveData(Prefs.praytime)

    // text prayer time
    var textPrayerTime = ObservableField("Loading...")

    var textUntil = ObservableField("Loading...")

    var untilFajr = ObservableField("")
    var untilDhuhr = MutableLiveData("")
    var untilAsr = MutableLiveData("")
    var untilMaghrib = MutableLiveData("")
    var untilIsya = MutableLiveData("")


    var praytimeFajr = MutableLiveData("")
    var praytimeDhuhr = MutableLiveData("")
    var praytimeAsr = MutableLiveData("")
    var praytimeMaghrib = MutableLiveData("")
    var praytimeIsya = MutableLiveData("")

    fun getGregorianDate(): String {
        return SimpleDateFormat("EEE,d MMM , yyyy").format(Calendar.getInstance().time)
            .toArabicFormat()
    }

    fun getHijriDate(): String {
        val cal = UmmalquraCalendar()
        cal.time = Calendar.getInstance().time
        val timer = "${cal.get(Calendar.DAY_OF_MONTH)} ${
            cal.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale("ar")
            )
        } , " +
                " ${cal.get(Calendar.YEAR)}"
        return timer.toArabicNumber().toString()
    }


    var newtimer: CountDownTimer? = null
    val time = MutableLiveData(Calendar.getInstance().time)
    var isPrayerTimeNeedsPermission = MutableLiveData(false)
    var isPrayerTimeHidden = MutableLiveData(false)
    val address = ObservableField(Prefs.userCity)


    fun buildTimer(context: Context) {
        newtimer?.cancel()
        newtimer = tick(Long.MAX_VALUE, 1000) {
            if (Calendar.getInstance().get(Calendar.MINUTE) != Calendar.getInstance()
                    .apply { time = this@SalatViewModel.time.value!! }
                    .get(Calendar.MINUTE)
            ) {
                time.value = Calendar.getInstance().apply {
                    add(Calendar.MINUTE, 1)
                }.time
            }
            buildPrayers(Prefs.praytime)
            textPrayerTime.set(
                Prefs.praytime.getCurrentPrayerTimeString()?.convertTo12HourFormat()
                    ?.toArabicNumber().removeZeroFromLeft().toString()
            )
            address.set(Prefs.praytime.address)

            val changeFormat = Prefs.praytime.getTimeUntilNextPrayerString().changeFormat()
            textUntil.set(changeFormat)

        }
        newtimer?.start()
    }


    fun buildPrayers(prayer: PrayerTime) {
        untilFajr.set(
            PrayerTimeHelper.countTimeLight(
                prayer.fajr ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.FAJR)
            )
        )
        praytimeFajr.value = prayer.fajr

        untilDhuhr.value =
            PrayerTimeHelper.countTimeLight(
                prayer.dhuhr ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.DHUHR)
            )
        praytimeDhuhr.value = prayer.dhuhr

        untilAsr.value =
            PrayerTimeHelper.countTimeLight(
                prayer.asr ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.ASR)
            )
        praytimeAsr.value = prayer.asr

        untilMaghrib.value =
            PrayerTimeHelper.countTimeLight(
                prayer.maghrib ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.MAGHRIB)
            )
        praytimeMaghrib.value = prayer.maghrib

        untilIsya.value =
            PrayerTimeHelper.countTimeLight(
                prayer.isya ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.ISYA)
            )
        praytimeIsya.value = prayer.isya

    }

}





