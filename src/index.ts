import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ExpoForegroundService.web.ts
// and on native platforms to ExpoForegroundService.ts
import ExpoForegroundServiceModule from './ExpoForegroundServiceModule';
import ExpoForegroundServiceView from './ExpoForegroundServiceView';
import { ChangeEventPayload, ExpoForegroundServiceViewProps } from './ExpoForegroundService.types';

// Get the native constant value.
export const PI = ExpoForegroundServiceModule.PI;

export function hello(): string {
  return ExpoForegroundServiceModule.hello();
}

export async function setValueAsync(value: string) {
  return await ExpoForegroundServiceModule.setValueAsync(value);
}

const emitter = new EventEmitter(ExpoForegroundServiceModule ?? NativeModulesProxy.ExpoForegroundService);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ExpoForegroundServiceView, ExpoForegroundServiceViewProps, ChangeEventPayload };
