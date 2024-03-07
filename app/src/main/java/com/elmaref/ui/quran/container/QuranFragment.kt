package com.elmaref.ui.quran.container

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmaref.R
import com.elmaref.ui.app.MyApplication
import com.elmaref.databinding.FragmentQuranBinding
import com.elmaref.ui.base.fragment.BaseFragment
import com.elmaref.ui.quran.paged.functions.findCurrentPage
import com.elmaref.ui.quran.paged.quran.QuranPagedActivity
import com.elmaref.ui.quran.saved.SavedAyahQuranActivity
import com.example.quran.ui.adapter.ItemSurahNameAdapter
import com.quranscreen.constants.LocaleConstants
import com.quranscreen.utils.io


class QuranFragment : BaseFragment<FragmentQuranBinding,QuranContainerViewModel>(),Navigator {

    lateinit var adapter: ItemSurahNameAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(QuranContainerViewModel::class.java)
        viewModel.navigator = this

    }

    override fun getLayoutID(): Int = R.layout.fragment_quran


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ItemSurahNameAdapter(requireActivity())
        viewDataBinding.suranNameRecycler.adapter = adapter
        viewDataBinding.vmQuran = viewModel

        adapter.onItemClickListener= object: ItemSurahNameAdapter.OnItemClickListener{
            override fun onItemClick(pos: Int) {
                val result = findCurrentPage(pos)
                movedToQuranPage(result)
            }

        }
    }

    private fun movedToQuranPage(result: Int?) {
        if (result != null) {

            val intent = Intent(requireActivity(), QuranPagedActivity::class.java)
            intent.putExtra(LocaleConstants.QUICK_ACCESS, result)
            startActivity(intent)
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        } else {
            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun subscribeToLiveData() {
        adapter = ItemSurahNameAdapter(requireActivity())
        viewDataBinding.suranNameRecycler.adapter = adapter

    }

    override fun openFavoriteAyahs() {
        val intent = Intent(requireActivity(), SavedAyahQuranActivity::class.java)
        startActivity(intent)
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}