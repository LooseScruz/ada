package ada;

import ada.texttospeech.AdaTextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;

import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class AdaClientMain {

  private static final int PORT = 6259;

  public static void main(String[] args) {
    AdaTextToSpeechClient cloudClient;
    try {
      cloudClient = new AdaTextToSpeechClient(TextToSpeechClient.create());
    } catch (IOException e) {
      cloudClient = null;
    }

    String host = args.length > 0 ? args[0] : "localhost";
    AdaClient client = new AdaClient(host, cloudClient, new NetworkSocketClient(host, PORT));
      client.run();
  }
}
