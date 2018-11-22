package ada.texttospeech;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

final class AudioUtil {

    static void play(AudioInputStream audio) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(
                    e -> {
                        if (e.getType() == LineEvent.Type.STOP) {
                            latch.countDown();
                        }
                    });
            clip.open(audio);
            clip.start();
            latch.await();
        } catch (LineUnavailableException | IOException | InterruptedException | NullPointerException ex) {
            System.out.println("Error playing audio: " + ex);
        }
    }
}