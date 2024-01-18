//package com.quranscreen.ui.prayer.container
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.view.children
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.RecyclerView
//import androidx.viewpager2.widget.ViewPager2
//import com.google.android.material.tabs.TabLayout
//import com.google.android.material.tabs.TabLayoutMediator
//import com.quranscreen.databinding.FragmentAzkarBinding
//import com.quranscreen.ui.prayer.container.adapter.ViewPagerAdapter
//
//
//class AzkarFragment : Fragment() {
//
//    private lateinit var binding: FragmentAzkarBinding
//    private lateinit var viewPagerAdapter: ViewPagerAdapter
//    val tabsMediatorTitles = arrayOf(
//        "الأذْكار اليوْميَّة",
//        "أَدْعِيَة مِنْ اَلْقُرْآنِ",
//        "أَدْعِيَة يَوْمِيَّةٍ",
//        "اَلْمَسْجِدُ",
//        "اَلصَّلَاةُ",
//        "اَلْمَلَابِسُ",
//        "اَلنَّوْمُ",
//        "اَلْمُتَزَوِّجِينَ"
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        viewPagerAdapter = ViewPagerAdapter(
//            childFragmentManager,
//            lifecycle
//        ) // childFragmentManager is used for nested fragments inside fragments
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentAzkarBinding.inflate(inflater, container, false)
//        binding.viewPager.adapter = viewPagerAdapter
//        tabsMediator(binding.tabs, binding.viewPager)
//        return binding.root
//    }
//
//    private fun tabsMediator(tabs: TabLayout, viewPager: ViewPager2) {
//        TabLayoutMediator(tabs, viewPager) { tab, position ->
//            tab.text = tabsMediatorTitles[position]
//        }.attach()
//
//    }
//
//}