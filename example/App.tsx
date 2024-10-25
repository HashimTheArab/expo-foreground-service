import * as ExpoForegroundService from 'expo-foreground-service';
import { Button, StyleSheet, Text, View } from 'react-native';

export default function App() {

  const startForegroundService = async () => {
    await ExpoForegroundService.startService({
      serviceType: 'SPECIAL_USE', // Change this to what you require
      notification: {
        title: 'Foreground Service',
        description: 'Running in the background',
        ongoing: true,
        chronometer: true,
      },
    }, async () => {
      // Execute task here
      console.log('You can now run a long running task in the background');
    }, (error) => {
      console.log('Error starting service', error);
    });
  };

  const stopForegroundService = async () => {
    await ExpoForegroundService.stopService();
  }

  return (
    <View style={styles.container}>
      <Text>Foreground Service Example</Text>
      <Button
        title="Start Foreground Service"
        onPress={startForegroundService}
      />
      <Button title="Stop Foreground Service" onPress={stopForegroundService} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
