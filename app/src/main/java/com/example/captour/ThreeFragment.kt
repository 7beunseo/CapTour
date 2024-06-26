package com.example.captour

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import com.example.captour.databinding.FragmentThreeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ThreeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentThreeBinding
    lateinit var sharedPreference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentThreeBinding.inflate(inflater, container, false)

        binding.setting.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        binding.followingList.setOnClickListener {
            val intent = Intent(requireContext(), FollowingListActivity::class.java)
            startActivity(intent)
        }

        binding.followerList.setOnClickListener {
            val intent = Intent(requireContext(), FollowerListActivity::class.java)
            startActivity(intent)
        }

        val status = binding.login
        status.setOnClickListener {
            Log.d("mobileapp", "button.setOnClickListener")
            val intent = Intent(requireContext(), AuthActivity::class.java)

            if(status.text.equals("로그인"))  {
                intent.putExtra("status", "logout")
                binding.forMe.visibility = View.VISIBLE
                binding.followerList.visibility = View.VISIBLE
                binding.followingList.visibility = View.VISIBLE
                binding.setting.visibility = View.VISIBLE
            } else if(status.text.equals("로그아웃")) {
                intent.putExtra("status", "login")
                binding.forMe.visibility = View.GONE
                binding.followerList.visibility = View.GONE
                binding.followingList.visibility = View.GONE
                binding.setting.visibility = View.GONE
            }
            startActivity(intent)
        }

        /*
        Log.d("mobileapp", binding.login.text.toString())
        if(binding.login.text.equals("로그아웃")) {
            binding.forMe.visibility = View.GONE
            binding.followerList.visibility = View.GONE
            binding.followingList.visibility = View.GONE
            binding.setting.visibility = View.GONE
        } else {
            binding.forMe.visibility = View.VISIBLE
            binding.followerList.visibility = View.VISIBLE
            binding.followingList.visibility = View.VISIBLE
            binding.setting.visibility = View.VISIBLE
        }
         */

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ThreeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ThreeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStart() {
        super.onStart()

        // 처리
        val status = binding.login
        val user = binding.user

        Log.d("mobileapp", MyApplication.email.toString())
        // if(MyApplication.checkAuth() || MyApplication.email  != null) {
        if(MyApplication.checkAuth()) {
                status.text = "로그아웃"
                user.text = "${MyApplication.email}님\n반갑습니다"
            } else {
                status.text = "로그인"
                user.text = "안녕하세요"
            }
        }



    override fun onResume() {
        super.onResume()
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val fontSize = sharedPreference.getInt("font_size", 16)
        var myMemo = ""
        if(MyApplication.checkAuth()) {
            myMemo = sharedPreference.getString("myMemeo", "\"${MyApplication.email}님\n 위치와 시간을 검색할 기준이 되는 출발지를 입력해주세요\"")
                .toString()
        } else {
            myMemo = "로그인을 진행하여 내 주소를 남겨보세요"
        }
        binding.setting.textSize = fontSize + 1f
        binding.user.textSize = fontSize + 1f
        binding.login.textSize = fontSize + 1f
        binding.forMe.textSize = fontSize + 1f
        binding.followingList.textSize = fontSize + 1f
        binding.followerList.textSize = fontSize + 1f

        binding.forMe.text = myMemo

        // 글자 크기 설정
        binding.totoTitle.textSize = fontSize + 10f

        // 색상 설정
        val color = sharedPreference.getString("color", "#363C90")
        val colorCode = Color.parseColor(color)
        binding.totoTitle.setBackgroundColor(colorCode)

        // 폰트 굵기 설정
        val fontStyle = sharedPreference.getString("font_style", "regular")

        var typeface: Typeface?
        if(fontStyle == "regular") {
            typeface = ResourcesCompat.getFont(requireContext(), R.font.nanum_regular)
        } else {
            typeface = ResourcesCompat.getFont(requireContext(), R.font.nanum_bold)
        }
        binding.followerList.typeface = typeface
        binding.login.typeface = typeface
        binding.forMe.typeface = typeface
        binding.setting.typeface = typeface
        binding.followingList.typeface = typeface
        binding.totoTitle.typeface = typeface
        binding.user.typeface = typeface
    }
}