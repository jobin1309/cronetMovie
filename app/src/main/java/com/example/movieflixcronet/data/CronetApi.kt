package com.example.movieflixcronet.data

import com.google.gson.Gson
import com.quintetsolutions.qalert.utils.Constants.BASE_URL
import org.chromium.net.CronetEngine
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.Executors


class CronetApi(private val cronetEngine: CronetEngine) {

    interface ApiResponseCallback {
        fun onSuccess(responseData: ByteArray)
        fun onFailure(error: Throwable)
    }

    fun makeApiRequest(apiKey: String, page: Int, callback: ApiResponseCallback) {
        val baseUrl = BASE_URL
        val url = "$baseUrl/movie/popular?api_key=$apiKey&page=$page"

        val request = cronetEngine.newUrlRequestBuilder(url, object : ReadToMemoryCronetCallback() {
            private val responseData = ByteArrayOutputStream()

            override fun onReadCompleted(
                request: UrlRequest,
                info: UrlResponseInfo,
                byteBuffer: ByteBuffer
            ) {
                val byteArray = ByteArray(byteBuffer.remaining())
                byteBuffer.get(byteArray)
                responseData.write(byteArray)
            }


            override fun onSucceeded(
                request: UrlRequest,
                info: UrlResponseInfo,
                bodyBytes: ByteArray
            ) {
                callback.onSuccess(responseData.toByteArray())
            }

            override fun onFailed(
                request: UrlRequest?,
                info: UrlResponseInfo?,
                error: CronetException?
            ) {
                callback.onFailure(error ?: Exception("Unknown error"))
            }
        }, Executors.newSingleThreadExecutor())

        request.build().start()
    }
}



