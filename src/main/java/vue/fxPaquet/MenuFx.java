package vue.fxPaquet;

import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import vue.fxPaquet.metier.MusicManager;

public class MenuFx extends Application {

	public void start(Stage primaryStage) throws Exception {
		

		FXMLLoader loader = new FXMLLoader(getClass().getResource("SB/Menu.fxml"));
		BorderPane root = loader.load();
		Scene scene = new Scene(root);
		Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/menuFond.png")).toExternalForm());
		// Création du BackgroundImage
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1,1,true,true,true,false));
		
		// Application au conteneur
		root.setBackground(new Background(backgroundImage));
		
		Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
		ImageView logoView = new ImageView(logo);

		// Zoom visuel dans l'interface (pas l'icône de fenêtre)
		logoView.setPreserveRatio(true);
		logoView.setFitWidth(100);
		logoView.setScaleX(2.0);
		logoView.setScaleY(2.0);

		// Ajout correct de l'icône à la fenêtre
		primaryStage.getIcons().add(logo); // ici on met bien l'objet Image

		MusicManager.play("/sons/Kahoot.mp3");
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Menu");
		primaryStage.show();
		
		primaryStage.setResizable(false) ;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}


