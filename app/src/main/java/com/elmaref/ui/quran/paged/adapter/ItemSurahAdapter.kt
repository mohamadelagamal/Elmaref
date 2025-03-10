package com.elmaref.ui.quran.paged.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elmaref.R
import com.elmaref.ui.app.MyApplication
import com.elmaref.databinding.ItemSurahPageBinding
import com.elmaref.ui.quran.paged.functions.getBismillah
import com.elmaref.ui.quran.paged.functions.getBlank
import com.elmaref.ui.quran.paged.functions.getLine
import com.elmaref.ui.quran.paged.functions.getSeparatorByLineAndPage
import com.elmaref.ui.quran.paged.functions.getSurah
import com.elmaref.ui.quran.paged.functions.getVerseByLine
import com.elmaref.ui.quran.paged.functions.toArabicNumber
import com.quranscreen.utils.io
import com.quranscreen.utils.main


class ItemSurahAdapter(
    val pagesQuranPager: List<Int>,
    val context: Context,
) : RecyclerView.Adapter<ItemSurahAdapter.ItemSurahViewHolder>() {
    class ItemSurahViewHolder(val viewDataBinding: ItemSurahPageBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        val layoutItemQuran = viewDataBinding.container
        val pageNumberView = viewDataBinding.surahNumber
        val juzNumber = viewDataBinding.juzNumber
        val surahName = viewDataBinding.surahName

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSurahViewHolder {
        val viewDataBinding: ItemSurahPageBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_surah_page, parent, false
            )

        return ItemSurahViewHolder(viewDataBinding)
    }

    override fun getItemCount(): Int = pagesQuranPager.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(binding: ItemSurahViewHolder, itemAdapterNumber: Int) {

        val versesItem =
            MyApplication.quranValue.filter { it.page == pagesQuranPager[itemAdapterNumber] }
        val words = versesItem.flatMap { it.words }
        val juz = MyApplication.juzData
        val surahName = MyApplication.surahNameData

        val juzFilter =
            juz?.filter { it.start_page!! <= pagesQuranPager[itemAdapterNumber] && it.end_page!! >= pagesQuranPager[itemAdapterNumber] }

        main { // main{} is used to run the code in main thread because we can't update the UI from IO thread
            binding.pageNumberView.text =
                pagesQuranPager[itemAdapterNumber].toString().toArabicNumber()

            if (juz != null) {
                if (juzFilter!!.isNotEmpty()) {
                    // juzNumber[0] is the first juz in the page and juzNumber[1] is the last juz in the page
                    binding.juzNumber.text = juzFilter[0].juz_name_arabic
                } else {
                    binding.juzNumber.visibility = View.GONE
                }

            } else {
                binding.juzNumber.visibility = View.GONE
            }
            // surah name
            binding.surahName?.text = surahName?.filter {
                it.id == versesItem.map { it.verse_key }.firstOrNull()?.substringBefore(":")
                    ?.toInt()
            }?.firstOrNull()?.translation?.get(2)?.text

            for (i in 1..15) {
                val verse = words?.let { getVerseByLine(i, it) }
                if (verse!!.isBlank()) {
                    getSeparatorByLineAndPage(context, itemAdapterNumber + 1, i)?.let {
                        if (it.bismillah == true) {
                            val bismillahView = getBismillah(context)
                            bismillahView.setOnClickListener {
                                // Handle click event
                                // Get Ayah using x and y
                                Log.e("ItemSurahAdapter", "onBindViewHolder: ")
                            }
                            binding.layoutItemQuran.addView(bismillahView)
                        } else if (it.surah != null) {
                            val surahView = getSurah(it.unicode.toString(), context)
                            surahView.setOnClickListener {
                                // Handle click event
                                // Get Ayah using x and y
                                Log.e("ItemSurahAdapter", "onBindViewHolder: ")
                            }
                            binding.layoutItemQuran.addView(surahView)
                        }
                    } ?: kotlin.run {
                        val blankView = getBlank(context)
                        blankView.setOnClickListener {
                            // Handle click event
                            // Get Ayah using x and y
                            Log.e("ItemSurahAdapter", "onBindViewHolder: ")
                        }
                        binding.layoutItemQuran.addView(blankView)
                    }

                } else {
                    val lineView = getLine(
                        verse = verse,
                        words = versesItem,
                        context = context,
                        page = itemAdapterNumber + 1,
                        line = i
                    )
                    lineView.setOnClickListener {
                        // Handle click event
                        // Get Ayah using x and y
                        Log.e("ItemSurahAdapter", "onBindViewHolder: ")
                    }
                    binding.layoutItemQuran.addView(lineView)
                }
            }
        }
      // when you move to left use anmation left
//        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_right)
//        binding.itemView.startAnimation(animation)

}


// check if the item is finished or not to remove all views and add new views
override fun onViewRecycled(holder: ItemSurahViewHolder) { // this method is called when the item is finished
    super.onViewRecycled(holder)
    holder.layoutItemQuran.removeAllViews()
    //  setFadeAnimation(holder.layoutItemQuran)
}

// make recycler view show movement between each element
private fun setFadeAnimation(view: View) {
    val anim =
        AlphaAnimation(0.0f, 1.0f) // make the view transparent to visible again with animation
    anim.duration =
        1000 // duration of the animation in milliseconds (1000 milliseconds = 1 second) so the animation will take 1 second to finish from 0.0f to 1.0f
    view.startAnimation(anim)
}




}