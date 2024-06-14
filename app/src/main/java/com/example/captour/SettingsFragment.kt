package com.example.captour

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.captour.databinding.ActivitySettingBinding

class SettingsFragment : PreferenceFragmentCompat() {
    // lateinit var binding: RootPreference
    // lateinit var sharedPreference: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // binding = ActivitySettingBinding.inflate(layoutInflater)
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        // sharedPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())

    }

    /*
    override fun onResume() {
        super.onResume()

        // 글자 크기 설정
        val fontSize = sharedPreference.getInt("font_size", 16)

        // 색상 설정
        val color = sharedPreference.getString("color", "#363C90")
        val colorCode = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorCode)
        binding.toolbar.setBackgroundColor(colorCode)
    }

     */
}