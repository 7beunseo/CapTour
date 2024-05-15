package com.example.captour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.captour.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeVisivility(intent.getStringExtra("status").toString())

        binding.goSignInBtn.setOnClickListener {
            changeVisivility("signin")
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
                                    changeVisivility("logout")
                                }
                                else {
                                    Toast.makeText(baseContext, "메일 발송 실패", Toast.LENGTH_SHORT).show()
                                    Log.d("mobileapp", "메일 발송 실패")
                                    changeVisivility("logout")
                                }
                            }
                    } else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        Log.d("mobileapp", "== ${task.exception} ==")
                        changeVisivility("logout")
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
            MyApplication.email = null
            // changeVisivility("login")
            Log.d("mobileapp", "로그 아웃")
            finish()
        }

        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        }

        binding.googleLoginBtn.setOnClickListener {
            // val intent
            // requestLauncher.launch(intent)
        }
    }

    fun changeVisivility(mode:String){
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
            }
        }
    }
}