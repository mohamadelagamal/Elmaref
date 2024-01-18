//package com.elmaref.ui.prayer.container.adapter
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.lifecycle.Lifecycle
//import androidx.viewpager2.adapter.FragmentStateAdapter
//
//private const val NUM_TABS = 2
//class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
//   FragmentStateAdapter(fragmentManager, lifecycle) {
//
//   override fun getItemCount(): Int {
//       return NUM_TABS
//   }
//
//   override fun createFragment(position: Int): Fragment {
//       when (position) {
//          // 0 -> return AllAzkarFragment()
//           0 -> return ShalatFragment()
//           1 -> return QuranFragment()
////            2 -> return DailyPrayerFragment()
////            3 -> return MasjidFragment()
////            4 -> return ZikirShalatFragment()
////            5 -> return ZikirClothingFragment()
////            6 -> return ZikirSleepingFragment()
////            7 -> return NewlywedFragment()
//       }
//       return ShalatFragment()
//   }
//}