import { StyleSheet, Text, View } from 'react-native';

import * as ExpoForegroundService from 'expo-foreground-service';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ExpoForegroundService.hello()}</Text>
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
