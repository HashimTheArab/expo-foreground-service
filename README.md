# expo-foreground-service

Provides an API to run foreground services on Android.

## Features

- [x] 🚀 create different types of foreground services to run in the background
- [x] 🎨 customize notifications within the foreground service
- [ ] 🔄 support for multiple foreground services at once

# Installing the package

```
npx expo install expo-foreground-service
```

# Usage

1. Add any service type you need in the app.json plugin
```
"plugins": [
	[
		"expo-foreground-service",
		{
			"services": [
				{ "serviceType": "CAMERA" },
				{ "serviceType": "SPECIAL_USE", "description": "This is required by Google Play to explain what you need special use for." }
			]
		}
	]
]
```

2. Start and stop the service in your code.
```js
const startForegroundService = async () => {
  await ExpoForegroundService.startService(
    {
      notification: {
        title: 'Foreground Service',
        description: 'Running in the background',
        ongoing: true,
        chronometer: true,
        serviceType: ExpoForegroundService.ServiceType.LOCATION,
      },
    },
    async () => {
      // Execute task here
      console.log('You can now run a long running task in the background');
    }
  );
};

const stopForegroundService = async () => {
  await ExpoForegroundService.stopService();
};
```

## Supported Service Types
A full list of foreground service types and what they do can be found at https://developer.android.com/develop/background-work/services/fg-service-types

- CAMERA
- CONNECTED_DEVICE
- DATA_SYNC
- HEALTH
- LOCATION
- MEDIA_PLAYBACK
- MEDIA_PROJECTION
- MICROPHONE
- PHONE_CALL
- REMOTE_MESSAGING
- SPECIAL_USE
- SYSTEM_EXEMPTED