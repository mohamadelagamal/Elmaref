package com.elmaref.ui.shalat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.elmaref.R
import com.elmaref.ui.shalat.compass.CompassActivity


class ShalatFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shalat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                        val intent = Intent(requireContext(), CompassActivity::class.java)
                        startActivity(intent)
                        requireActivity().overridePendingTransition(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        )

                        true
                    }
                    R.id.settings -> {
                        Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.about -> {
                        Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popup.show()

        }

    }

       override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.qibla-> {
                    Toast.makeText(requireContext(), "Search", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.settings -> {
                    Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.about -> {
                    Toast.makeText(requireContext(), "about us", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }




}