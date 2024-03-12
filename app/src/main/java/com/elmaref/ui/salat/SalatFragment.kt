package com.elmaref.ui.salat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.elmaref.R
import com.elmaref.databinding.FragmentSalatBinding
import com.elmaref.databinding.ItemHomePraytimeBinding
import com.elmaref.ui.base.fragment.BaseFragment
import com.elmaref.ui.salat.options.compass.CompassActivity
import com.elmaref.ui.salat.options.setting.SettingActivity
import com.elmaref.ui.salat.options.us.AboutUsActivity
import com.elmaref.data.model.salat.SalatTime
import com.elmaref.ui.salat.adapter.SalatTimeAdapter
import com.quranscreen.constants.LocaleConstants
import com.quranscreen.constants.LocaleConstants.locale


class SalatFragment : BaseFragment<FragmentSalatBinding, SalatViewModel>(), Navigator {

    lateinit var adapter: SalatTimeAdapter
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
                SalatTime(LocaleConstants.FAJR, "٤:٣٠", R.drawable.ic_fajr_primary, "اجتاز",viewModel.ringFajr),
                SalatTime(LocaleConstants.DHUHR, "١٢:٣٠", R.drawable.ic_dhuhr_primary, "في 3 ساعات",viewModel.ringDhuhr),
                SalatTime(LocaleConstants.ASR, "٣:٣٠", R.drawable.ic_asr_primary, "في 6 ساعات",viewModel.ringAsr),
                SalatTime(LocaleConstants.MAGHRIB, "٦:٤", R.drawable.ic_maghrib_primary, "في 9 ساعات",viewModel.ringMaghrib),
                SalatTime(LocaleConstants.ISYA, "٨:٣٠", R.drawable.ic_isya_primary, "في 11 ساعة",viewModel.ringIsya)
            )
        )
        adapter = SalatTimeAdapter(list)
        viewDataBinding.praytimeContainer.adapter = adapter


    }

    fun movingActivity(activity: Class<*>) {
        val intent = Intent(requireContext(), activity)
        startActivity(intent)
        requireActivity().overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }

    fun configurePraytimes() {
        addPraytime(
            LocaleConstants.FAJR.locale(),
            R.drawable.ic_fajr_primary,

            )
        addPraytime(
            LocaleConstants.DHUHR.locale(),
            R.drawable.ic_dhuhr_primary,

            )
        addPraytime(
            LocaleConstants.ASR.locale(),
            R.drawable.ic_asr_primary,

            )
        addPraytime(
            LocaleConstants.MAGHRIB.locale(),
            R.drawable.ic_maghrib_primary,

            )
        addPraytime(
            LocaleConstants.ISYA.locale(),
            R.drawable.ic_isya_primary,
        )
    }

    fun addPraytime(
        title: String,
        icon: Int,
    ) {
        val view = ItemHomePraytimeBinding.inflate(LayoutInflater.from(requireActivity())).apply {
            this.title.text = title
            this.icon.setImageResource(icon)
            this.praytime
        }


        //   view.ring.configureRing(title)

        viewDataBinding?.praytimeContainer?.addView(view.root)
    }


}