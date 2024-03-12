package com.elmaref.ui.salat.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elmaref.R
import com.elmaref.data.model.enums.RingType
import com.elmaref.data.model.salat.SalatTime
import com.elmaref.databinding.ItemHomePraytimeBinding
import com.quranscreen.constants.LocaleConstants

class SalatTimeAdapter(val prayerViewList: List<SalatTime>) :
    RecyclerView.Adapter<SalatTimeAdapter.PrayTimeViewHolder>() {

    lateinit var context: Context

    class PrayTimeViewHolder(var viewDataBinding: ItemHomePraytimeBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayTimeViewHolder {
        val viewDataBinding: ItemHomePraytimeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_home_praytime,
            parent,
            false
        )
        context = parent.context
        return PrayTimeViewHolder(viewDataBinding)
    }

    override fun getItemCount(): Int {
        return prayerViewList.size
    }

    override fun onBindViewHolder(holder: PrayTimeViewHolder, position: Int) {
        val prayTime = prayerViewList[position]
        holder.viewDataBinding.title.text = prayTime.prayTimeName
        holder.viewDataBinding.icon.setImageResource(prayTime.prayIcon!!)
        holder.viewDataBinding.praytime.text = prayTime.prayTime
        holder.viewDataBinding.until.text = prayTime.prayPassedSalat

        // check of ring type and change icon
        when (prayTime.ringType) {
            RingType.NOTIFICATION -> holder.viewDataBinding.ring.setImageResource(R.drawable.ic_notification)
            RingType.SILENT -> holder.viewDataBinding.ring.setImageResource(R.drawable.ic_volume_off)
            RingType.SOUND -> holder.viewDataBinding.ring.setImageResource(R.drawable.ic_volume_up)
        }

        // add click listener to ring
        holder.viewDataBinding.ring.setOnClickListener {
            when (prayTime.ringType) {
                RingType.NOTIFICATION -> {
                    prayTime.ringType = RingType.SILENT

                    holder.viewDataBinding.ring.setImageResource(R.drawable.ic_volume_off)
                }

                RingType.SILENT -> {
                    prayTime.ringType = RingType.SOUND
                    holder.viewDataBinding.ring.setImageResource(R.drawable.ic_volume_up)
                }

                RingType.SOUND -> {
                    prayTime.ringType = RingType.NOTIFICATION
                    holder.viewDataBinding.ring.setImageResource(R.drawable.ic_notification)
                }
            }
        }

        // check in item 2 and change background
        if (position==2){

        }

    }

}