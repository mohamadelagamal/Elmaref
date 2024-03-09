package com.elmaref.ui.salat.options.setting

import android.os.Bundle
import com.elmaref.R
import com.elmaref.databinding.ActivitySettingBinding
import com.example.muslim.ui.base.activity.BaseActivity

class SettingActivity : BaseActivity<ActivitySettingBinding, SettingViewModel>(), Navigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.setting= viewModel
        viewModel.navigator=this

    }
    override fun getLayoutID(): Int {
        return R.layout.activity_setting
    }

    override fun makeViewModelProvider(): SettingViewModel {
        return SettingViewModel()
    }

}