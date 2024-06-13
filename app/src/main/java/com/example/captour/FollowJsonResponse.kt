package com.example.captour

data class FollowJsonResponse (
    val message: String,
    val data: List<Follow>
)

data class Follow (
    val follower: String,
    val following: String
)

/*
    {
    message: "팔로우 조회 완료",
    data: [
        {
            follower: "kimes",
            following: "kimtj"
        },
        {
            follower: "kimes",
            following: "kimtj"
        },
        {
            follower: "kimes",
            following: "kimtj"
        }
      ]
    }
 */