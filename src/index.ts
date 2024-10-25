import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

import { StartServiceConfig, ServiceType, Event, OnStartEventPayload, OnErrorEventPayload } from './ExpoForegroundService.types';
import ExpoForegroundServiceModule from './ExpoForegroundServiceModule';

export async function startService(config: StartServiceConfig, callback: () => Promise<void>, onError?: (error: any) => void): Promise<void> {
  config = {
    ...config,
    notification: {
      id: 1,
      channelId: 'expo_foreground_service',
      channelName: 'Expo Foreground Service',
      ...config.notification,
    },
  };
  await ExpoForegroundServiceModule.startService(config);
  return new Promise<void>((resolve, reject) => {
    const subscription = addOnStartListener(async () => {
      try {
        await callback();
        subscription.remove();
        resolve();
      } catch (error) {
        subscription.remove();
        if (onError) {
          onError(error);
        }
        reject(error);
      }
    });
    
    const errorSubscription = addOnErrorListener((event) => {
      errorSubscription.remove();
      subscription.remove();
      if (onError) {
        onError(event.error);
      }
      reject(new Error(event.error));
    });
  });
}

export async function stopService(): Promise<void> {
  return await ExpoForegroundServiceModule.stopService();
}

const emitter = new EventEmitter(ExpoForegroundServiceModule ?? NativeModulesProxy.ExpoForegroundService);

export function addOnStartListener(listener: (event: OnStartEventPayload) => void): Subscription {
  return emitter.addListener<OnStartEventPayload>(Event.OnStart, listener);
}

export function addOnErrorListener(listener: (event: OnErrorEventPayload) => void): Subscription {
  return emitter.addListener<OnErrorEventPayload>(Event.OnError, listener);
}

export { ServiceType, StartServiceConfig };