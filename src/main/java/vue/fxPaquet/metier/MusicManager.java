package vue.fxPaquet.metier;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {

	private static MediaPlayer player;

    public static void play(String file) {
        stop();
        Media media = new Media(MusicManager.class.getResource(file).toExternalForm());
        player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
    }

    public static void stop() {
        if (player != null) player.stop();
    }
}
