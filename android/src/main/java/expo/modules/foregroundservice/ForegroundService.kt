package expo.modules.foregroundservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat

class ForegroundService : Service() {

    private fun stopService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null || intent.action == Actions.STOP.toString()) {
            stopService()
            return START_STICKY_COMPATIBILITY
        }

        try {
            val extras = intent.extras ?: Bundle()

            val notificationChannelID = extras.getString("notification.channelId", "expo_foreground_service")
            val notificationChannelName = extras.getString("notification.channelName", "Expo Foreground Service Channel")
            val notificationID = extras.getInt("notification.id", 1)

            val notificationTitle = extras.getString("notification.title", "Foreground Service")
            val notificationDescription = extras.getString("notification.description", "Running")
            val notificationOngoing = extras.getBoolean("notification.ongoing", false)
            val notificationChronometer = extras.getBoolean("notification.chronometer", false)
            val foregroundServiceType = extras.getInt("serviceType", -1)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val serviceChannel = NotificationChannel(
                    notificationChannelID,
                    notificationChannelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(serviceChannel)
            }

            val builder = NotificationCompat.Builder(this, notificationChannelID)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setOngoing(notificationOngoing)
                .setUsesChronometer(notificationChronometer)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setWhen(System.currentTimeMillis())

            ServiceCompat.startForeground(
                this, notificationID, builder.build(),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    foregroundServiceType
                } else {
                    0
                },
            )

            val eventIntent = Intent("expo.modules.foregroundservice.ACTION_SERVICE_EVENT")
            eventIntent.putExtra("event", Events.ON_START)
            sendBroadcast(eventIntent)
            return START_NOT_STICKY
        } catch (error: Exception) {
            val eventIntent = Intent("expo.modules.foregroundservice.ACTION_SERVICE_EVENT")
            eventIntent.putExtra("event", Events.ON_ERROR)
            eventIntent.putExtra("error", error.message)
            sendBroadcast(eventIntent)
            return START_STICKY_COMPATIBILITY
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    enum class Actions {
        START, STOP
    }

    companion object {
        fun start(context: Context, config: Map<String, Any>) {
            val bundle = Bundle().apply {
                config.forEach { (key, value) ->
                    when (value) {
                        is String -> putString(key, value)
                        is Int -> putInt(key, value)
                        is Boolean -> putBoolean(key, value)
                        is Long -> putLong(key, value)
                        is Double -> putDouble(key, value)
                        is Float -> putFloat(key, value)
                        is Short -> putShort(key, value)
                        is Char -> putChar(key, value)
                        else -> {
                            Log.w("ForegroundService", "Unsupported type for key: $key")
                        }
                    }
                }
            }

            val intent = Intent(context, ForegroundService::class.java).apply {
                action = Actions.START.toString()
                putExtras(bundle)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, ForegroundService::class.java).apply {
                action = Actions.STOP.toString()
            }
            context.stopService(intent)
        }
    }
}