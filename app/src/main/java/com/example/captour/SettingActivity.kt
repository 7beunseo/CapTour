package com.example.captour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.captour.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_setting)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // 뒤로 가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 뒤로 가기 버튼 선택 시
        if (item.itemId == android.R.id.home) {
            onBackPressed() // 현재 액티비티를 종료하고 이전 화면으로 돌아갑니다.
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}