package com.elmaref.ui.salat.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elmaref.R
import com.elmaref.data.model.enums.RingType
import com.elmaref.data.model.salat.PrayerTime
import com.elmaref.data.model.salat.SalatTime
import com.elmaref.databinding.ItemHomePraytimeBinding
import kotlinx.coroutines.channels.ticker

class SalatTimeAdapter(val prayerViewList: List<SalatTime>) :
    RecyclerView.Adapter<SalatTimeAdapter.PrayTimeViewHolder>() {

    lateinit var context: Context
    var updateData:List<PrayerTime> ?=null
    var updateData2:List<String?> ?=null


    class PrayTimeViewHolder(var viewDataBinding: ItemHomePraytimeBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
            fun bind(prayTime: SalatTime) {
                viewDataBinding.prayBinding = prayTime
                viewDataBinding.invalidateAll()
            }
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
        Log.e("prayerTime", "${position}")
        val prayTime = prayerViewList[position]

        holder.bind(prayerViewList[position])


        holder.viewDataBinding.until.text= (updateData2?.get(position) ?: "تحميل...").toString()

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
    }

    fun changeData(data: List<String?>){
        updateData2=data
        notifyDataSetChanged()
    }
}