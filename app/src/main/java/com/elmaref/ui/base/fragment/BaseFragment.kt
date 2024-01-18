package com.elmaref.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.muslim.ui.base.activity.BaseViewModel

abstract class BaseFragment <DB : ViewDataBinding, VM : BaseViewModel<*>>: Fragment() {
    lateinit var viewDataBinding: DB
    lateinit var viewModel: VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(makeViewModelProvider()::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewDataBinding = DataBindingUtil.inflate(inflater,getLayoutID(),container,false)
        return viewDataBinding.root
    }
    abstract fun getLayoutID (): Int
    abstract fun makeViewModelProvider (): VM
}