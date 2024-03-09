package com.elmaref.ui.salat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.elmaref.R
import com.elmaref.databinding.FragmentSalatBinding
import com.elmaref.ui.base.fragment.BaseFragment
import com.elmaref.ui.salat.options.compass.CompassActivity
import com.elmaref.ui.salat.options.setting.SettingActivity
import com.elmaref.ui.salat.options.us.AboutUsActivity


class SalatFragment : BaseFragment<FragmentSalatBinding,SalatViewModel>(),Navigator {
    override fun getLayoutID(): Int {
        return R.layout.fragment_salat
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SalatViewModel::class.java)
        viewModel.navigator= this
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.salat=viewModel
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

    }

    fun movingActivity(activity:Class<*>){
        val intent = Intent(requireContext(), activity)
        startActivity(intent)
        requireActivity().overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }


}