package vue.fxPaquet;

import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import metier.arbitre.Arbitre;
import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import vue.fxPaquet.controleur.DndImgControleur;
import vue.fxPaquet.controleur.LaticeFXControleur;

public class LaticeFX extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("SB/FormeLatice.fxml"));
		BorderPane root = loader.load();
		Scene scene = new Scene(root);
		LaticeFXControleur controller = loader.getController();
		
		Arbitre arbitre = new Arbitre();
		List<GridPane> gridPanes = controller.listeGridPanes() ;
		List<Joueur> champsJoueurs = arbitre.creationListeJoueurFX(2,gridPanes);
		TasDeTuile pioche = new TasDeTuile();
		GridPane gpPlateauDeJeu = controller.gridPane() ;
		pioche.creerTasDeTuile();
		arbitre.distribuerTuile(pioche, champsJoueurs);
		arbitre.distribuerDansChevalet(champsJoueurs);
		
		for (Joueur joueur : champsJoueurs) {
			LaticeFXControleur.afficherChevalet(joueur);
		}
			
		DndImgControleur.DndPourGridPane(gpPlateauDeJeu);
		//TODO Faire en sorte que l'arbitre puisse dire qu'une tuile est valide ou pas.
		
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Latice");
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	
}