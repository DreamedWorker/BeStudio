package icu.bluedream.bestudio.backend

import android.content.Context
import android.content.Intent
import icu.bluedream.bestudio.frontend.ErrorReportActivity
import java.io.PrintWriter
import java.io.StringWriter

class CrashHandler internal constructor(private val context: Context) :
    Thread.UncaughtExceptionHandler {

    private var defaultUEH: Thread.UncaughtExceptionHandler? = null

    init {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val stackTrace = getStackTrace(throwable)
        val intent = Intent(context, ErrorReportActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("ERROR_MESSAGE", stackTrace)
        }
        context.startActivity(intent)
        defaultUEH?.uncaughtException(thread, throwable)
    }

    private fun getStackTrace(throwable: Throwable): String {
        val sw = StringWriter()
        throwable.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }
}