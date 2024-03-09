package com.elmaref.ui.salat.options.us

import android.os.Bundle
import com.elmaref.R
import com.elmaref.databinding.ActivityAboutUsBinding
import com.example.muslim.ui.base.activity.BaseActivity

class AboutUsActivity : BaseActivity<ActivityAboutUsBinding, AboutUsViewModel>(), Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.aboutUs= viewModel
        viewModel.navigator=this

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_about_us
    }

    override fun makeViewModelProvider(): AboutUsViewModel {
        return AboutUsViewModel()
    }
}