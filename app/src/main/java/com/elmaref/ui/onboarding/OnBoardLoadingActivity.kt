package com.elmaref.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.elmaref.R
import com.elmaref.databinding.ActivityBoardLoadingBinding
import com.elmaref.model.enums.ResultEnum
import com.elmaref.ui.splash.SplashAppActivity
import com.example.muslim.ui.base.activity.BaseActivity

class OnBoardLoadingActivity : BaseActivity<ActivityBoardLoadingBinding,OnBoardLoadingViewModel>(),Navigator {


    val handler =Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmOnBoardLoading=viewModel
        viewModel.navigator=this


        viewModel.isQuranLoaded.observe(this,{
          when(it){

                ResultEnum.Success->{
                    viewDataBinding.progressBarQuran.visibility=View.GONE
                    viewDataBinding.checkQuran.visibility=View.VISIBLE
                    viewDataBinding.prepareQuran.text = "تَمَّ إعْدَادُ بَيَانَاتِ اَلْقُرْآنَِّ بِنَجَاحٍ"

                        // moving to next activity
                        moving()
                }
                ResultEnum.Error->{
                    viewDataBinding.tryAgainQuran.visibility=View.VISIBLE
                }
                ResultEnum.Loading->{

                }
          }

        })


    }

    private fun moving() {
        handler.postDelayed({
            val intent = Intent(this, SplashAppActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        },2000)

    }

    override fun getLayoutID(): Int = R.layout.activity_board_loading

    override fun makeViewModelProvider(): OnBoardLoadingViewModel = ViewModelProvider(this).get(OnBoardLoadingViewModel::class.java)






}