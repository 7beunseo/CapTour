package com.example.captour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val flashView = findViewById<ImageView>(R.id.flashView)
        val textView = findViewById<LinearLayout>(R.id.textView)
        val zoomInAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        val flashAnimation = AnimationUtils.loadAnimation(this, R.anim.flash)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        zoomInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                flashView.visibility = View.VISIBLE
                flashView.startAnimation(flashAnimation)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        flashAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                flashView.visibility = View.GONE
                imageView.visibility = View.GONE
                textView.visibility = View.VISIBLE
                textView.startAnimation(fadeInAnimation)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        imageView.startAnimation(zoomInAnimation)

        val backgroundExe: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        val mainExe: Executor = ContextCompat.getMainExecutor(this)
        backgroundExe.schedule({ // 스레드 실행 
            mainExe.execute {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 3, TimeUnit.SECONDS
        )
    }
}
