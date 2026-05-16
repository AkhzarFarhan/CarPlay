package com.carplay.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "carplay_settings"
)

/**
 * Shared DataStore singleton. All feature modules access their
 * preferences through this single DataStore instance.
 */
class AppDataStore(context: Context) {
    val store: DataStore<Preferences> = context.dataStore
}
