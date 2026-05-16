package com.carplay.feature.autoaudio

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.carplay.core.datastore.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Manages DataStore preferences specific to the Auto-Audio feature.
 * Keys are namespaced with "auto_audio_" to avoid collisions with other features.
 */
class AutoAudioPreferences(private val appDataStore: AppDataStore) {

    private val store = appDataStore.store

    companion object {
        private val KEY_MASTER_TOGGLE = booleanPreferencesKey("auto_audio_master_toggle")
        private val KEY_AUDIO_URI = stringPreferencesKey("auto_audio_uri")
    }

    /** Flow of the master toggle state. Defaults to false. */
    val masterToggleFlow: Flow<Boolean> = store.data.map { prefs ->
        prefs[KEY_MASTER_TOGGLE] ?: false
    }

    /** Flow of the persisted audio file URI string. Null if not set. */
    val audioUriFlow: Flow<String?> = store.data.map { prefs ->
        prefs[KEY_AUDIO_URI]
    }

    suspend fun setMasterToggle(enabled: Boolean) {
        store.edit { it[KEY_MASTER_TOGGLE] = enabled }
    }

    suspend fun setAudioUri(uri: String?) {
        store.edit { prefs ->
            if (uri != null) {
                prefs[KEY_AUDIO_URI] = uri
            } else {
                prefs.remove(KEY_AUDIO_URI)
            }
        }
    }

    /**
     * Blocking read used by the Service/Receiver where a Flow isn't practical.
     * Returns (masterToggleEnabled, audioUriString?).
     */
    suspend fun readSync(): Pair<Boolean, String?> {
        val prefs = store.data.first()
        return Pair(
            prefs[KEY_MASTER_TOGGLE] ?: false,
            prefs[KEY_AUDIO_URI]
        )
    }
}
