package vue.fxPaquet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import vue.fxPaquet.controleur.DndImgControleur;
import vue.fxPaquet.controleur.LaticeFXControleur;

public class LaticeFX extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("SB/FormeLatice.fxml"));
		BorderPane root = loader.load();
		Scene scene = new Scene(root);
		LaticeFXControleur controller = loader.getController();
		
		GridPane gpPlateauDeJeu = controller.gridPane() ;
		
		DndImgControleur.DndPourGridPane(gpPlateauDeJeu);
		controller.afficherPlateau();
		//TODO Faire en sorte que l'arbitre puisse dire qu'une tuile est valide ou pas.
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Latice");
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	
}