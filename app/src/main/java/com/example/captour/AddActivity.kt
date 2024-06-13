package com.example.captour

import android.app.Activity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.captour.databinding.ActivityAddBinding
import java.io.File
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat

class AddActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar);

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnWrite.setOnClickListener {
            val intent = intent
            val title = binding.title.text.toString()
            val content = binding.content.text.toString()
            val data = title + "\n" + content
            intent.putExtra("result", data)
            setResult(Activity.RESULT_OK, intent)

            // db에 저장하기
            val db = DBHelper(this).writableDatabase
            db.execSQL("insert into captour_db (data) values (?)", arrayOf<String>(data))
            db.close()
            finish()
            true

            // 파일 저장하기
            val dateformat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss") // 년 월 일 시 분 초
            val file = File(filesDir, "test.txt")
            val writestream: OutputStreamWriter = file.writer()
            writestream.write(dateformat.format(System.currentTimeMillis()))
            writestream.flush()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}