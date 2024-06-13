package com.example.captour

data class CommunityData (
    // 로그인 이메일, 제목, 본문, 스타, 입력 시간
    var docId: String? = null,
    var email: String? = null,
    var title: String? = null,
    var content: String? = null,
    var stars: Float = 0.0f,
    var date_time: String? = null
)