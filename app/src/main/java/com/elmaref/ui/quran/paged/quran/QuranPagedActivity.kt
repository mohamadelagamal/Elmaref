package com.elmaref.ui.quran.paged.quran


import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.elmaref.R
import com.elmaref.databinding.ActivityQuranPagedBinding
import com.elmaref.ui.quran.paged.adapter.ItemSurahAdapter
import com.elmaref.ui.quran.paged.functions.returnPagesNumber
import com.example.muslim.ui.base.activity.BaseActivity


class QuranPagedActivity() : BaseActivity<ActivityQuranPagedBinding, QuranPagedViewModel>() , Navigator {

    lateinit var adapterSurah: ItemSurahAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmQuranPaged=viewModel
        viewModel.navigator=this
        adapterSurah = ItemSurahAdapter(returnPagesNumber(), this)
        viewDataBinding.viewPager.apply {
            // make viewpager vertical
            adapter = adapterSurah
        }
    }

    override fun getLayoutID(): Int = R.layout.activity_quran_paged

    override fun makeViewModelProvider(): QuranPagedViewModel = ViewModelProvider(this).get(
        QuranPagedViewModel::class.java)

}