package com.elmaref.ui.quran.paged.tfseer

import android.animation.ValueAnimator
import android.graphics.ColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.toColor
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieListener
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.elmaref.R
import com.elmaref.databinding.QuranMenuTfserBinding
import com.elmaref.model.quran.mark.BookmarkType
import com.elmaref.model.quran.mark.QuranBookMark
import com.elmaref.room.dao.quran.ayah.getAyahDao
import com.elmaref.room.dao.quran.mark.getBookmarkDao
import com.elmaref.room.dao.quran.tfseer.getTafseerDao
import com.elmaref.room.tables.QuranTable
import com.elmaref.ui.quran.paged.functions.Copy
import com.elmaref.ui.quran.paged.functions.getArabic
import com.elmaref.ui.quran.paged.functions.toArabicNumber
import com.elmaref.ui.quran.paged.tfseer.options.share.ShareAyahActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textview.MaterialTextView
import com.quranscreen.room.dao.quran.names.getSurahDao
import com.quranscreen.utils.io
import com.quranscreen.utils.main


class QuranMenuTfser : BottomSheetDialogFragment() {


    lateinit var viewDataBinding: QuranMenuTfserBinding
    lateinit var viewModel: QuranMenuTfserViewModel
    var isBookmarked = false
    var ayahId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.quran_menu_tfser, container, false
        )
        return viewDataBinding.root
    }

    var surahName = ""

    var surah: Int = 1
    var ayah: Int = 1
    var page: Int? = null
    lateinit var tafseerId: String

    companion object {

        const val ARG_SURAH = "surah"
        const val ARG_AYAH = "ayah"
        const val ARG_PAGE = "page"

        fun newInstance(surah: Int, ayah: Int, page: Int? = null): QuranMenuTfser {
            return QuranMenuTfser().apply {
                this.arguments = bundleOf(ARG_SURAH to surah, ARG_AYAH to ayah, ARG_PAGE to page)
            }
        }

        fun QuranMenuTfser.getSurah(): Int {
            return arguments?.getInt(ARG_SURAH) ?: 1
        }

        fun QuranMenuTfser.getAyah(): Int {
            return arguments?.getInt(ARG_AYAH) ?: 1
        }

        fun QuranMenuTfser.getPage(): Int? {
            return arguments?.getInt(ARG_PAGE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = QuranMenuTfserViewModel()
        surah = getSurah()
        ayah = getAyah()
        page = getPage()
        setSurahNameAyahNumber()
        copyFunction()
        shareFunction()
        bookMarkFunction()
        playAndPauseFunction()


    }

    private fun playAndPauseFunction() {
        viewDataBinding.playButton.setOnClickListener { play() }

    }
    fun play() {

        val animtor = ValueAnimator.ofFloat(0f, 0.5f)
        animtor.addUpdateListener { animator ->
           viewDataBinding.playButton.progress = animator.animatedValue as Float
        }

        animtor.duration = 500
        animtor.start()

        viewDataBinding.playingBar.playAnimation()
        viewDataBinding.playingBar.visibility = View.VISIBLE

        viewDataBinding.playButton.setOnClickListener {
            pause()
        }
    }
    fun pause() {
        val animtor = ValueAnimator.ofFloat(0.5f, 1f)
        animtor.addUpdateListener { animator ->
            viewDataBinding.playButton.progress = animator.animatedValue as Float
        }

        animtor.duration = 500
        animtor.start()

        viewDataBinding.playingBar.pauseAnimation()
        viewDataBinding.playingBar.visibility = View.GONE

        viewDataBinding.playButton.setOnClickListener {
            play()
        }
    }
    private fun bookMarkFunction() {
        io {
            ayahId =
                getAyahDao().getAyahBySurahIdAndVerseNumber(surah, ayah).firstOrNull()?.id
            Log.d("QuranMenuTfser", "ayahId: $ayahId")
            main {
                isBookmarked = requireContext().getBookmarkDao()
                    .getBookmarkByIdSuspend(ayahId.toString()) != null
                if (isBookmarked) {
                    changeBookMark("أَزَالُهُ مِنْ اَلْمُفَضَّلَةِ", true)
                } else {
                    changeBookMark("أَضِفْ إِلَى اَلْمُفَضَّلَةِ")
                }
                viewDataBinding.bookmark.setOnClickListener {
                    io {
                        val ayah_id =
                            getAyahDao().getAyahBySurahIdAndVerseNumber(surah, ayah)
                                .firstOrNull()?.id
                        val mark = QuranTable.getDatabase(requireContext()).quranBookMarkDao()
                        val isBookmarked =
                            mark.getBookmarkByIdSuspend(ayah_id.toString()) != null
                        if (!isBookmarked) {
                            mark.insertBookMark(
                                QuranBookMark(
                                    idString = ayah_id.toString(),
                                    type = BookmarkType.QURAN
                                )
                            )
                            main {
                                changeBookMark("أَزَالُهُ مِنْ اَلْمُفَضَّلَةِ", true)
                            }
                        } else {
                            mark.deleteBookmark(ayah_id.toString(), BookmarkType.QURAN)
                            main {
                                changeBookMark("أَضِفْ إِلَى اَلْمُفَضَّلَةِ")
                            }
                        }
                    }
                }
            }
        }

    }

    private fun changeBookMark(word: String, changeBackGround: Boolean = false) {
        if (changeBackGround) {
            compoundDrawableTint(R.drawable.read_bookmark)
            viewDataBinding.bookmark.text = word
        } else {
            viewDataBinding.bookmark.text = word
            compoundDrawableTint(R.drawable.ic_bookmarks)
        }
    }

    private fun compoundDrawableTint(color: Int) {
        viewDataBinding.bookmark.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            color,
            0
        )
    }

    private fun setSurahNameAyahNumber() {
        io {
            // get surah name from database
            getSurahDao().getSurahById(surah).firstOrNull()?.let {
                main {
                    viewDataBinding.title.text =
                        " ${it.translation?.get(2)?.text} ,  اَلْآيَةُ ( ${ayah.toArabicNumber()} )"
                    surahName = it.name!!
                }

            }
            // get Tafser from database by surah and ayah number
            tafseerId =
                getTafseerDao().getAyahBySurahIdAndVerseNumber(surah, ayah).firstOrNull()?.text!!
            main {
                viewDataBinding.textTranslation.text = tafseerId
            }
        }
    }

    private fun shareFunction() {
        viewDataBinding.share.setOnClickListener {
            io {
                val surahName =
                    getSurahDao().getSurahById(surah).firstOrNull()?.translation?.get(2)?.text
                val text = getAyahDao().getAyahBySurahIdAndVerseNumber(surah, ayah).first().text
                val arabic = text.getArabic()
                main {

                    ShareAyahActivity.start(
                        requireActivity(),
                        arabic,
                        tafseerId,
                        "(${surahName}: ${ayah.toArabicNumber()})"
                    )
                    requireActivity().overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    )
                    dismiss()

                }
            }
        }
    }

    private fun copyFunction() {
        viewDataBinding.copy.setOnClickListener {
            io {
                getAyahDao().getAyahBySurahIdAndVerseNumber(surah, ayah).firstOrNull()?.let {
                    main {
                        val arabicText = it.text.getArabic()
                        // copy arabic text to clipboard
                        requireContext().Copy(arabicText, requireContext())
                    }
                }
            }
        }
    }


}