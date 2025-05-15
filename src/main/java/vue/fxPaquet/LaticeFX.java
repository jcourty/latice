package vue.fxPaquet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LaticeFX extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = FXMLLoader.load((getClass().getResource("SB/FormeLatice.fxml")));
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Latice");
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}