package com.elmaref.ui.quran.search

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elmaref.R
import com.elmaref.databinding.ActivitySearchQuranBinding
import com.example.muslim.ui.base.activity.BaseActivity

class SearchQuranActivity : BaseActivity<ActivitySearchQuranBinding,SearchQuranViewModel>(),Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_search_quran
    }

    override fun makeViewModelProvider(): SearchQuranViewModel {
       return SearchQuranViewModel()
    }
}