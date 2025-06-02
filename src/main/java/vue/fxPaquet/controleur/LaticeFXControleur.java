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

	@FXML private Label idLblTour;
	@FXML private Label idLblNbTour;
	@FXML private VBox idChevalet1;
	@FXML private Label idScore1;
	@FXML private GridPane idGridPaneChevalet1;
	@FXML private Label idLblChevalet1;
	@FXML private VBox idChevalet2;
	@FXML private Label idScore2;
	@FXML private Label idLblChevalet2;
	@FXML private GridPane idGridPaneChevalet2;
	@FXML private GridPane idPlateauJeu;
	@FXML private Button btnEchange;
	@FXML private Button btnPasser;
	@FXML private Button btnNouvAction;
	@FXML private Button btnQuitter;

	private PlateauDeJeu plateauDeJeu = new PlateauDeJeu();
	private static List<Joueur> champsJoueurs;
	private static int indexJoueurActuel = 0;
	private static int nbTour = 1; 
	private static int actionsEffectuees = 0;
    private static int actionsMaxParTour = 1;

	@FXML
	public GridPane gridPane() {
		return idPlateauJeu;
	}

	@FXML
	public void initialize() {
		Arbitre arbitre = new Arbitre();

	    List<GridPane> gridPanes = new ArrayList<>();
	    gridPanes.add(idGridPaneChevalet1);
	    gridPanes.add(idGridPaneChevalet2);

	    List<Label> labels = new ArrayList<>();
	    labels.add(idLblChevalet1);
	    labels.add(idLblChevalet2);

	    List<Label> scores = new ArrayList<>();
	    scores.add(idScore1);
	    scores.add(idScore2);
		champsJoueurs = arbitre.creationListeJoueurFX(2, gridPanes, labels, scores);
		Collections.shuffle(champsJoueurs);

		majLabelsTour();

		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();
		arbitre.distribuerTuile(pioche, champsJoueurs);
		arbitre.distribuerDansChevalet(champsJoueurs);

		for (Joueur joueur : champsJoueurs) {
			afficherChevalet(joueur);
		}

		DndImgControleur.dndPourGridPane(gridPane());
	}

	private void majLabelsTour() {
		idLblTour.setText("Tour de " + joueurActuel().pseudo());
		idLblNbTour.setText("Tour " + nbTour);
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
		resetActions();
		if (!partieFinie()) {
			if (indexJoueurActuel == champsJoueurs.size() - 1) {
				nbTour++;
			}

			if (nbTour <= 10) {
				indexJoueurActuel = (indexJoueurActuel + 1) % champsJoueurs.size();
				majLabelsTour();

				for (Joueur joueur : champsJoueurs) {
					afficherChevalet(joueur);
				}
				DndImgControleur.dndPourGridPane(gridPane());
			} else {
				finDePartie();
			}
		}
	}

	@FXML
	void quitter(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	boolean partieFinie() {
		return nbTour > 10;
	}

	private void finDePartie() {
		Alert dialog = new Alert(Alert.AlertType.INFORMATION);
		dialog.setTitle("Fin de partie");
		dialog.setHeaderText(null);
		dialog.setContentText("La partie est finie.");
		dialog.showAndWait();
	}
	
	public static boolean peutJouer() {
        return actionsEffectuees < actionsMaxParTour;
    }

    public static void actionEffectuee() {
        actionsEffectuees++;
    }

    public static void resetActions() {
    	actionsMaxParTour = 1 ;
        actionsEffectuees = 0;
    }
}
