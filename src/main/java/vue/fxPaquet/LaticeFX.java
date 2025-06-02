package vue.fxPaquet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import vue.fxPaquet.controleur.LaticeFXControleur;

public class LaticeFX extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("SB/FormeLatice.fxml"));
		BorderPane root = loader.load();
		Scene scene = new Scene(root);
		LaticeFXControleur controller = loader.getController();
		
		controller.afficherPlateau();
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Latice");
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	
}