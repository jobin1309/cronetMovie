package com.example.movieflixcronet.repo

import androidx.lifecycle.LiveData
import com.example.movieflixcronet.data.Movie
import com.example.movieflixcronet.data.MovieDataSource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieRepository @Inject constructor(private val movieDataSource: MovieDataSource) {

    fun getPopularMovies(): LiveData<List<Movie>> {
        return movieDataSource.getPopularMovies()
    }
}

