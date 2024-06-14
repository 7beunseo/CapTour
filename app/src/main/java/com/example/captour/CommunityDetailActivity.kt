package com.example.captour

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.example.captour.databinding.ActivityDetailCommunityBinding
import com.example.captour.databinding.ItemRecyclerviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommunityDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailCommunityBinding
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = intent.getStringExtra("title")
        val email = intent.getStringExtra("email")
        val dateTime = intent.getStringExtra("dateTime")
        val content = intent.getStringExtra("content")
        val stars = intent.getFloatExtra("stars", 0.0f)
        val imageUrl = intent.getStringExtra("image")

        binding.title.text = title
        binding.email.text = email
        binding.dateTime.text = dateTime
        binding.content.text = content
        binding.ratingBar.rating = stars

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions() ) {
            if (it.all { permission -> permission.value == true }) {
                noti("${binding.email.text.toString()} 팔로우 시작")
            } else {
                Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show()
            }
        }

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .into(binding.imageView)
            binding.imageView.visibility = View.VISIBLE
        }

        if (MyApplication.email.toString() != binding.email.text.toString()) {
            // 팔로우 상태 확인
            val retrofit = Retrofit.Builder()
                .baseUrl("http://172.30.1.4:8080/captour/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(NetworkService::class.java)
            val call =
                apiService.getFollowStatus(
                    follower = MyApplication.email.toString(),
                    following = binding.email.text.toString()
                )

            call?.enqueue(object : Callback<FollowJsonResponse> {
                override fun onResponse(call: Call<FollowJsonResponse>, response: Response<FollowJsonResponse>) {
                    response.body()?.toString()?.let { Log.d("mobileapp", it) }
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 상태 조회 완료", Toast.LENGTH_LONG).show()
                    if (response.body()?.message == "true") {
                        binding.followBtn.visibility = View.GONE
                        binding.followCancleBtn.visibility = View.VISIBLE
                    } else {
                        binding.followBtn.visibility = View.VISIBLE
                        binding.followCancleBtn.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<FollowJsonResponse>, t: Throwable) {
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 상태 조회 실패", Toast.LENGTH_LONG).show()
                }
            })
        }

        binding.followCancleBtn.setOnClickListener {
            // 팔로우 취소
            val retrofit = Retrofit.Builder()
                .baseUrl("http://172.30.1.4:8080/captour/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(NetworkService::class.java)

            val call =
                apiService.deleteFollow(
                    follower = MyApplication.email.toString(),
                    following = binding.email.text.toString()
                )

            call?.enqueue(object : Callback<FollowJsonResponse> {
                override fun onResponse(call: Call<FollowJsonResponse>, response: Response<FollowJsonResponse>) {
                    response.body()?.toString()?.let { Log.d("mobileapp", it) }
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 삭제 완료", Toast.LENGTH_LONG).show()

                    // 화면 다시 조회
                    val intent = intent
                    finish()
                    startActivity(intent)
                }

                override fun onFailure(call: Call<FollowJsonResponse>, t: Throwable) {
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 삭제 실패", Toast.LENGTH_LONG).show()
                }
            })
        }

        binding.followBtn.setOnClickListener {
            // 팔로우 생성

            val retrofit = Retrofit.Builder()
                .baseUrl("http://172.30.1.4:8080/captour/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(NetworkService::class.java)

            val followRequest = Follow(
                follower = MyApplication.email.toString(),
                following = binding.email.text.toString()
            )

            val call =
                apiService.createFollow(followRequest)

            call?.enqueue(object : Callback<FollowJsonResponse> {
                override fun onResponse(call: Call<FollowJsonResponse>, response: Response<FollowJsonResponse>) {
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 완료", Toast.LENGTH_LONG).show()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(this@CommunityDetailActivity, "android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED) {
                            noti("${binding.email.text.toString()} 팔로우 시작")
                        } else {
                            Log.d("mobileapp","permission-err")
                            permissionLauncher.launch(arrayOf("android.permission.POST_NOTIFICATIONS"))
                        }
                    } else {
                        Log.d("mobileapp","noti()-second")
                        noti("${binding.email.text.toString()} 팔로우 시작")
                    }

                    // 화면 다시 조회
                    val intent = intent
                    finish()
                    startActivity(intent)
                }

                override fun onFailure(call: Call<FollowJsonResponse>, t: Throwable) {
                    Toast.makeText(this@CommunityDetailActivity, "팔로우 실패", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    fun noti(message: String = "알림") { // 알림창을 띄움
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        Log.d("mobileapp", "noti-in")
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {     // 26 버전 이상
            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {   // 채널에 다양한 정보 설정
                description = "My Channel One Description"
                setShowBadge(true)  // 앱 런처 아이콘 상단에 숫자 배지를 표시할지 여부를 지정
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes) // 소리 발생하지 않게 해도 됨
                enableVibration(true)
            }
            // 채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)
            // 채널을 이용하여 builder 생성
            builder = NotificationCompat.Builder(this, channelId)
        } else {  // 26 버전 이하
            builder = NotificationCompat.Builder(this)
        }

        // 알림의 기본 정보
        builder.run {
            setSmallIcon(R.drawable.captour_logo)
            setWhen(System.currentTimeMillis())
            setContentTitle("CapTour")
            setContentText(message)
            // setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher))
        }

        manager.notify(11, builder.build())
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()

        // 글자 크기 설정
        val fontSize = sharedPreference.getInt("font_size", 16)
        binding.title.textSize = fontSize + 10f

        binding.email.textSize = fontSize + 1f
        binding.title.textSize = fontSize + 1f
        binding.content.textSize = fontSize + 1f
        binding.dateTime.textSize = fontSize + 1f

        // 색상 설정
        val color = sharedPreference.getString("color", "#363C90")
        val colorCode = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorCode)
        binding.toolbar.setBackgroundColor(colorCode)
        binding.followCancleBtn.setBackgroundColor(colorCode)
        binding.followBtn.setBackgroundColor(colorCode)
    }
}
