package vue.fxPaquet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import vue.fxPaquet.controleur.LaticeFXControleur;
import vue.fxPaquet.metier.MusicManager;

public class LaticeFX extends Application {

	@Override
	public void start(Stage stagePrincipale) throws Exception {
		
	    FXMLLoader chargeur = new FXMLLoader(getClass().getResource("SB/FormeLatice.fxml"));
		BorderPane racine = chargeur.load();
		Scene scene = new Scene(racine);
		LaticeFXControleur controleur = chargeur.getController();
		
		
		controleur.afficherPlateau();
		
		Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
		ImageView logoView = new ImageView(logo);

		// Zoom visuel dans l'interface (pas l'icône de fenêtre)
		logoView.setPreserveRatio(true);
		logoView.setFitWidth(100);
		logoView.setScaleX(2.0);
		logoView.setScaleY(2.0);

		// Ajout correct de l'icône à la fenêtre
		stagePrincipale.getIcons().add(logo);
		
		MusicManager.play("/sons/Jeu.mp3");
		
		stagePrincipale.setScene(scene);
		stagePrincipale.setTitle("Latice");
		stagePrincipale.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	
}