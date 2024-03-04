package com.elmaref.ui.quran.paged.quran


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.elmaref.R
import com.elmaref.databinding.ActivityQuranPagedBinding
import com.elmaref.ui.app.MyApplication
import com.elmaref.ui.container.ContainerActivity
import com.elmaref.ui.quran.paged.adapter.ItemSurahAdapter
import com.elmaref.ui.quran.paged.functions.returnPagesNumber
import com.elmaref.ui.quran.paged.tfseer.QuranMenuTfser
import com.elmaref.ui.splash.SplashAppActivity
import com.example.muslim.ui.base.activity.BaseActivity
import com.quranscreen.constants.LocaleConstants
import java.util.Objects


class QuranPagedActivity() : BaseActivity<ActivityQuranPagedBinding, QuranPagedViewModel>(),
    Navigator {

    lateinit var adapterSurah: ItemSurahAdapter
    var currentPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPage = intent.getIntExtra(LocaleConstants.QUICK_ACCESS, 0)
        viewDataBinding.vmQuranPaged = viewModel
        viewModel.navigator = this
        adapterSurah = ItemSurahAdapter(returnPagesNumber(), this)
        viewDataBinding.viewPager.apply {
            // make viewpager vertical
            adapter = adapterSurah
        }
        viewDataBinding.viewPager.setCurrentItem(currentPage, false)

    }

    override fun getLayoutID(): Int = R.layout.activity_quran_paged

    override fun makeViewModelProvider(): QuranPagedViewModel = ViewModelProvider(this).get(
        QuranPagedViewModel::class.java
    )

}