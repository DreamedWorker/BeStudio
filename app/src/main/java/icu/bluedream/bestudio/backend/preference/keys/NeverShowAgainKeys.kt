package icu.bluedream.bestudio.backend.preference.keys

import androidx.datastore.preferences.core.booleanPreferencesKey

/**
 * 定义一系列针对弹窗的下次不再弹出的存储key，它们的类型都是bool。
 * 它们的默认值应当都是`true`。
 */
object NeverShowAgainKeys {
    val ERROR_REPORTER_WHAT_IS_IT = booleanPreferencesKey("error_reporter_what_is_it")
}