package com.example.captour

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// https://apis.data.go.kr/B551011/PhotoGalleryService1/galleryList1?serviceKey=APKTrp0XMZTlReSionHVfAbVsgefp6rmsviSNGmE5MndTP43LqhqvSm2n7Qj%2B2GQ3TpsgbH%2FKaUWDEMV5ApISg%3D%3D&arrange=A&MobileOS=ETC&MobileApp=CapTour&numOfRows=10&pageNo=1

/*
numOfRows	한 페이지 결과 수
pageNo	페이지 번호
MobileOS	OS 구분
MobileApp	서비스명
serviceKey	인증키
_type	응답메세지 형식
arrange	정렬 구분
numOfRows	한 페이지 결과 수
pageNo	페이지 번호
MobileOS	OS 구분
MobileApp	서비스명
serviceKey	인증키
_type	응답메세지 형식
arrange	정렬 구분
 */

// https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&q=%EA%B1%B0%EB%8F%88%EC%82%AC%EC%A7%80&type=video&key=AIzaSyBmdUaOHy8OiRw3cNttYLul8Sa4m7NhVP4
interface NetworkService {

    @GET("galleryList1")
    fun getXmlList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") MobileOS: String,
        @Query("MobileApp") MobileApp: String,
        @Query("serviceKey") serviceKey: String,
        @Query("arrange") arrange: String
    ): Call<XmlResponse>

    @GET("gallerySearchList1")
    fun getSearchXmlList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") MobileOS: String,
        @Query("keyword") keyword: String,
        @Query("MobileApp") MobileApp: String,
        @Query("serviceKey") serviceKey: String,
        @Query("arrange") arrange: String
    ): Call<XmlResponse>

    @GET("search")
    fun searchVideos(
        @Query("key") apiKey: String,
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 25
    ): Call<YoutubeJsonResponse>
}