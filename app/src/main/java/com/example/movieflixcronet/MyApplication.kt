package com.example.movieflixcronet

import android.app.Application
import android.util.Log
import com.google.android.gms.net.CronetProviderInstaller
import com.quintetsolutions.qalert.utils.Constants.LOGGER_TAG
import org.chromium.net.CronetEngine

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        cronetProviderInstalller()
    }

    fun cronetProviderInstalller() {
        CronetProviderInstaller.installProvider(applicationContext).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d(LOGGER_TAG, "Successfully installed Play Services provider: $it")
                // TODO(you): Initialize Cronet engine

                // TODO(you): Initialize the Cronet image downloader
            } else {
                Log.d(LOGGER_TAG, "Unable to load Cronet from Play Services", it.exception)
            }
        }
    }
}