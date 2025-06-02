package vue.fxPaquet.controleur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
	private Label idLblTour;
	
	@FXML
	private Label idLblNbTour;

	@FXML
	private VBox idChevalet1;

	@FXML
	private Label idScore1;
	
	@FXML
	private GridPane idGridPaneChevalet1;

	@FXML
	private Label idLblChevalet1;

	@FXML
	private VBox idChevalet2;
	
	@FXML
	private Label idScore2;

	@FXML
	private Label idLblChevalet2;

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

	private List<GridPane> gridPanes;
	private List<Label> labels;
	private List<Label> scores = new ArrayList<>();

	private PlateauDeJeu plateauDeJeu = new PlateauDeJeu();
	private static List<Joueur> champsJoueurs;
	private static int indexJoueurActuel = 0;
	
	 
	private static int nbTour = 1;
		
	public void scores() {
		scores.add(idScore1);
		scores.add(idScore2);
	}
	
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
		idLblNbTour.setText("Tour " + nbTour);

		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();
		scores();
		arbitre.distribuerTuile(pioche, champsJoueurs);
		arbitre.distribuerDansChevalet(champsJoueurs);

		for (int i = 0; i < champsJoueurs.size(); i++) {
			Joueur joueur = champsJoueurs.get(i);
			Label score = scores.get(i);
			LaticeFXControleur.afficherChevalet(joueur);
			score.setText("Score : " + joueur.score());
		}

		DndImgControleur.dndPourGridPane(gridPane());
	}

	public static void afficherChevalet(Joueur joueur) {
		List<Tuile> tuiles = joueur.listeChevalet();

		joueur.idGridPane().getChildren().clear();

		if (joueur.tailleChevalet() == 5) {
			for (int i = 0; i < tuiles.size(); i++) {
				Tuile tuile = tuiles.get(i);
				ImageView imageView = new ImageView(tuile.getImage());
				imageView.setFitWidth(79);
				imageView.setFitHeight(79);
				imageView.setPreserveRatio(true);

				joueur.ajouterDansGridPane(imageView, 0, i);

				if (joueur == joueurActuel()) {
					imageView.setOpacity(1.0);
					DndImgControleur.manageSourceDragAndDrop(imageView, joueur, tuile);
				} else {
					imageView.setOnDragDetected(null);
					imageView.setOnDragDone(null);
					imageView.setOpacity(0.5);
				}
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

	public static Joueur joueurActuel() {
		return champsJoueurs.get(indexJoueurActuel);
	}

	// Tous les eventHandler pour les boutons
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
		if(!partieFinie()) {
			if (indexJoueurActuel == champsJoueurs.size()-1) {
				nbTour += 1;
			}
			if (nbTour <= 10) {
				idLblNbTour.setText("Tour " + nbTour);
			
				indexJoueurActuel = (indexJoueurActuel + 1) % champsJoueurs.size();
		
				Joueur nouveauJoueurActuel = joueurActuel();
				idLblTour.setText("Tour de " + nouveauJoueurActuel.pseudo());
		
				for (int i = 0; i < champsJoueurs.size(); i++) {
					Joueur joueur = champsJoueurs.get(i);
					Label score = scores.get(i);
					LaticeFXControleur.afficherChevalet(joueur);
					score.setText("Score : " + joueur.score());
				}
				DndImgControleur.dndPourGridPane(gridPane());
	
				System.out.println(nbTour);
			} else {
				System.out.println("Partie finie");
				Alert dialog = new Alert(Alert.AlertType.INFORMATION);
				dialog.setTitle("Fin de partie");
				dialog.setHeaderText(null);
				dialog.setContentText("La partie est finie.");
				dialog.showAndWait();
			}
		}
	}

	@FXML
	void quitter(ActionEvent event) {
		Platform.exit();
	}
	
	@FXML
	boolean partieFinie() {
		return (nbTour > 10);
	}

}
