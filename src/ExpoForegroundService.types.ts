export type ServiceType =
  | 'CAMERA'
  | 'CONNECTED_DEVICE'
  | 'DATA_SYNC'
  | 'HEALTH'
  | 'LOCATION'
  | 'MEDIA_PLAYBACK'
  | 'MEDIA_PROJECTION'
  | 'MICROPHONE'
  | 'PHONE_CALL'
  | 'REMOTE_MESSAGING'
  | 'SPECIAL_USE'
  | 'SYSTEM_EXEMPTED';

export type StartServiceConfig = {
  serviceType?: ServiceType;
  notification?: {
    channelId?: string;
    channelName?: string;
    id?: number;
    title?: string;
    description?: string;
    ongoing?: boolean;
    chronometer?: boolean;
  };
};

export type OnStartEventPayload = object;

export type OnErrorEventPayload = {
  error: string;
};

export enum Event {
  OnStart = 'onStart',
  OnError = 'onError',
}