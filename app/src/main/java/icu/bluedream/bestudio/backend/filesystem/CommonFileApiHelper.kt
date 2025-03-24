package icu.bluedream.bestudio.backend.filesystem

import java.io.File

object CommonFileApiHelper {
    fun checkWorklistDir(prefix: String) {
        val fullPath = File("${prefix}/")
        if (!fullPath.exists()) {
            fullPath.mkdir()
        }
    }
}