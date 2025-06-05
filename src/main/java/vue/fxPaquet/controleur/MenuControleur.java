package vue.fxPaquet.controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import vue.fxPaquet.LaticeFX;
import vue.fxPaquet.RegleJeu;
import vue.fxPaquet.metier.MusicManager;

public class MenuControleur {

	@FXML
	private Button btnJouer;

	@FXML
	private void lancerJeu(ActionEvent event) {
		try {
			couperSon();
			LaticeFX jeu = new LaticeFX();
			Stage stage = new Stage();
			jeu.start(stage);

			// Fermer le menu
			Stage menuStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			menuStage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private Button btnQuitter;

	@FXML
	void quitter(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	private Button btnRegle;

	@FXML
	private void voirRegle(ActionEvent event) {
		try {
			RegleJeu regle = new RegleJeu();
			Stage stage = new Stage();
			regle.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private Button btnSon;

	private boolean sonActive = true;
	private Image sonOnImage;
	private Image sonOffImage;

	@FXML
	private void initialize() {
		sonOnImage = new Image(getClass().getResource("/images/Son.png").toExternalForm());
		sonOffImage = new Image(getClass().getResource("/images/Muet.png").toExternalForm());

		ImageView imageView = new ImageView(sonOnImage);
		imageView.setFitWidth(32);
		imageView.setFitHeight(32);

		btnSon.setGraphic(imageView);
		btnSon.setText("");
		btnSon.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

		btnSon.setFocusTraversable(false);
	}

	@FXML
	private void modifierSon(ActionEvent event) {
		try {
			if (sonActive) {
				couperSon();

			} else {
				activerSon();
			}
			sonActive = !sonActive; 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void activerSon() {
		ImageView sonView = new ImageView(sonOnImage);
		MusicManager.play("/sons/Kahoot.mp3",btnSon,sonView);
	}

	public void couperSon() {
		ImageView sonView = new ImageView(sonOffImage);
		MusicManager.stop(btnSon,sonView);
	}
}
