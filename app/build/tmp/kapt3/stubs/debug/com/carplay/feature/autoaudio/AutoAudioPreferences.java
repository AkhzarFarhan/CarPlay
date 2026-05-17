package com.carplay.feature.autoaudio;

/**
 * Manages DataStore preferences specific to the Auto-Audio feature.
 * Keys are namespaced with "auto_audio_" to avoid collisions with other features.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u0007\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0010\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0012J\u0018\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0007H\u0086@\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\u0019R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\tR\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/carplay/feature/autoaudio/AutoAudioPreferences;", "", "appDataStore", "Lcom/carplay/core/datastore/AppDataStore;", "(Lcom/carplay/core/datastore/AppDataStore;)V", "audioUriFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getAudioUriFlow", "()Lkotlinx/coroutines/flow/Flow;", "masterToggleFlow", "", "getMasterToggleFlow", "store", "Landroidx/datastore/core/DataStore;", "Landroidx/datastore/preferences/core/Preferences;", "readSync", "Lkotlin/Pair;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setAudioUri", "", "uri", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setMasterToggle", "enabled", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class AutoAudioPreferences {
    @org.jetbrains.annotations.NotNull()
    private final com.carplay.core.datastore.AppDataStore appDataStore = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> store = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> KEY_MASTER_TOGGLE = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> KEY_AUDIO_URI = null;
    
    /**
     * Flow of the master toggle state. Defaults to false.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Boolean> masterToggleFlow = null;
    
    /**
     * Flow of the persisted audio file URI string. Null if not set.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.String> audioUriFlow = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.carplay.feature.autoaudio.AutoAudioPreferences.Companion Companion = null;
    
    public AutoAudioPreferences(@org.jetbrains.annotations.NotNull()
    com.carplay.core.datastore.AppDataStore appDataStore) {
        super();
    }
    
    /**
     * Flow of the master toggle state. Defaults to false.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> getMasterToggleFlow() {
        return null;
    }
    
    /**
     * Flow of the persisted audio file URI string. Null if not set.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getAudioUriFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setMasterToggle(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setAudioUri(@org.jetbrains.annotations.Nullable()
    java.lang.String uri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Blocking read used by the Service/Receiver where a Flow isn't practical.
     * Returns (masterToggleEnabled, audioUriString?).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object readSync(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Boolean, java.lang.String>> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/carplay/feature/autoaudio/AutoAudioPreferences$Companion;", "", "()V", "KEY_AUDIO_URI", "Landroidx/datastore/preferences/core/Preferences$Key;", "", "KEY_MASTER_TOGGLE", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}