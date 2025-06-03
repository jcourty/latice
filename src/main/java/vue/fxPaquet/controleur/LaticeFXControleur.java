package vue.fxPaquet.controleur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metier.arbitre.Arbitre;
import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.plateau.Case;
import metier.plateau.PlateauDeJeu;
import metier.tuile.Tuile;
import vue.Console;
import vue.fxPaquet.MenuFx;

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
	private Label idAction;
	@FXML
	private Button btnEchange;
	@FXML
	private Button btnPasser;
	@FXML
	private Button btnNouvAction;
	@FXML
	private Button btnQuitter;
	 @FXML
	 private Label idTuilesPosees1;
	 @FXML
	 private Label idTuilesPosees2;

	private PlateauDeJeu plateauDeJeu;
	private static List<Joueur> champsJoueurs;
	private static int indexJoueurActuel = 0;;
	private static int nbTour;
	private static int actionsEffectuees;
	private static int actionsMaxParTour;
	private static StringProperty actionTexteProperty;

	@FXML
	public GridPane gridPane() {
		return idPlateauJeu;
	}

	@FXML
	public void initialize() {
		plateauDeJeu = new PlateauDeJeu();
		nbTour = 1;
		actionsEffectuees = 0;
		actionsMaxParTour = 1;
		actionTexteProperty = new SimpleStringProperty("Nombre d'actions : 0/1");
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
		
		List<Label> tuileposees = new ArrayList<>();
		tuileposees.add(idTuilesPosees1);
		tuileposees.add(idTuilesPosees2);
		
		champsJoueurs = arbitre.creationListeJoueurFX(2, gridPanes, labels, scores,tuileposees);
		Collections.shuffle(champsJoueurs);

		majLabelsTour();
		idScore1.setText("Score : 0");
		idScore2.setText("Score : 0");
		idTuilesPosees1.setText("Tuiles posées : 0");
		idTuilesPosees2.setText("Tuiles posées : 0");
		idAction.textProperty().bind(actionTexteProperty());

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
			String cheminImage = urls.get(i);
			int[] pos = positions.get(i);
			Image image = new Image(getClass().getResource(cheminImage).toExternalForm());

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
			int ligne = uneCase.coordonneeY();

			if (plateauDeJeu.tuileSur(uneCase) == null) {
				Image image = uneCase.getImage();
				ImageView imageView = new ImageView(image);
				imageView.setUserData("fond");
				imageView.setFitWidth(80);
				imageView.setFitHeight(80);
				imageView.setPreserveRatio(true);

				idPlateauJeu.add(imageView, col - 1, ligne - 1);
			}
		}
	}

	public static Joueur joueurActuel() {
		return champsJoueurs.get(indexJoueurActuel);
	}

	@FXML
	void echanger(ActionEvent event) {
		if (peutJouer()) {
			actionEffectuee();
			majLabelActionAutomatique();
			Joueur joueur = joueurActuel();
			joueur.viderChevalet();
			joueur.remplirChevalet();
			afficherChevalet(joueur);
		}
	}

	@FXML
	void nouvelleAction(ActionEvent event) {
		Joueur joueur = joueurActuel();
        if(joueur.score() >= 2) {
            joueur.ajouterScore(-2);
            actionsMaxParTour++;
            majLabelActionAutomatique();
        }
	}

	@FXML
	void passer(ActionEvent event) {
		resetActions();
		majLabelActionAutomatique();
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
		 retourMenu((Node) event.getSource());
	}

	@FXML
	boolean partieFinie() {
		return nbTour > 10;
	}

	private void finDePartie() {
		Alert dialogue = new Alert(Alert.AlertType.INFORMATION);
		Joueur joueurGagne = Arbitre.joueurGagnant(champsJoueurs);
		Console.message("Le joueur gagnant est : " + joueurGagne.pseudo());
		dialogue.setTitle("Fin de partie");
		dialogue.setHeaderText(null);
		dialogue.setContentText("La partie est finie, le joueur gagnant est : " + joueurGagne.pseudo()+".");
		dialogue.showAndWait();
		
		retourMenu(btnQuitter);
	}

	public static boolean peutJouer() {
		return actionsEffectuees < actionsMaxParTour;
	}

	public static void actionEffectuee() {
		actionsEffectuees++;
	}

	public static void resetActions() {
		actionsMaxParTour = 1;
		actionsEffectuees = 0;
	}

	public static String majLblAction() {
		return "Nombre d'actions : " + actionsEffectuees + "/" + actionsMaxParTour;
	}

	public static StringProperty actionTexteProperty() {
		return actionTexteProperty;
	}

	public static void majLabelActionAutomatique() {
		actionTexteProperty.set("Nombre d'actions : " + actionsEffectuees + "/" + actionsMaxParTour);
	}
	public void retourMenu(Node event) {
		 try {
	            MenuFx menu = new MenuFx();
	            Stage stage = new Stage();
	            menu.start(stage);
	            
	            // Fermer le menu
	            Stage menuStage = (Stage) ((Node) event).getScene().getWindow();
	            menuStage.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
}
