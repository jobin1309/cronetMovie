package com.example.movieflixcronet.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.quintetsolutions.qalert.utils.Constants
import org.chromium.net.CronetEngine
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieDataSource @Inject constructor(private val cronetEngine: CronetEngine) {

    fun getPopularMovies(): LiveData<List<Movie>> {
        val moviesLiveData = MutableLiveData<List<Movie>>()
        var isReadingComplete = false

        val url = "${Constants.BASE_URL}/movie/popular?api_key=${Constants.API_KEY}&page=${Constants.PAGE}"

        val request = cronetEngine.newUrlRequestBuilder(url, object : UrlRequest.Callback() {
            private val responseData = ByteArrayOutputStream()
            override fun onRedirectReceived(
                request: UrlRequest?,
                info: UrlResponseInfo?,
                newLocationUrl: String?
            ) {
                request?.followRedirect()
            }

            override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
                Log.d("responseStart", "****** Response Started ******")
                Log.d("responseStart", "*** Headers Are *** ${info?.allHeaders}")

                // One must use a *direct* byte buffer when calling the read method.
                if (request != null) {
                    request.read(ByteBuffer.allocateDirect(ReadToMemoryCronetCallback.BYTE_BUFFER_CAPACITY_BYTES))
                }
            }

            override fun onReadCompleted(
                request: UrlRequest,
                info: UrlResponseInfo,
                byteBuffer: ByteBuffer
            ) {
                byteBuffer.flip() // Flip the buffer before reading
                val byteArray = ByteArray(byteBuffer.remaining())
                byteBuffer.get(byteArray)
                responseData.write(byteArray)
                Log.d("responseStart", "****** Response Completed ******")
                Log.d("responseStart", responseData.toString())
                isReadingComplete = true
            }

            override fun onSucceeded(
                request: UrlRequest,
                info: UrlResponseInfo
            ) {

                if (isReadingComplete) {
                    val responseString = String(responseData.toByteArray())

                    try {
                        val movieResponse =
                            Gson().fromJson(responseString, MovieResponse::class.java)
                        moviesLiveData.postValue(movieResponse.results)
                        Log.d("responseStart", movieResponse.results.toString())
                    } catch (e: JsonSyntaxException) {
                        // Handle JSON parsing error
                        moviesLiveData.postValue(emptyList())
                        Log.d("responseStart", e.message.toString())

                    }
                }
                else {
                    // Handle request failure
                    moviesLiveData.postValue(emptyList())
                    Log.d("responseStart", "failed")
                }
            }



            override fun onFailed(
                request: UrlRequest,
                info: UrlResponseInfo?,
                error: CronetException?
            ) {
                // Handle request failure
                moviesLiveData.postValue(emptyList())
                Log.d("responseStart", "failed")

            }
        }, Executors.newSingleThreadExecutor())

        request.build().start()

        return moviesLiveData
    }
}

