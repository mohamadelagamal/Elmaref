package com.elmaref.ui.quran.saved.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.elmaref.R
import com.elmaref.data.model.quran.mark.QuranBookMark
import com.elmaref.data.room.tables.QuranTable
import com.elmaref.databinding.ItemSavedAyahQuarnBinding
import com.elmaref.databinding.ItemSurahNameBinding
import com.elmaref.ui.quran.paged.functions.toArabicNumber
import com.quranscreen.utils.io
import com.quranscreen.utils.main

class SavedAyahQuranAdapter(val savedAyahList:List<QuranBookMark>?=null)
    :RecyclerView.Adapter<SavedAyahQuranAdapter.ViewHolder>() {


    class ViewHolder(var viewDataBinding: ItemSavedAyahQuarnBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        // init the views from item_surah_name.xml

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding : ItemSavedAyahQuarnBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_saved_ayah_quarn,parent,false)
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount(): Int {
        return savedAyahList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = savedAyahList?.get(position)
        holder.viewDataBinding.surahName.text = itemData?.surahName
        holder.viewDataBinding.ayahNumber.text = "رَقْم اَلآيَة : ${itemData?.verseNumber.toString().toArabicNumber()}"

        holder.viewDataBinding.cardView.setOnClickListener {
            val dataBase = QuranTable.getInstance()
            io {
                dataBase.quranBookMarkDao().deleteBookmark(itemData?.idString.toString(),itemData?.type.toString())
                main {
                    Toast.makeText(holder.viewDataBinding.root.context, "تم حذف الآية", Toast.LENGTH_SHORT).show()
                }
            }
        }
        holder.viewDataBinding.itemSoraName.setOnClickListener {
            onItemClickListener?.onItemClick(itemData?.pageNumber)
        }
    }
    var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(pageNumber:Int?)
    }
}