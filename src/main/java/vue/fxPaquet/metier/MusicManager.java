package vue.fxPaquet.metier;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {

	private static MediaPlayer player;

	public static void play(String fichier, Button btnSon, ImageView sonView) {
		stop(btnSon,sonView);
		Media media = new Media(MusicManager.class.getResource(fichier).toExternalForm());
		player = new MediaPlayer(media);
		player.setCycleCount(MediaPlayer.INDEFINITE);
		player.play();
		sonView.setPreserveRatio(false);
		sonView.setFitWidth(32);
		sonView.setFitHeight(32);
		btnSon.setGraphic(sonView);
	}

	public static void play(String fichier) {
		if (player != null) {
			player.stop();
		}
		Media media = new Media(MusicManager.class.getResource(fichier).toExternalForm());
		player = new MediaPlayer(media);
		player.setCycleCount(MediaPlayer.INDEFINITE);
		player.play();
	}
	
	public static void stop(Button btnSon, ImageView sonView) {
		if (player != null) {
			player.stop();
			sonView.setPreserveRatio(false);
			sonView.setFitWidth(32);
			sonView.setFitHeight(32);
			btnSon.setGraphic(sonView);
		}
	}
	
	
	
}
