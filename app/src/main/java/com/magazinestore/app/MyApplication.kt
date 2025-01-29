package com.magazinestore.app

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object {
        fun getSavedToken(): String? {
            val sharedPreferences = instance.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            return sharedPreferences.getString("auth_token", null)
        }


        lateinit var instance: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getSavedToken(): String? {
        val sharedPreferences = instance.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

}
