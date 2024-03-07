package com.elmaref.ui.quran.saved

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.elmaref.R
import com.elmaref.data.model.quran.mark.BookmarkType
import com.elmaref.data.model.quran.mark.QuranBookMark
import com.elmaref.databinding.ActivitySavedAyahQuranBinding
import com.elmaref.ui.onboarding.OnBoardLoadingActivity
import com.elmaref.ui.quran.container.QuranFragment
import com.elmaref.ui.quran.paged.quran.QuranPagedActivity
import com.elmaref.ui.quran.saved.adapter.SavedAyahQuranAdapter
import com.example.muslim.ui.base.activity.BaseActivity
import com.quranscreen.constants.LocaleConstants

class SavedAyahQuranActivity : BaseActivity<ActivitySavedAyahQuranBinding,SavedAyahQuranViewModel>(),Navigator {

    lateinit var adapter: SavedAyahQuranAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.savedAyah=viewModel
        viewModel.navigator=this
        adapter = SavedAyahQuranAdapter()

        viewModel.SavedAyahList.observe(this,{
            adapter= SavedAyahQuranAdapter(it)
            viewDataBinding.recyclerView.adapter= adapter

            adapter.onItemClickListener = object :SavedAyahQuranAdapter.OnItemClickListener{
                override fun onItemClick(pageNumber: Int?) {
                    movedToQuranPage(pageNumber?.minus(1))
                    Log.e("pageNumberAdapter","$pageNumber")
                }

            }
        })


    }


    override fun getLayoutID(): Int = R.layout.activity_saved_ayah_quran

    override fun makeViewModelProvider(): SavedAyahQuranViewModel = SavedAyahQuranViewModel()
    private fun movedToQuranPage(result: Int?) {
        if (result != null) {

            val intent = Intent(this, QuranPagedActivity::class.java)
            intent.putExtra(LocaleConstants.QUICK_ACCESS, result)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}