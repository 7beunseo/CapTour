package com.example.captour

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
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

            // 1. db에 저장하기
            /*
            val intent = intent
            val title = binding.title.text.toString()
            val content = binding.content.text.toString()
            val data = title + "\n" + content
            intent.putExtra("result", data)
            setResult(Activity.RESULT_OK, intent)

            val db = DBHelper(this).writableDatabase
            db.execSQL("insert into captour_db (data) values (?)", arrayOf<String>(data))
            db.close()
            finish()
            true
             */

            val dateFormat = SimpleDateFormat(("yyyy-MM-dd hh:mm:ss"))

            // 2. FirebaseStoreage 이용
            if(binding.title.text.isNotEmpty()) {
                // 로그인 이메일, 제목, 본문, 스타, 입력 시간
                val data = mapOf(
                    "email" to MyApplication.email,
                    "title" to binding.title.text.toString(),
                    "content" to binding.content.text.toString(),
                    "stars" to binding.ratingBar.rating.toFloat(),
                    "date_time" to dateFormat.format(System.currentTimeMillis())
                )
                // store에 저장
                MyApplication.db.collection("review")
                    .add(data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "데이터 저장 성공", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "데이터 저장 실패", Toast.LENGTH_SHORT).show()

                    }
            } else {
                Toast.makeText(this, "제목을 작성해주세요", Toast.LENGTH_SHORT).show()
            }

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