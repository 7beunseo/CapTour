package com.example.captour

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.captour.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.BufferedReader
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var sharedPreference: SharedPreferences
    var myFontSize = 0

    class MyFragmentAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
        // 어떤 fragement를 다룰 것인지 변수를 선언해둠
        val fragments: List<Fragment>

        init {
            fragments =
                listOf(OneFragment(), TwoFragment(), ThreeFragment()) // 3개의 Fragment를 리스트로 담고 있음
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)

        /* 테마 바꾸기 실행 안됨 */
        val color = sharedPreference.getString("color", "#ff0000")
        Log.d("mobileapp", ""+color)
        if(color.equals("#9F7346")) {
            Log.d("mobileapp", "in")
            // setTheme(R.style.Theme_CookBook_brown)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // val adapter = MyFragmentAdapter(this)
        binding.viewpager.adapter = MyFragmentAdapter(this)

        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            tab.text = "TAB ${position + 1}" // 탭의 텍스트 설정
            when (position) {
                0 ->  {
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.talk)
                    tab.text = "커뮤니티"
                }
                1 -> {
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.recipe)
                    tab.text = "레시피"
                }
                2 -> {
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.user)
                    tab.text = "마이페이지"
                }
            }
        }.attach()


        // tab 색상 변경
        val colorCode = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorCode)
        // val colorStateList = ContextCompat.getColorStateList(this, colorCode)
        binding.tabs.tabIconTint = colorStateList
        binding.tabs.tabTextColors = colorStateList


        /*
        테마 바뀌기 실행 안됨
        Log.d("mobileapp", ""+color)
        val colorInt = Color.parseColor(color)

        val newTheme = this.theme

        val typedValue = TypedValue()
        typedValue.data = colorInt
        newTheme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        newTheme.applyStyle(typedValue.resourceId, false)

        setTheme(newTheme)

         */

    }

    // tabs 글자 크기 변경 - 미완성
    private fun setTabTextSize(tabLayout: TabLayout, textSize: Float) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                val tabTextView = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
                val textView = tabTextView.findViewById<TextView>(com.google.android.material.R.id.tabMode)
                textView?.textSize = textSize
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        // tab 색상 변경
        val color = sharedPreference.getString("color", "#ff0000")
        Log.d("mobileapp", ""+color)
        val colorCode = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorCode)
        // val colorStateList = ContextCompat.getColorStateList(this, colorCode)
        binding.tabs.tabIconTint = colorStateList
        binding.tabs.tabTextColors = colorStateList
        val fontSize = sharedPreference.getInt("font_size", 16)

        setTabTextSize(binding.tabs, fontSize + 1f)
    }

}