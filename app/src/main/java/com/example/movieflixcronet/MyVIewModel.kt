package com.example.movieflixcronet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movieflixcronet.data.CronetApi
import com.example.movieflixcronet.data.Movie
import com.example.movieflixcronet.data.MovieResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.chromium.net.CronetEngine

class MyViewModel(cronetEngine: CronetEngine) : ViewModel() {

    private val cronetApi = CronetApi(cronetEngine)

    // LiveData for holding the parsed response data
    private val _moviesLiveData = MutableLiveData<List<Movie>>()
    val moviesLiveData: LiveData<List<Movie>>
        get() = _moviesLiveData

    fun fetchMovies(apiUrl: String, apiKey: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            cronetApi.makeApiRequest(apiKey, page, object : CronetApi.ApiResponseCallback {
                override fun onSuccess(responseData: ByteArray) {
                    val responseString = String(responseData)

                    try {
                        // Parse the JSON response into MovieResponse using Gson
                        val movieResponse = Gson().fromJson(responseString, MovieResponse::class.java)

                        // Update LiveData with the list of movies
                        _moviesLiveData.postValue(movieResponse.results)
                    } catch (e: JsonSyntaxException) {
                        // Handle JSON parsing error
                        _moviesLiveData.postValue(emptyList()) // Set LiveData to empty list
                    }
                }
                override fun onFailure(error: Throwable) {
                    // Handle API request failure
                    _moviesLiveData.postValue(emptyList()) // Set LiveData to empty list
                }
            })
        }
    }
}


class MyViewModelFactory(private val cronetEngine: CronetEngine) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyViewModel(cronetEngine) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
