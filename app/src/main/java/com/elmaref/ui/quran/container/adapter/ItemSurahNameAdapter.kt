package com.example.quran.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elmaref.R
import com.elmaref.ui.app.MyApplication
import com.elmaref.databinding.ItemSurahNameBinding
import com.elmaref.ui.quran.paged.functions.toArabicNumber

// create a itemsurahnameadtaper thats inflate the item_surah_name.xml and give it list of surahname
// crate the class now
class ItemSurahNameAdapter(
  //  val surahNameList2: List<SurahName>?=null,
    val context: Context,
    //val surahInfoItem2: List<SurahDescription> ?=null
) :
    RecyclerView.Adapter<ItemSurahNameAdapter.ViewHolder>() {

    // create the view holder class
    val surahNameList = MyApplication.surahNameData
    class ViewHolder(var viewDataBinding: ItemSurahNameBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        // init the views from item_surah_name.xml

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding :ItemSurahNameBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_surah_name,parent,false)
        return ViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val surahInfoItem = MyApplication.surahDescription

        val surahName = surahNameList?.get(position)
        holder.viewDataBinding.tvSoraNum.text = surahName?.id.toString().toArabicNumber()
        // bind surahnameer to text in the translation data class that has english language
        holder.viewDataBinding.surahNameEn.text = surahName?.name
        // bind surahnamear to text in the translation data class that has arabic language
        holder.viewDataBinding.tvSoraName.text = surahName?.translation?.get(2)?.text

        holder.viewDataBinding.surahNameDisc.text = surahName?.translation?.get(1)?.text  // explain why 1 ?
//         holder.surahNameEr.text = surahName.translation.find { it.language == "english" }?.text
//         holder.surahNameAr.text = surahName.translation.find { it.language == "arabic" }?.text

        holder.viewDataBinding.surahNameDiscAr.text =
            " اَلْآيَاتِ :  ${surahInfoItem?.get(position)?.count?.toArabicNumber()} | اَلْمَكَانَ : ${surahInfoItem?.get(position)?.place}"
    }


    override fun getItemCount(): Int {
        return surahNameList?.size !!
    }
}
