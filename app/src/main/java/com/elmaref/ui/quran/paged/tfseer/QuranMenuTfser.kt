package com.elmaref.ui.quran.paged.tfseer

import android.Manifest
import android.animation.ValueAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.elmaref.R
import com.elmaref.data.model.enums.ServiceAction
import com.elmaref.databinding.QuranMenuTfserBinding
import com.elmaref.data.model.quran.mark.BookmarkType
import com.elmaref.ui.quran.paged.functions.Copy
import com.elmaref.ui.quran.paged.functions.getArabic
import com.elmaref.ui.quran.paged.functions.toArabicNumber
import com.elmaref.ui.quran.paged.tfseer.options.share.ShareAyahActivity
import com.elmaref.ui.quran.paged.tfseer.servies.PlayAyahService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quranscreen.constants.LocaleConstants
import com.quranscreen.utils.io
import com.quranscreen.utils.main

class QuranMenuTfser : BottomSheetDialogFragment() {


    lateinit var viewDataBinding: QuranMenuTfserBinding
    lateinit var viewModel: QuranMenuTfserViewModel
    var isBookmarked = false
    var ayahIdFromAllQuran: Int? = null
    var isOnlineNetwork = false
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = QuranMenuTfserViewModel()
        surah = getSurah()
        ayah = getAyah()
        page = getPage()
        createChannelService()
    }

    private fun createChannelService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "MY_SERVICE_CHANNEL",
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = requireActivity().getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get id for ayah from database
        getAyahIdFromAllQuran()
        // set surah name and ayah number
        setSurahNameAyahNumber()
        // set functions for buttons in bottom sheet
        copyFunction()
        // use share function to share ayah text
        shareFunction()
        // use book mark function to add or remove ayah from book mark
        bookMarkFunction()
        // use play and pause function to play or pause sound
        playAndPauseFunction()
        // check if network is online or not
        checkOnlineNetwork()
        // check permission for play sound
        checkPermissionNotification()
    }

    private fun getAyahIdFromAllQuran() {
       io { ayahIdFromAllQuran = viewModel.getAyahId(surah, ayah)       }

    }


    private fun checkPermissionNotification() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

    private fun checkOnlineNetwork() {
        isOnlineNetwork = viewModel.isOnline(requireContext())
    }

    private fun playAndPauseFunction() {
        viewDataBinding.playButton.setOnClickListener {
            if (isOnlineNetwork){
                play()
            }
            else{
                Toast.makeText(requireContext(), "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show()
            }

        }

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
        // play sound

        io{playSound()}

        viewDataBinding.playButton.setOnClickListener {
            pause()
        }
    }

    suspend fun playSound() {
        // make toast to 1 second
        main {
            Toast.makeText(requireContext(), "جاري تشغيل الصوت", Toast.LENGTH_SHORT).show()
        }
        io {
            val serviceIntent = Intent(requireActivity(), PlayAyahService::class.java).apply {
                action = ServiceAction.PLAY_MUSIC.toString()
            }
            // send audio Url to service
            Log.e("Ayahs", "AudioUrl: $ayahIdFromAllQuran")
            serviceIntent.putExtra(LocaleConstants.AYAH_ID_SERVICES, "${ayahIdFromAllQuran}" )
            requireActivity().startService(serviceIntent)
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

        val serviceIntent = Intent(requireActivity(), PlayAyahService::class.java).apply {
            action = ServiceAction.STOP_MUSIC.toString()
        }
        requireActivity().stopService(serviceIntent)

        viewDataBinding.playButton.setOnClickListener {
            play()
        }
    }

    private fun bookMarkFunction() {
        io {
           // viewModel.getAyahAudio(ayahId!!)
            Log.d("QuranMenuTfser", "ayahId: $ayahIdFromAllQuran")
            main {
                isBookmarked = viewModel.getBookMarkDao(ayahIdFromAllQuran.toString())

                if (isBookmarked) {
                    changeBookMark("أَزَالُهُ مِنْ اَلْمُفَضَّلَةِ", true)
                } else {
                    changeBookMark("أَضِفْ إِلَى اَلْمُفَضَّلَةِ")
                }
                viewDataBinding.bookmark.setOnClickListener {
                    io {
                        val ayah_id = viewModel.getAyahId(surah, ayah)
                        // val mark = QuranTable.initializeDatabase(requireContext()).quranBookMarkDao()
                        val isBookmarked = viewModel.getBookMarkDao(ayah_id.toString())
                        if (!isBookmarked) {
                            viewModel.insertBookMark(ayah_id.toString(), BookmarkType.QURAN)
                            main {
                                changeBookMark("أَزَالُهُ مِنْ اَلْمُفَضَّلَةِ", true)
                            }
                        } else {
                            viewModel.deleteBookMark(ayah_id.toString(), BookmarkType.QURAN)
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
            viewModel.getSurahById(surah).firstOrNull()?.let {
                main {
                    viewDataBinding.title.text =
                        " ${it.translation?.get(2)?.text} ,  اَلْآيَةُ ( ${ayah.toArabicNumber()} )"
                    surahName = it.name!!
                }

            }
            // get Tafser from database by surah and ayah number
            tafseerId = viewModel.getTfseerByIdAndVerseNumber(surah, ayah).firstOrNull()?.text!!
            main {
                viewDataBinding.textTranslation.text = tafseerId
            }
        }
    }

    private fun shareFunction() {
        viewDataBinding.share.setOnClickListener {
            io {
                val surahName =
                    viewModel.getSurahById(surah).firstOrNull()?.translation?.get(2)?.text
                val text = viewModel.getAyah(surah, ayah).first().text
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
                viewModel.getAyah(surah, ayah).firstOrNull()?.let {
                    main {
                        val arabicText = it.text.getArabic()
                        // copy arabic text to clipboard
                        requireContext().Copy(arabicText, requireContext())
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}