package com.example.movieflixcronet.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieflixcronet.data.Movie
import com.example.movieflixcronet.data.MovieDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieDataSource: MovieDataSource) : ViewModel() {
    val moviesLiveData: LiveData<List<Movie>> = MutableLiveData()

    init {
        viewModelScope.launch {
            val movies = movieDataSource.getPopularMovies()
            (moviesLiveData as MutableLiveData).value = movies
        }
    }
}


