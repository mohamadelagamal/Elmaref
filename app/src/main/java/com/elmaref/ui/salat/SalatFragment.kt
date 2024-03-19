package com.elmaref.ui.salat

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.elmaref.R
import com.elmaref.data.model.salat.PrayerTime
import com.elmaref.databinding.FragmentSalatBinding
import com.elmaref.ui.base.fragment.BaseFragment
import com.elmaref.ui.salat.options.compass.CompassActivity
import com.elmaref.ui.salat.options.setting.SettingActivity
import com.elmaref.ui.salat.options.us.AboutUsActivity
import com.elmaref.data.model.salat.SalatTime
import com.elmaref.ui.quran.paged.functions.toArabicNumber
import com.elmaref.ui.salat.adapter.SalatTimeAdapter
import com.elmaref.utils.FileUtils
import com.elmaref.utils.Prefs
import com.elmaref.utils.changeFormat
import com.elmaref.utils.convertTo12HourFormat
import com.elmaref.utils.praytimes.PrayerTimeHelper
import com.elmaref.utils.removeZeroFromLeft
import com.quranscreen.constants.LocaleConstants
import com.quranscreen.constants.LocaleProvider
import java.util.Calendar


class SalatFragment : BaseFragment<FragmentSalatBinding, SalatViewModel>(), Navigator {

    lateinit var adapter: SalatTimeAdapter
    val prayerTime: PrayerTime by lazy {
        Prefs.praytime
    }
    var newtimer: CountDownTimer? = null

    override fun getLayoutID(): Int {
        return R.layout.fragment_salat
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SalatViewModel::class.java)
        viewModel.navigator = this
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.salat = viewModel


        viewModel.buildTimer(requireContext())
        // get current time

        val quibla = requireActivity().findViewById<ImageView>(R.id.more_vert)

        quibla.setOnClickListener {

            // show menu option in fragment
            val popup = androidx.appcompat.widget.PopupMenu(requireContext(), quibla)
            // show menu from left to right

            // make popup menu show icon in menu option in right side
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popup)
                        val classPopupHelper =
                            Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons: java.lang.reflect.Method = classPopupHelper.getMethod(
                            "setForceShowIcon", Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }



            popup.inflate(R.menu.option_menu_shalat)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.qibla -> {
                        movingActivity(CompassActivity::class.java)
                        true
                    }

                    R.id.settings -> {
                        movingActivity(SettingActivity::class.java)
                        true
                    }

                    R.id.about -> {
                        movingActivity(AboutUsActivity::class.java)
                        true
                    }

                    else -> false
                }
            }
            popup.show()
        }


        val list = ArrayList<SalatTime>(
            listOf(
                SalatTime(
                    LocaleConstants.FAJR,
                    prayerTime.fajr?.convertTo12HourFormat()?.toArabicNumber()?.removeZeroFromLeft()
                        .toString(),
                    R.drawable.ic_fajr_primary,
                    viewModel.untilFajr.get()?.changeFormat(),
                    viewModel.ringFajr
                ),
                SalatTime(
                    LocaleConstants.DHUHR,
                    prayerTime.dhuhr?.convertTo12HourFormat()?.toArabicNumber()
                        ?.removeZeroFromLeft().toString(),
                    R.drawable.ic_dhuhr_primary,
                    "في 3 ساعات",
                    viewModel.ringDhuhr
                ),
                SalatTime(
                    LocaleConstants.ASR,
                    prayerTime.asr?.convertTo12HourFormat()?.toArabicNumber().removeZeroFromLeft()
                        ?.toString(),
                    R.drawable.ic_asr_primary,
                    "في 6 ساعات",
                    viewModel.ringAsr
                ),
                SalatTime(
                    LocaleConstants.MAGHRIB,
                    prayerTime.maghrib?.convertTo12HourFormat()?.toArabicNumber()
                        ?.removeZeroFromLeft()?.toString(),
                    R.drawable.ic_maghrib_primary,
                    "في 9 ساعات",
                    viewModel.ringMaghrib
                ),
                SalatTime(
                    LocaleConstants.ISYA,
                    prayerTime.isya?.convertTo12HourFormat()?.toArabicNumber()?.removeZeroFromLeft()
                        .toString(),
                    R.drawable.ic_isya_primary,
                    "في 11 ساعة",
                    viewModel.ringIsya
                )
            )
        )
        adapter = SalatTimeAdapter(list)
        viewDataBinding.praytimeContainer.adapter = adapter

        newtimer?.cancel()
        newtimer = FileUtils.tick(Long.MAX_VALUE, 1000) {
            if (Calendar.getInstance().get(Calendar.MINUTE) != Calendar.getInstance()
                    .apply { time = viewModel.time.value!! }
                    .get(Calendar.MINUTE)
            ) {
                viewModel.time.value = Calendar.getInstance().apply {
                    add(Calendar.MINUTE, 1)
                }.time
            }
            var fajr = PrayerTimeHelper.countTimeLight(
                Prefs.praytime.fajr ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.FAJR)
            )
            var dhuhr = PrayerTimeHelper.countTimeLight(
                Prefs.praytime.dhuhr ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.DHUHR)
            )
            var asr = PrayerTimeHelper.countTimeLight(
                Prefs.praytime.asr ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.ASR)
            )
            var maghrib = PrayerTimeHelper.countTimeLight(
                Prefs.praytime.maghrib ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.MAGHRIB)
            )
            var isya = PrayerTimeHelper.countTimeLight(
                Prefs.praytime.isya ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.ISYA)
            )
            var sunrah = PrayerTimeHelper.countTimeLight(
                Prefs.praytime.sunrise ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.SUNRISE)
            )
            var pray = listOf(
                fajr.changeFormat(),
                dhuhr.changeFormat(),
                asr.changeFormat(),
                maghrib.changeFormat(),
                isya.changeFormat(),
                sunrah.changeFormat()
            )
            adapter.changeData(pray)
        }
        newtimer?.start()


    }


    fun movingActivity(activity: Class<*>) {
        val intent = Intent(requireContext(), activity)
        startActivity(intent)
        requireActivity().overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }

}