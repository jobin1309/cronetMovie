/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.movieflixcronet.data

import android.util.Log
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels

 abstract class ReadToMemoryCronetCallback : UrlRequest.Callback() {
     var responseBody: String? = null

     override fun onRedirectReceived(
        request: UrlRequest, info: UrlResponseInfo?, newLocationUrl: String?
    ) {
        request.followRedirect()
    }

     override fun onResponseStarted(request: UrlRequest, info: UrlResponseInfo) {
        Log.i(TAG, "****** Response Started ******")
        Log.i(TAG, "*** Headers Are *** ${info.allHeaders}")

        // One must use a *direct* byte buffer when calling the read method.
        request.read(ByteBuffer.allocateDirect(BYTE_BUFFER_CAPACITY_BYTES))
    }

     override fun onReadCompleted(
        request: UrlRequest, info: UrlResponseInfo, byteBuffer: ByteBuffer
    ) {

         byteBuffer?.let {
             val byteArray = ByteArray(it.remaining())
             it.get(byteArray)
             String(byteArray, Charsets.UTF_8)
         }?.apply {
             responseBody += this
         }
         byteBuffer?.clear()
         request?.read(byteBuffer)
         Log.d("ResponseComplete", responseBody.toString())


     }




     companion object {
        const val TAG = "ReadToMemoryCronetCallback"
        const val BYTE_BUFFER_CAPACITY_BYTES = 64 * 1024
    }
}

