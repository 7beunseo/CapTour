package com.example.captour

data class YoutubeJsonResponse(
    val items: List<VideoItem>
)

data class VideoItem(
    val id: VideoId,
    val snippet: VideoSnippet
)

data class VideoId(
    val videoId: String
)

data class VideoSnippet(
    val title: String,
    val description: String,
    val thumbnails: VideoThumbnails
)

data class VideoThumbnails(
    val default: Thumbnail,
    val medium: Thumbnail,
    val high: Thumbnail
)

data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)