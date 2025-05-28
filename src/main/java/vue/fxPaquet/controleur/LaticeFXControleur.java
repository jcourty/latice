package vue.fxPaquet.controleur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	private Label idLblTour ;
	
	@FXML
	private VBox idChevalet1;

	@FXML
	private GridPane idGridPaneChevalet1;

	@FXML
	private Label idLblChevalet1;
	
	@FXML
	private VBox idChevalet2;
	
	@FXML
    private Label idLblChevalet2 ;
	
	@FXML
	private GridPane idGridPaneChevalet2;

	@FXML
	private GridPane idPlateauJeu;

	@FXML
	private Button btnEchange;

	@FXML
	private Button btnPasser;

	@FXML
	private Button btnNouvAction;

	@FXML
	private Button btnQuitter;

	@FXML
	public GridPane gridPane() {
		return idPlateauJeu;
	}
	
	List<GridPane> gridPanes ;
	List<Label> labels ;
	
	PlateauDeJeu plateauDeJeu = new PlateauDeJeu();
	private List<Joueur> champsJoueurs;
	private int indexJoueurActuel = 0;

	public List<GridPane> listeGridPanes() {
		gridPanes = new ArrayList<>();
		gridPanes.add(idGridPaneChevalet1);
		gridPanes.add(idGridPaneChevalet2);
		return gridPanes;
	}

	public List<Label> listeNomChevalet() {
		labels = new ArrayList<>();
		labels.add(idLblChevalet1);
		labels.add(idLblChevalet2);
		return labels;
	}

	@FXML
	public void initialize() {
		Arbitre arbitre = new Arbitre();

		List<GridPane> chevalet = listeGridPanes();
		List<Label> nomChevalet = listeNomChevalet();
		champsJoueurs = arbitre.creationListeJoueurFX(2, chevalet, nomChevalet);
		Collections.shuffle(champsJoueurs);
		idLblTour.setText("Tour de " + champsJoueurs.get(indexJoueurActuel).pseudo());
		
		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();

		arbitre.distribuerTuile(pioche, champsJoueurs);
		arbitre.distribuerDansChevalet(champsJoueurs);

		for (Joueur joueur : champsJoueurs) {
			LaticeFXControleur.afficherChevalet(joueur);
		}
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

	public Joueur joueurActuel() {
		return champsJoueurs.get(indexJoueurActuel);
	}

	// Tous les eventHandler pour les bouttons
	@FXML
	void echanger(ActionEvent event) {
		Joueur joueur = joueurActuel();
		joueur.viderChevalet();
		joueur.remplirChevalet();
		afficherChevalet(joueur);
	}

	@FXML
	void nouvelleAction(ActionEvent event) {
		// TODO
	}

	@FXML
	void passer(ActionEvent event) {
		indexJoueurActuel = (indexJoueurActuel + 1) % champsJoueurs.size();
		idLblTour.setText("Tour de " + champsJoueurs.get(indexJoueurActuel).pseudo());
	}

	@FXML
	void quitter(ActionEvent event) {
		Platform.exit();
	}

}
