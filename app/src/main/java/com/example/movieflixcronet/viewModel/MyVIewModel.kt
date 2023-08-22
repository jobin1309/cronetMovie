package com.example.movieflixcronet.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieflixcronet.data.Movie
import com.example.movieflixcronet.repo.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import org.chromium.net.CronetEngine
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    val moviesLiveData: LiveData<List<Movie>> = repository.getPopularMovies()
}


