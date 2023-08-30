package com.example.movieflixcronet.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import java.nio.channels.Channels
import java.nio.charset.Charset
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Singleton
class MovieDataSource @Inject constructor(
    private val cronetEngine: CronetEngine,
    private val executor: Executor
) {

    private val bytesReceived = ByteArrayOutputStream()
    private val receiveChannel = Channels.newChannel(bytesReceived)

    suspend fun getPopularMovies(): List<Movie> {

        val url =
            "${Constants.BASE_URL}/movie/popular?api_key=${Constants.API_KEY}&page=${Constants.PAGE}"

        return suspendCoroutine { continuation ->
            val request =
                cronetEngine.newUrlRequestBuilder(url, object : UrlRequest.Callback() {

                    override fun onRedirectReceived(
                        request: UrlRequest?,
                        info: UrlResponseInfo?,
                        newLocationUrl: String?
                    ) {
                        request?.followRedirect()

                    }

                    override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
                        Log.i(ReadToMemoryCronetCallback.TAG, "****** Response Started ******")
                        Log.i(
                            ReadToMemoryCronetCallback.TAG,
                            "*** Headers Are *** ${info?.allHeaders}"
                        )

                        // One must use a *direct* byte buffer when calling the read method.
                        request?.read(ByteBuffer.allocateDirect(ReadToMemoryCronetCallback.BYTE_BUFFER_CAPACITY_BYTES))
                    }

                    override fun onReadCompleted(
                        request: UrlRequest,
                        info: UrlResponseInfo,
                        byteBuffer: ByteBuffer
                    ) {

                        byteBuffer.flip()
                        receiveChannel.write(byteBuffer)

                        // Reset the buffer to prepare it for the next read
                        byteBuffer?.clear()
                        request?.read(byteBuffer)
                        Log.d("ResponseCompleted", bytesReceived.toString())

                    }

                    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                    override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo?) {

                        Log.d(
                            "ResponseSucceed",
                            String(bytesReceived.toByteArray(), Charset.defaultCharset())
                        )
                        // You can also invoke the callback with the complete response

                        val data = String(bytesReceived.toByteArray(), Charset.defaultCharset())

                        try {
                            val movieResponse =
                                Gson().fromJson(data, MovieResponse::class.java)
                            continuation.resume(movieResponse.results)
                            Log.d("MovieResponseSucceed", movieResponse.results.toString())
                        } catch (e: JsonSyntaxException) {
                            continuation.resume(emptyList())
                            Log.d("MovieResponseSucceed", e.toString())
                        }
                    }


//                    override fun onSucceeded(
//                        request: UrlRequest,
//                        info: UrlResponseInfo,
//                        bodyBytes: ByteArray
//                    ) {
//
//                        val responseString = String(bodyBytes)
//                        Log.d("MovieResponseString", responseString)
//
//                        try {
//                            val movieResponse =
//                                Gson().fromJson(responseString, MovieResponse::class.java)
//                            continuation.resume(movieResponse.results)
//                            Log.d("MovieResponse", movieResponse.results.toString())
//                        } catch (e: JsonSyntaxException) {
//                            continuation.resume(emptyList())
//                            Log.d("MovieResponseSucceed", e.toString())
//                        }
//                    }

                    override fun onFailed(
                        request: UrlRequest,
                        info: UrlResponseInfo,
                        error: CronetException
                    ) {
                        continuation.resume(emptyList())
                        Log.d("MovieResponse", error.toString())
                    }
                }, executor)

            request.build().start()
        }
    }
}

