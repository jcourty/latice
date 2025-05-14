package vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginAppSb extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane root = FXMLLoader.load((getClass().getResource("Formlatice.fxml")));
        Scene scene = new Scene(root, 600, 400);
        
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Application Login avec Scene Builder");
	    primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}