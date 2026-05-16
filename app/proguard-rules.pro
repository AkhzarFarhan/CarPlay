# Proguard rules for CarPlay
# Keep the Application class
-keep class com.carplay.CarPlayApplication { *; }

# Keep BroadcastReceiver
-keep class com.carplay.feature.autoaudio.CarModeReceiver { *; }

# Keep Service
-keep class com.carplay.feature.autoaudio.AudioPlayerService { *; }
