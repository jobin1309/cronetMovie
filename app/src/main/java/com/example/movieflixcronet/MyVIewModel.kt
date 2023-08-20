package com.example.movieflixcronet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieflixcronet.data.CronetApi
import com.example.movieflixcronet.data.Movie
import com.example.movieflixcronet.data.MovieResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.chromium.net.CronetEngine

class MyViewModel(private val cronetEngine: CronetEngine) : ViewModel() {

    private val cronetApi = CronetApi(cronetEngine)

    // LiveData for holding the parsed response data
    private val _moviesLiveData = MutableLiveData<List<Movie>>()
    val moviesLiveData: LiveData<List<Movie>>
        get() = _moviesLiveData

    fun fetchMovies(apiUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cronetApi.makeApiRequest(apiUrl, object : CronetApi.ApiResponseCallback {
                override fun onSuccess(responseData: ByteArray) {
                    val responseString = String(responseData)

                    // Parse the JSON response into MovieResponse
                    val movieResponse = Gson().fromJson(responseString, MovieResponse::class.java)

                    // Update LiveData with the list of movies
                    _moviesLiveData.postValue(movieResponse.results)
                }
                override fun onFailure(error: Throwable) {
                    // Handle API request failure
                }
            })
        }
    }
}