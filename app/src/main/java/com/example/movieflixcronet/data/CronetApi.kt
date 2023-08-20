package com.example.movieflixcronet.data

import com.google.gson.Gson
import com.quintetsolutions.qalert.utils.Constants.BASE_URL
import org.chromium.net.CronetEngine
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

class CronetApi(private val cronetEngine: CronetEngine) {

    interface ApiResponseCallback {
        fun onSuccess(responseData: ByteArray)
        fun onFailure(error: Throwable)
    }

    fun makeApiRequest(url: String, callback: ApiResponseCallback) {
        val request = cronetEngine.newUrlRequestBuilder(url, object : ReadToMemoryCronetCallback() {
            override fun onSucceeded(
                request: UrlRequest,
                info: UrlResponseInfo,
                bodyBytes: ByteArray
            ) {
                callback.onSuccess(bodyBytes)
            }

            override fun onFailed(
                request: UrlRequest?,
                info: UrlResponseInfo?,
                error: CronetException?
            ) {
            }
        }, Executors.newSingleThreadExecutor())

        request.build().start()
    }


}





