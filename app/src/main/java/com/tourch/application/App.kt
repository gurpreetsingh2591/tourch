package com.tourch.application

import android.app.Application
import com.tourch.utils.SharedPreferencesUtils
import java.io.File

class App : Application() {
    private var currentApplication: App? = null
    override fun onCreate() {
        super.onCreate()
        currentApplication = this
        SharedPreferencesUtils.init(applicationContext)


    }

    fun getInstance(): App? {

        return currentApplication
    }



    fun freeMemory() {
        try {
            val dir: File = getCacheDir()
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        System.runFinalization()
        Runtime.getRuntime().gc()
        System.gc()
    }

    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }
}