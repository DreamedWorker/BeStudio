package icu.bluedream.bestudio.backend.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.preferenceManager: DataStore<Preferences> by preferencesDataStore(name = "studio_pref")


inline fun <reified T> Context.prefGetValue(key: Preferences.Key<T>, defVal: T): Flow<T> =
    this.preferenceManager.data
        .map { pref ->
            pref[key] ?: defVal
        }

suspend inline fun <reified T> Context.prefSetValue(key: Preferences.Key<T>, value: T) {
    this.preferenceManager.edit { pref ->
        pref[key] = value
    }
}