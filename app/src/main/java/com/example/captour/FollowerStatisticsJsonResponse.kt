package com.example.captour

data class FollowerStatisticsJsonResponse (
    val message: String,
    val data: List<Statistics>
)

data class Statistics (
    val day: String,
    val followerNum: String
)

/*
{
    message: "주간 통계 조회 완료",
    data: [
            {
                day: 7,
                followerNum: 2
            },
            {
                day: 6,
                followerNum: 2
            },
            {
                day: 5,
                followerNum: 0
            },
            {
                day: 4,
                followerNum: 1
            },
            {
                day: 3,
                followerNum: 0
            },
            {
                day: 2,
                followerNum: 0
            },
            {
                day: 1,
                followerNum: 0
            }
        ]
     }
 */