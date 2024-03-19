package com.elmaref.utils.praytimes

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import com.elmaref.data.model.salat.PrayerTime
import com.elmaref.utils.Prefs
import com.google.android.gms.maps.model.LatLng
import com.quranscreen.constants.LocaleConstants.ASR
import com.quranscreen.constants.LocaleConstants.DHUHR
import com.quranscreen.constants.LocaleConstants.FAJR
import com.quranscreen.constants.LocaleConstants.HOUR
import com.quranscreen.constants.LocaleConstants.ISYA
import com.quranscreen.constants.LocaleConstants.LEFT_UNTIL
import com.quranscreen.constants.LocaleConstants.MAGHRIB
import com.quranscreen.constants.LocaleConstants.MINUTE
import com.quranscreen.constants.LocaleProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object PrayerTimeHelper {

    @SuppressLint("CheckResult")
    suspend fun getPrayerTime(ctx: Context): PrayerTime {
        var isResumed = false
        return suspendCoroutine { suspended ->
            SingleLocationProvider.requestSingleUpdate(ctx) { location ->
                if (!isResumed) {
                    suspended.resume(
                        getCurrentPrayerTimeString(
                            ctx,
                            LatLng(location.latitude.toDouble(), location.longitude.toDouble())
                        )
                    )
                    isResumed = true
                }
            }
        }
    }

    fun countTimeLight(endTime: String, prayer: String): String {
        val cal = Calendar.getInstance()
        val nowHour = cal.get(Calendar.HOUR_OF_DAY)
        val nowMin = cal.get(Calendar.MINUTE)
        val m = Pattern.compile("(\\d{2}):(\\d{2})").matcher(endTime)
        require(m.matches()) { "Invalid time format: $endTime" }
        val endHour = Integer.parseInt(m.group(1)!!)
        val endMin = Integer.parseInt(m.group(2)!!)
        require(!(endHour >= 24 || endMin >= 60)) { "Invalid time format: $endTime" }
        var minutesLeft = endHour * 60 + endMin - (nowHour * 60 + nowMin)
        if (minutesLeft < 0)
            minutesLeft += 24 * 60 // Time passed, so time until 'end' tomorrow
        val hours = minutesLeft / 60
        val minutes = minutesLeft - hours * 60
//            return "${StringProvider.getInstance().getString(TIME_LEFT_UNTIL)}$prayer: " +
//                    "$hours ${StringProvider.getInstance().getString(HOUR)} " +
//                    "$minutes ${StringProvider.getInstance().getString(MINUTE)}"

        val minutesString =
            if (minutes != 0) "$minutes ${LocaleProvider.getInstance().getString(MINUTE)} " else ""

        return hours.toString() + " ${
            LocaleProvider.getInstance().getString(HOUR)
        } $minutesString${LocaleProvider.getInstance().getString(LEFT_UNTIL)} " + prayer
    }

    fun PrayerTime.getTimeUntilNextPrayerString(): String {
        val now = SimpleDateFormat("HH:mm").format(Calendar.getInstance().time)
        return when {
            now < fajr!! -> countTimeLight(fajr!!, LocaleProvider.getInstance().getString(FAJR))
            now < dhuhr!! -> countTimeLight(dhuhr!!, LocaleProvider.getInstance().getString(DHUHR))
            now < asr!! -> countTimeLight(asr!!, LocaleProvider.getInstance().getString(ASR))
            now < maghrib!! -> countTimeLight(
                maghrib!!,
                LocaleProvider.getInstance().getString(MAGHRIB)
            )
            now < isya!! -> countTimeLight(isya!!, LocaleProvider.getInstance().getString(ISYA))
            else -> countTimeLight(fajr!!, LocaleProvider.getInstance().getString(FAJR))
        }
    }

    fun PrayerTime.getCurrentPrayerTimeString(): String? {
        val now = SimpleDateFormat("HH:mm").format(Calendar.getInstance().time)
        return when {
            now < fajr!! -> fajr
            now < dhuhr!! -> dhuhr
            now < asr!! -> asr
            now < maghrib!! -> maghrib
            now < isya!! -> isya
            else -> {
                fajr
            }
        }
    }

    // get prayer times using PrayTime and user location
    fun getCurrentPrayerTimeString(ctx: Context, latLng: LatLng): PrayerTime {
        Prefs.userCoordinates = latLng
        var address: String? = "Cannot get location"
        try {
            if (isNetworkAvailable(ctx)) {
                val gcd = Geocoder(ctx, Locale.getDefault())
                val addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses!!.size > 0) {
                    address = addresses[0].locality
                    Prefs.userCity = address
                }
            } else {
                if (Prefs.userCity != "") {
                    address = Prefs.userCity
                }
            }
        } catch (e: Exception) {
        }

        // PrayTimeScript is a class that is used to get the prayer times of the user location using PrayTime library
        val prayers = PrayTimeScript()
        // seTimeFormat is a function that is used to set the time format of the prayer times to 24 hours
        prayers.setTimeFormat(prayers.Time24)
        // setCalcMethod is a function that is used to set the calculation method of the prayer times to the user's preferred calculation method
        prayers.setCalcMethod(Prefs.calculationMethod)
        // setAsrJuristic is a function that is used to set the asr juristic method of the prayer times to the user's preferred asr juristic method
        prayers.setAsrJuristic(Prefs.asrJuristic)
        // setAdjustHighLats is a function that is used to set the adjustment method of the higher latitudes of the prayer times to the user's preferred adjustment method
        prayers.setAdjustHighLats(Prefs.higherLatitudes)
        val offsets = intArrayOf(
            Prefs.fajrOffset,
            2,
            Prefs.dhuhrOffset,
            Prefs.asrOffset,
            2,
            Prefs.maghribOffset,
            Prefs.isyaOffset
        )
        prayers.tune(offsets)
        val mCalendar = GregorianCalendar()
        val mTimeZone = mCalendar.timeZone
        val mGMTOffset = mTimeZone.rawOffset
        val now = Date()
        val cal = Calendar.getInstance()
        cal.time = now
        val prayerTimes = prayers.getPrayerTimes(
            cal,
            latLng.latitude,
            latLng.longitude,
            TimeUnit.HOURS.convert(mGMTOffset.toLong(), TimeUnit.MILLISECONDS).toDouble()
        )
        val prayerTime = PrayerTime(
            address,
            prayerTimes[0],
            prayerTimes[1],
            prayerTimes[2],
            prayerTimes[3],
            prayerTimes[4],
            prayerTimes[5],
            prayerTimes[6]
        )
        Prefs.praytime = prayerTime
        return prayerTime
    }

    fun isNetworkAvailable(ctx: Context): Boolean {
        val connectivityManager =
            ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}