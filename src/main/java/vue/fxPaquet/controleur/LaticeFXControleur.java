package vue.fxPaquet.controleur;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import metier.arbitre.Arbitre;
import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.tuile.Tuile;
import vue.fxPaquet.LaticeFX;

public class LaticeFXControleur {

	@FXML
	private VBox idChevalet2;

	@FXML
	private VBox idChevalet1;

	@FXML
	private GridPane idGridPaneChevalet1;

	@FXML
	private GridPane idGridPaneChevalet2;

	public List<GridPane> listeGridPanes() {
		List<GridPane> gridPanes = new ArrayList<>();
		gridPanes.add(idGridPaneChevalet1);
		gridPanes.add(idGridPaneChevalet2);

		return gridPanes;
	}

	public static void afficherChevalet(Joueur joueur) {
		List<Tuile> tuiles = joueur.listeChevalet();

	    joueur.idGridPane().getChildren().clear();
		if (joueur.tailleChevalet() >= 5) {
			for (int i = 0; i < tuiles.size(); i++) {
				Tuile tuile = tuiles.get(i);
				ImageView imageView = new ImageView(tuile.getImage());
				imageView.setFitWidth(79);
				imageView.setFitHeight(79);
				imageView.setPreserveRatio(true);
				joueur.ajouterDansGridPane(imageView, 0, i);

				DndImgControleur.manageSourceDragAndDrop(imageView, joueur, tuile);
			}
		}
	}

	@FXML
	private GridPane idPlateauJeu;

	@FXML
	public GridPane gridPane() {
		return idPlateauJeu;
	}
	
	@FXML
	public void initialize() {
		
		Arbitre arbitre = new Arbitre();
		
		List<GridPane> chevalet = listeGridPanes();
		List<Joueur> champsJoueurs = arbitre.creationListeJoueurFX(2,chevalet);
		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();
		arbitre.distribuerTuile(pioche, champsJoueurs);
		arbitre.distribuerDansChevalet(champsJoueurs);
		
		for (Joueur joueur : champsJoueurs) {
			LaticeFXControleur.afficherChevalet(joueur);
		}
		
	}
}
