package com.example.movieflixcronet.data

interface MovieDownloader {
    suspend fun downloadMovie(urlString: String): MovieResponse
}