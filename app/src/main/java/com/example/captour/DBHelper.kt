package com.example.captour

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, "captourdb", null, 1) { // captourdb 데이터베이스 생성
    override fun onCreate(db: SQLiteDatabase?) { //db 변수를 통해서 sqlite에서 하고싶은 명령어를 이용하면 됨
        db?.execSQL("create table captour_db (_id integer primary key autoincrement, data not null)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS captour_db;")
        onCreate(db)
    }
}