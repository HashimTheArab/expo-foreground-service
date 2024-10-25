package expo.modules.foregroundservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ExpoForegroundServiceModule : Module() {

  private val serviceStatusReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      val event = intent?.getStringExtra("event")
      val error = intent?.getStringExtra("error")

      if (event == Events.ON_START) {
        this@ExpoForegroundServiceModule.sendEvent(event, null)
      } else if (event == Events.ON_ERROR) {
        this@ExpoForegroundServiceModule.sendEvent(event, mapOf("error" to error))
      }
    }
  }

  override fun definition() = ModuleDefinition {
    Name("ExpoForegroundService")

    // Register the receiver when the module is created
    OnCreate {
      val context: Context = appContext.reactContext ?: throw IllegalStateException("React context is not available")
      val filter = IntentFilter("expo.modules.foregroundservice.ACTION_SERVICE_EVENT")
      ContextCompat.registerReceiver(
        context,
        serviceStatusReceiver,
        filter,
        ContextCompat.RECEIVER_NOT_EXPORTED
      )
    }

    // Unregister the receiver when the module is destroyed
    OnDestroy {
      val context: Context = appContext.reactContext ?: throw IllegalStateException("React context is not available")
      context.unregisterReceiver(serviceStatusReceiver)
    }

    // Defines event names that the module can send to JavaScript.
    Events(Events.ON_START, Events.ON_ERROR)

    // Method to start the foreground service
    AsyncFunction("startService") { config: Map<String, Any>, promise: Promise ->
      val context: Context = appContext.reactContext ?: throw IllegalStateException("React context is not available")
      ForegroundService.start(context, config)
      promise.resolve(null)
    }

    // Method to stop the foreground service
    AsyncFunction("stopService") { promise: Promise ->
      val context: Context = appContext.reactContext ?: throw IllegalStateException("React context is not available")
      ForegroundService.stop(context)
      promise.resolve(null)
    }

  }
}

object Events {
  const val ON_START = "onStart"
  const val ON_ERROR = "onError"
}