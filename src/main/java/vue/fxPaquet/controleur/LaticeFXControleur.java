package vue.fxPaquet.controleur;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import metier.arbitre.Arbitre;
import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.plateau.Case;
import metier.plateau.PlateauDeJeu;
import metier.tuile.Tuile;

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
	
	public void afficherImagesDansGridPane(List<String> urls, List<int[]> positions, GridPane gridPane) {
	   for (int i = 0; i < urls.size(); i++) {
	        String imagePath = urls.get(i);
	        int[] pos = positions.get(i); 
	        Image image = new Image(getClass().getResource(imagePath).toExternalForm());

	        ImageView imageView = new ImageView(image);
	        imageView.setFitWidth(79);
	        imageView.setFitHeight(79);
	        imageView.setPreserveRatio(true);

	        gridPane.add(imageView, pos[0], pos[1]);
	    }
	}
	
	PlateauDeJeu plateauDeJeu = new PlateauDeJeu();
	
	public void afficherPlateau() {
		for (Map.Entry<Case, Tuile> entry : plateauDeJeu.plateau().entrySet()) {
			Case uneCase = entry.getKey();
			int col = uneCase.coordonneeX();
			int row = uneCase.coordonneeY();

			if (plateauDeJeu.tuileSur(uneCase) == null) {
				Image image = uneCase.getImage();
				ImageView imageView = new ImageView(image);
				imageView.setUserData("fond");
				imageView.setFitWidth(80);
				imageView.setFitHeight(80);
				imageView.setPreserveRatio(true);

				idPlateauJeu.add(imageView, col - 1, row - 1);
			}
		}
	}
}
