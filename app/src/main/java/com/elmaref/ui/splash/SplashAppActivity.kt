package com.elmaref.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.elmaref.R
import com.elmaref.data.repository.interfaces.offline.QuranOfflineDataSource
import com.elmaref.ui.app.MyApplication
import com.elmaref.databinding.ActivitySplashBinding
import com.elmaref.data.room.tables.QuranTable
import com.elmaref.ui.container.ContainerActivity
import com.elmaref.ui.onboarding.OnBoardLoadingActivity
import com.example.muslim.ui.base.activity.BaseActivity
import com.quranscreen.utils.io
import com.quranscreen.utils.main


class SplashAppActivity : BaseActivity<ActivitySplashBinding,SplashViewModel>(),Navigator {
    // shared preferences is used to save data in the device and it's used to save data like user name or password or any data that you want to save in the device
    var sharedPreferences: SharedPreferences? = null
    // shared editor is used to edit the data that saved in shared preferences and it's used to edit data like user name or password or any data that you want to edit in the device
    var sharedEditor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmSplash = viewModel
        viewModel.navigator=this
        // check if first run or not and move to onboarding activity
        sharedPreferences = getSharedPreferences("onBoard", MODE_PRIVATE)
        sharedEditor = sharedPreferences!!.edit()
        val isFirstRun = sharedPreferences!!.getBoolean("FirstRun", true)
        if (isFirstRun) {
            sharedEditor!!.putBoolean("FirstRun", false)
            sharedEditor!!.apply()
          onBoardingMove()
        }
        else {
           //  check if data is loaded or not and move to loading activity
            viewModel.getPageQuran().observe(this,{
                if (!it.size.equals(6236)){
                    onBoardingMove()
                }
                else{
                    // create database for quran
                    MyApplication.quranValue = it
                    containerMove()

                }
            })
        }
    }

    private fun containerMove() {
        val intent = Intent(this, ContainerActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun onBoardingMove() {
        val intent = Intent(this, OnBoardLoadingActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun getLayoutID(): Int = R.layout.activity_splash

    override fun makeViewModelProvider(): SplashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)





}