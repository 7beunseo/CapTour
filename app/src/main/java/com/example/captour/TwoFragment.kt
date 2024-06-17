package com.example.captour

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
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.captour.databinding.FragmentTwoBinding
import com.example.captour.databinding.ItemMainBinding
import com.example.captour.databinding.ItemRecyclerviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TwoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : FragmentTwoBinding
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
        binding = FragmentTwoBinding.inflate(inflater, container, false)
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())


        val call: Call<XmlResponse> = RetrofitConnection.xmlNetworkServ.getXmlList(
            10,
            1,
            "ETC",
            "CapTour",
            "APKTrp0XMZTlReSionHVfAbVsgefp6rmsviSNGmE5MndTP43LqhqvSm2n7Qj+2GQ3TpsgbH/KaUWDEMV5ApISg==",
            "A"
        )


        call?.enqueue(object: Callback<XmlResponse> {
            override fun onResponse(call: Call<XmlResponse>, response: Response<XmlResponse>) {
                Log.d("mobileApp", "$response")
                Log.d("mobileapp", "${response.body()}")
                binding.xmlRecyclerView.adapter = XmlAdapter(response.body()?.body!!.items!!.item)
                binding.xmlRecyclerView.layoutManager = LinearLayoutManager(activity)
                binding.xmlRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
            }

            override fun onFailure(call: Call<XmlResponse>, t: Throwable) {
                Log.d("mobileApp", "onFalure ${call.request()}")
            }

        })

        binding.btnSearch.setOnClickListener {
            val searchText = binding.search.text.toString()
            val call: Call<XmlResponse> = RetrofitConnection.xmlNetworkServ.getSearchXmlList(
                10,
                1,
                "ETC",
                searchText,
                "CapTour",
                "APKTrp0XMZTlReSionHVfAbVsgefp6rmsviSNGmE5MndTP43LqhqvSm2n7Qj+2GQ3TpsgbH/KaUWDEMV5ApISg==",
                "A"
            )

            Log.d("mobileapp", searchText)

            call?.enqueue(object: Callback<XmlResponse> {
                override fun onResponse(call: Call<XmlResponse>, response: Response<XmlResponse>) {
                    Log.d("mobileApp", "$response")
                    Log.d("mobileapp", "${response.body()}")
                    binding.xmlRecyclerView.adapter = XmlAdapter(response.body()?.body!!.items!!.item)
                    binding.xmlRecyclerView.layoutManager = LinearLayoutManager(activity)
                    binding.xmlRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
                }

                override fun onFailure(call: Call<XmlResponse>, t: Throwable) {
                    Log.d("mobileApp", "onFalure ${call.request()}")
                }

            })
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // 글자 크기 설정
        val fontSize = sharedPreference.getInt("font_size", 16)
        binding.totoTitle.textSize = fontSize + 10f
        binding.btnSearch.textSize = fontSize + 1f
        binding.search.textSize = fontSize + 1f

        // 색상 설정
        val color = sharedPreference.getString("color", "#363C90")
        val colorCode = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorCode)
        binding.totoTitle.setBackgroundColor(colorCode)
        binding.btnSearch.setBackgroundColor(colorCode)

        // 폰트 굵기 설정
        val fontStyle = sharedPreference.getString("font_style", "regular")

        var typeface: Typeface?
        if(fontStyle == "regular") {
            typeface = ResourcesCompat.getFont(requireContext(), R.font.nanum_regular)
        } else {
            typeface = ResourcesCompat.getFont(requireContext(), R.font.nanum_bold)
        }
        binding.search.typeface = typeface
        binding.btnSearch.typeface = typeface
        binding.totoTitle.typeface = typeface
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TwoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TwoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}