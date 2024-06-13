package com.example.captour

import androidx.multidex.MultiDexApplication
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage

// Dex : Dalvic Executale (64k)
class MyApplication : MultiDexApplication() {
    companion object {
        fun checkAuth(): Boolean {
            var currentUser = auth.currentUser
            if(currentUser != null) {
                email = currentUser.email
                return currentUser.isEmailVerified
            }
            return false
        }

        var auth: FirebaseAuth = Firebase.auth
        var email: String? = null
        // firestore에 데이터 저장
        var db : FirebaseFirestore =  FirebaseFirestore.getInstance()
        var storage : FirebaseStorage =  Firebase.storage
    }
    override fun onCreate() {
        super.onCreate()

        auth = Firebase.auth
        // db = FirebaseFirestore.getInstance()
        // storage = Firebase.storage

    }
}