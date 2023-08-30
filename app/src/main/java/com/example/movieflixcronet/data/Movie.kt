package com.example.movieflixcronet.data

import com.google.gson.annotations.SerializedName


data class Movie(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("poster_path")
    var posterPath: String? = null,
    @SerializedName("release_date")
    var releaseDate: String? = null,
    @SerializedName("title")
    var title: String? = null
)