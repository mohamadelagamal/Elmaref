package com.elmaref.ui.quran.container

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.elmaref.R
import com.elmaref.databinding.FragmentQuranBinding
import com.elmaref.ui.base.fragment.BaseFragment
import com.elmaref.ui.quran.paged.functions.findCurrentPage
import com.elmaref.ui.quran.paged.quran.QuranPagedActivity
import com.elmaref.ui.quran.saved.SavedAyahQuranActivity
import com.elmaref.ui.quran.search.SearchQuranActivity
import com.example.quran.ui.adapter.ItemSurahNameAdapter
import com.quranscreen.constants.LocaleConstants


class QuranFragment : BaseFragment<FragmentQuranBinding, QuranContainerViewModel>(), Navigator {

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

        adapter.onItemClickListener = object : ItemSurahNameAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int) {
                val result = findCurrentPage(pos)
                movedToQuranPage(result)
            }
        }

        viewDataBinding.searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                viewModel.openSearch()
            }

        }
        viewDataBinding.searchView.setOnClickListener {
            viewModel.openSearch()
        }


    }

    private fun movedToQuranPage(result: Int?) {
        if (result != null) {

            val intent = Intent(requireActivity(), QuranPagedActivity::class.java)
            intent.putExtra(LocaleConstants.QUICK_ACCESS, result)
            startActivity(intent)
            requireActivity().overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        } else {
            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
        }
    }



    override fun openFavoriteAyahs() {
        val intent = Intent(requireActivity(), SavedAyahQuranActivity::class.java)
        startActivity(intent)
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }

    override fun openSearch() {
        val intent = Intent(requireActivity(), SearchQuranActivity::class.java)
        startActivity(intent)
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onResume() {
        super.onResume()

        //hide keyboard when returning in activity with search view

        viewDataBinding.searchView.clearFocus()
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(viewDataBinding.searchView.windowToken, 0)

    }
}
