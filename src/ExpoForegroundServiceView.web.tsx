import * as React from 'react';

import { ExpoForegroundServiceViewProps } from './ExpoForegroundService.types';

export default function ExpoForegroundServiceView(props: ExpoForegroundServiceViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
