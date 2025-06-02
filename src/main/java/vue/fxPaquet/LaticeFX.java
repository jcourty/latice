package vue.fxPaquet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import vue.fxPaquet.controleur.LaticeFXControleur;

public class LaticeFX extends Application {

	@Override
	public void start(Stage stagePrincipale) throws Exception {
		
	    FXMLLoader chargeur = new FXMLLoader(getClass().getResource("SB/FormeLatice.fxml"));
		BorderPane racine = chargeur.load();
		Scene scene = new Scene(racine);
		LaticeFXControleur controleur = chargeur.getController();
		
		controleur.afficherPlateau();
		
		stagePrincipale.setScene(scene);
		stagePrincipale.setTitle("Latice");
		stagePrincipale.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	
}