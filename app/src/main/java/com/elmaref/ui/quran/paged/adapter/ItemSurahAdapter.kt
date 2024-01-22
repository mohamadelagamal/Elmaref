package com.elmaref.ui.quran.paged.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
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
import com.quranscreen.utils.main


class ItemSurahAdapter(
    val pagesQuranPager: List<Int>,
    val context: Context, val page: Int? = null,
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

        val versesItem = MyApplication.quranValue.filter { it.page == pagesQuranPager[itemAdapterNumber] }
        val words = versesItem.flatMap { it.words }
        val juz = MyApplication.juzData
        val surahName = MyApplication.surahNameData

        val juzFilter = juz?.filter { it.start_page!! <= pagesQuranPager[itemAdapterNumber] && it.end_page!! >= pagesQuranPager[itemAdapterNumber] }

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
                            binding.layoutItemQuran.addView(getBismillah(context))
                        } else if (it.surah != null) {
                            binding.layoutItemQuran.addView(
                                getSurah(
                                    it.unicode.toString(),
                                    context
                                )
                            )
                        }
                    } ?: kotlin.run {
                        binding.layoutItemQuran.addView(getBlank(context))
                    }

                } else {
                    binding.layoutItemQuran.addView(
                        getLine(
                            verse = verse,
                            words = versesItem,
                            context = context,
                            page = itemAdapterNumber + 1,
                            line = i
                        )
                    )
                }
            }

//            Log.e("TAG100", "onBindViewHolder: ${binding.layoutItemQuran.childCount}")
//
//            val layout: LinearLayout = binding.layoutItemQuran
//            val count = layout.childCount
//            var v: View? = null
//            for (i in 0 until count) {
//                val verse = words?.let { getVerseByLine(i, it) }
//                v = layout.getChildAt(i)
//                v.setOnTouchListener { v, event ->
//                    when (event.action) {
//                        MotionEvent.ACTION_UP -> {
//                            val layout: Layout? = (v as TextView).layout
//                            val x = event!!.x
//                            val y = event.y.toInt()
//                            if (layout != null) {
//                                val line: Int = layout.getLineForVertical(y) // get the line number from y position
//                                var offset: Int = layout.getOffsetForHorizontal(line, x) - 1 //
//                                if (offset == -1) {
//                                    offset = 0
//                                }
//
//                                // get the word by offset (position) and filter the words by the word code
//                                val htmlEscapedVerse = verse?.replace("<font color=\"#048383\">", "")
//                                    ?.replace("</font>", "")
//                                versesItem.filter {
//                                    it.words.firstOrNull()?.code_v1?.contains(
//                                        htmlEscapedVerse?.get(offset)!!
//                                    ) == true
//                                }
//                                    .firstOrNull()
//                                    ?.let { word ->
//
//                                        // scroll to the selected word
//                                        word.verse_key?.split(":")?.get(0)?.toInt().let { surah ->
//                                            word.verse_key?.split(":")?.get(1)?.toInt()
//                                                .let { ayah ->
//                                                    Log.e("AYAlINE", "ACTION_MOMENT: $surah $ayah")
//                                                    QuranMenuTfser.newInstance(
//                                                        surah ?: 1,
//                                                        ayah ?: 1,
//                                                        page
//                                                    ).show(
//                                                        (context as AppCompatActivity).supportFragmentManager,
//                                                        ""
//                                                    )
//
//                                                    offset = 0
//                                                }
//                                        }
//                                    }
//                            }
//                        }
//                    }
//                    true
//                }
//
//            }
        }


        //setFadeAnimation(binding.layoutItemQuran)
    }

    // check if the item is finished or not to remove all views and add new views
    override fun onViewRecycled(holder: ItemSurahViewHolder) { // this method is called when the item is finished
        super.onViewRecycled(holder)
        holder.layoutItemQuran.removeAllViews()
        // setFadeAnimation(holder.layoutItemQuran)
    }

    // make recycler view show movement between each element
    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f) // make the view transparent to visible again with animation
        anim.duration =
            1000 // duration of the animation in milliseconds (1000 milliseconds = 1 second) so the animation will take 1 second to finish from 0.0f to 1.0f
        view.startAnimation(anim)
    }


}