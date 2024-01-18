package com.elmaref.ui.quran.container

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.elmaref.R
import com.elmaref.app.MyApplication
import com.elmaref.databinding.FragmentQuranBinding
import com.example.quran.ui.adapter.ItemSurahNameAdapter


class QuranFragment : Fragment() {

    lateinit var adapter: ItemSurahNameAdapter
    lateinit var viewModel: QuranContainerViewModel
    lateinit var viewDataBinding: FragmentQuranBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(QuranContainerViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_quran, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToLiveData()

    }

    private fun subscribeToLiveData() {
        val surahName = MyApplication.surahNameFlowData
        // work in background thread and return data to main thread to update the ui with the data that returned from the background thread
        val surahDescription = MyApplication.surahDescriptionObserver
        adapter = ItemSurahNameAdapter(requireActivity())
        viewDataBinding.suranNameRecycler.adapter = adapter

    }
}