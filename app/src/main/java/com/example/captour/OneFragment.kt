package com.example.captour

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.captour.databinding.FragmentOneBinding
import com.example.captour.databinding.ItemRecyclerviewBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.io.BufferedReader
import java.io.File
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OneFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var sharedPreference: SharedPreferences
    lateinit var binding: FragmentOneBinding
    // firestore에 데이터 저장
    lateinit var db : FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOneBinding.inflate(inflater, container, false)
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())
        db = FirebaseFirestore.getInstance()


        val datas = mutableListOf<String>()

        // 1. db에 저장하는 방법
        /*
        val db = DBHelper(requireContext()).readableDatabase
        val cursor = db.rawQuery("select * from captour_db", null)
        // Log.d("mobileapp", cursor.toString())
        while(cursor.moveToNext()) {
            datas?.add(cursor.getString(1))
        }
        db.close()


        // db 연결 시 수행했던 작업
        val adapter = MyAdapter(datas)
        binding.recyclerView.adapter=adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager=layoutManager
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            it.data?.getStringExtra("result")?.let {// "result"에 값이 저장되어 있으면(non-null)
                if(it != "") {
                    datas?.add(it)
                    adapter.notifyDataSetChanged()

                    // 파일 읽기 작업 진행
                    val file = File(requireContext().filesDir, "test.txt")
                    val readstream: BufferedReader = file.reader().buffered() // 읽을 준비
                    binding.last.text = "마지막 활동시간 : " + readstream.readLine()
                }
            }
        }

         */

        // 2. firestorage 이용


        // 파일에 저장하기
        val file = File(requireContext().filesDir, "test.txt")

        if (!file.exists()) {
            file.writeText("No data") // 파일이 없으면 생성
        }

        val readstream: BufferedReader = file.reader().buffered() // 읽을 준비
        binding.last.text = "마지막 활동시간 : " + readstream.readLine()


        binding.mainFab.setOnClickListener {
            // 로그인한 사용자만
            if(!MyApplication.checkAuth()) {
                Toast.makeText(requireContext(), "로그인을 먼저 진행해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(requireContext(), AddActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth()) {
            MyApplication.db.collection("review")
                .orderBy("date_time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {result ->
                    val itemList = mutableListOf<CommunityData>()
                    for( document in result){
                        val item = document.toObject(CommunityData::class.java)
                        item.docId = document.id
                        itemList.add(item)
                    }
                    binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
                    binding.recyclerView.adapter = CommunityAdapter(requireContext(), itemList)
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()) }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onResume() {
        super.onResume()

        // 글자 크기 설정
        val fontSize = sharedPreference.getInt("font_size", 16)
        binding.totoTitle.textSize = fontSize + 10f

        // 색상 설정
        val color = sharedPreference.getString("color", "#363C90")
        val colorCode = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorCode)
        binding.totoTitle.setBackgroundColor(colorCode)
        binding.last.setBackgroundColor(colorCode)
        binding.mainFab.setBackgroundColor(colorCode)

        val recyclerView = ItemRecyclerviewBinding.inflate(layoutInflater)
        recyclerView.title.textSize = fontSize / 16.0f
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OneFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OneFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}