package com.elmaref.ui.container


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.elmaref.R
import com.elmaref.data.model.quran.page.VersesItem
import com.elmaref.databinding.ActivityContainerBinding
import com.example.muslim.ui.base.activity.BaseActivity

class ContainerActivity : BaseActivity<ActivityContainerBinding, ContainerViewModel>(), Navigator {


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vmContainer = viewModel
        viewModel.navigator = this

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        NavigationUI.setupWithNavController(viewDataBinding.bottomNavView, navController)

    }

    override fun getLayoutID(): Int = R.layout.activity_container

    override fun makeViewModelProvider(): ContainerViewModel = ViewModelProvider(this).get(
        ContainerViewModel::class.java
    )




}