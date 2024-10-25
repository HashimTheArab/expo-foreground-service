import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ExpoForegroundServiceViewProps } from './ExpoForegroundService.types';

const NativeView: React.ComponentType<ExpoForegroundServiceViewProps> =
  requireNativeViewManager('ExpoForegroundService');

export default function ExpoForegroundServiceView(props: ExpoForegroundServiceViewProps) {
  return <NativeView {...props} />;
}
