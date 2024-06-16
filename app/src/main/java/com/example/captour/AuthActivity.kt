package com.example.captour

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.PreferenceManager
import com.example.captour.databinding.ActivityAuthBinding
import com.example.captour.databinding.ItemRecyclerviewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfile
import com.navercorp.nid.profile.data.NidProfileResponse

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)

        changeVisibility(intent.getStringExtra("status").toString())

        binding.goSignInBtn.setOnClickListener {
            changeVisibility("signin")
        }

        binding.signBtn.setOnClickListener {
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()

            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if(task.isSuccessful) {
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener {sendTask ->
                                if(sendTask.isSuccessful) {
                                    Toast.makeText(baseContext, "회원가입 성공!! 메일을 확인해주세요", Toast.LENGTH_SHORT).show()
                                    Log.d("mobileapp", "회원가입 성공")
                                    changeVisibility("logout")
                                }
                                else {
                                    Toast.makeText(baseContext, "메일 발송 실패", Toast.LENGTH_SHORT).show()
                                    Log.d("mobileapp", "메일 발송 실패")
                                    changeVisibility("logout")
                                }
                            }
                    } else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        Log.d("mobileapp", "== ${task.exception} ==")
                        changeVisibility("logout")
                    }
                }
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task->
                    binding.authPasswordEditView.text.clear()
                    binding.authEmailEditView.text.clear()
                    if(task.isSuccessful) {
                        if(MyApplication.checkAuth()) {
                            MyApplication.email = email
                            Log.d("mobileapp", "로그인 성공")
                            finish()
                        } else {
                            Toast.makeText(baseContext, "이메일 인증이 되지 않았습니다", Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "이메일 인증 안됨")
                        }
                    } else {
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                        Log.d("mobileapp", "로그인 실패")
                    }
                }


        }

        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            // 네이버 로그아웃
            // NaverIdLoginSDK.logout()
            MyApplication.email = null
            // changeVisivility("login")
            Log.d("mobileapp", "로그 아웃")
            finish()
        }


        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data) // 이메올로 로그인 한 경우 값을 받음
            Log.d("mobileapp","account1 : ${task.toString()}")
            //Log.d("mobileapp","account2 : ${task.result}")
            try{
                val account = task.getResult(ApiException::class.java) // 로그인 한 경우 로그아웃 하지 않는 이상 로그인 된 상태임 - 신용으로 계속해서 로그인이 유지되도록 함
                val crendential = GoogleAuthProvider.getCredential(account.idToken, null)
                MyApplication.auth.signInWithCredential(crendential)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            MyApplication.email = account.email // 구글 이메일이 됨
                            Toast.makeText(baseContext,"구글 로그인 성공",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "구글 로그인 성공")
                            finish() // 인증 완료
                        }
                        else{
                            changeVisibility("logout")
                            Toast.makeText(baseContext,"구글 로그인 실패",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "구글 로그인 실패")
                        }
                    }
            }catch (e: ApiException){ // APIException은 이미 지정된 exception말고 custom한 exception을 만들어서 쓰고 싶을때 사용
                changeVisibility("logout")
                Toast.makeText(baseContext,"구글 로그인 Exception : ${e.printStackTrace()},${e.statusCode}",Toast.LENGTH_SHORT).show()
                Log.d("mobileapp", "구글 로그인 Exception : ${e.message}, ${e.statusCode}")
            }
        }

        binding.googleLoginBtn.setOnClickListener { // 구글인증 Button
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_client_id))
                .requestEmail()
                .build()
            val signInIntent = GoogleSignIn.getClient(this,gso).signInIntent
            requestLauncher.launch(signInIntent)
        }

        /*
        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            Log.d("mobileapp","account1 : ${task.toString()}")
            //Log.d("mobileapp","account2 : ${task.result}")
            try{
                val account = task.getResult(ApiException::class.java)
                val crendential = GoogleAuthProvider.getCredential(account.idToken, null)
                MyApplication.auth.signInWithCredential(crendential)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            MyApplication.email = account.email
                            Toast.makeText(baseContext,"구글 로그인 성공",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "구글 로그인 성공")
                            finish()
                        }
                        else{
                            changeVisibility("logout")
                            Toast.makeText(baseContext,"구글 로그인 실패",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "구글 로그인 실패")
                        }
                    }
            }catch (e: ApiException){ // APIException은 이미 지정된 exception말고 custom한 exception을 만들어서 쓰고 싶을때 사용
                changeVisibility("logout")
                Toast.makeText(baseContext,"구글 로그인 Exception : ${e.printStackTrace()},${e.statusCode}",Toast.LENGTH_SHORT).show()
                Log.d("mobileapp", "구글 로그인 Exception : ${e.message}, ${e.statusCode}")
            }
        }

        binding.googleLoginBtn.setOnClickListener { // 구글인증 Button
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_client_id))
                .requestEmail()
                .build()
            val signInIntent = GoogleSignIn.getClient(this,gso).signInIntent
            requestLauncher.launch(signInIntent)
        }
        */

        // 네이버 로그인
        /*
        binding.naverLoginBtn.setOnClickListener {
            val oAuthLoginCallback = object: OAuthLoginCallback {
                override fun onError(errorCode: Int, message: String) {

                }

                override fun onFailure(httpStatus: Int, message: String) {

                }

                override fun onSuccess() {
                    // 네이버 로그인 API 호출 성공 시 유저 정보를 가져옴
                    NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                        override fun onSuccess(result: NidProfileResponse) { /// 사용자의 이메일 정보가 NidProfileResponse 에 담김 -> email에 담음
                            MyApplication.email = result.profile?.email.toString()
                            finish()
                        }

                        override fun onError(errorCode: Int, message: String) {
                            TODO("Not yet implemented")
                        }

                        override fun onFailure(httpStatus: Int, message: String) {
                            TODO("Not yet implemented")
                        }
                    })
                }

            }
            NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), "Captour")
            NaverIdLoginSDK.authenticate(this, oAuthLoginCallback)
        }

         */

    }

    fun changeVisibility(mode:String){
        if(mode.equals("login")){
            binding.run{
                authMainTextView.text = "정말 로그아웃하시겠습니까?"
                authMainTextView.visibility = View.VISIBLE
                logoutBtn.visibility = View.VISIBLE
                goSignInBtn.visibility = View.GONE
                authEmailEditView.visibility = View.GONE
                authPasswordEditView.visibility = View.GONE
                signBtn.visibility = View.GONE
                loginBtn.visibility= View.GONE
                googleLoginBtn.visibility = View.GONE
                // naverLoginBtn.visibility = View.GONE
            }
        }
        else if(mode.equals("logout")){
            binding.run{
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                authMainTextView.visibility = View.VISIBLE
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE
                loginBtn.visibility= View.VISIBLE
                googleLoginBtn.visibility = View.VISIBLE
                // naverLoginBtn.visibility = View.VISIBLE
            }
        }else if(mode.equals("signin")){
            binding.run{
                authMainTextView.visibility = View.GONE
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility= View.GONE
                googleLoginBtn.visibility = View.GONE
                // naverLoginBtn.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // 글자 크기 설정
        val fontSize = sharedPreference.getInt("font_size", 16)

        // 색상 설정
        val color = sharedPreference.getString("color", "#363C90")
        val colorCode = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorCode)
        binding.goSignInBtn.setBackgroundColor(colorCode)
        binding.loginBtn.setBackgroundColor(colorCode)
        binding.googleLoginBtn.setBackgroundColor(colorCode)
        binding.logoutBtn.setBackgroundColor(colorCode)
        binding.naverLoginBtn.setBackgroundColor(colorCode)
        binding.signBtn.setBackgroundColor(colorCode)

        val recyclerView = ItemRecyclerviewBinding.inflate(layoutInflater)
        recyclerView.title.textSize = fontSize / 16.0f
    }
}