package com.example.captour

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.captour.databinding.FragmentTwoBinding
import com.example.captour.databinding.ItemMainBinding
import com.example.captour.databinding.ItemRecyclerviewBinding
import com.google.android.gms.tasks.OnCompleteListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.util.concurrent.Executors

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TwoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : FragmentTwoBinding
    lateinit var sharedPreference: SharedPreferences
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

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
        binding = FragmentTwoBinding.inflate(inflater, container, false)
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getLastLocation()
        }

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

    // 위치 정보 얻기
    fun getLastLocation(): String {
        var data: String = ""
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return "Failed"
        }
        fusedLocationClient.lastLocation
            .addOnCompleteListener(Executors.newSingleThreadExecutor(), OnCompleteListener<Location> { task ->
                if (task.isSuccessful && task.result != null) {
                    val location: Location = task.result
                    val latitude = location.latitude
                    val longitude = location.longitude
                    data = getAddressFromLocation(latitude, longitude)

                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
                    }
                    data = ""
                }
            })
        return data
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        var data: String = "Address not found"
        Handler(Looper.getMainLooper()).post {
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses[0].getAddressLine(0)
                    Toast.makeText(requireContext(), address, Toast.LENGTH_LONG).show()
                    binding.currentLocation.text = "현재 위치 : " + address
                    data = address

                    val file = File(requireContext().filesDir, "location.txt")

                    // 파일이 존재하지 않으면 초기화
                    if (!file.exists()) {
                        file.createNewFile()
                        file.writeText("뒷골 1로 42")
                    }

                    val writestream = file.bufferedWriter()
                    writestream.write(data.toString())
                    writestream.flush()
                    writestream.close()

                } else {
                    Toast.makeText(requireContext(), "Address not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return data
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
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
        binding.currentLocation.setBackgroundColor(colorCode)

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
        binding.currentLocation.typeface = typeface
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