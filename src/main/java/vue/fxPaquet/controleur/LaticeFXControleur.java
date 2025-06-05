package vue.fxPaquet.controleur;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import metier.statistique.StatistiqueJeu;
import metier.tuile.Tuile;
import vue.fxPaquet.MenuFx;
import vue.fxPaquet.metier.MusicManager;

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

	private StatistiqueJeu statistique;

	@FXML
	public GridPane gridPane() {
		return idPlateauJeu;
	}

	@FXML
	private Button btnSon;

	private boolean sonActive = true;
	private Image sonOnImage;
	private Image sonOffImage;

	@FXML
	public void initialize() {
		Arbitre arbitre = new Arbitre();
		PlateauDeJeu plateau = new PlateauDeJeu();

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

		statistique = new StatistiqueJeu(plateau,
				arbitre.creationListeJoueurFX(2, gridPanes, labels, scores, tuileposees));
		statistique.melangerListeJoueurs();
		statistique.resetActionsEffectuees();

		majLabelsTour();
		idScore1.setText("Score : 0");
		idScore2.setText("Score : 0");
		idTuilesPosees1.setText("Tuiles posées : 0");
		idTuilesPosees2.setText("Tuiles posées : 0");
		idAction.textProperty().bind(statistique.actionTexteProperty());

		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();
		arbitre.distribuerTuile(pioche, statistique.joueurs());
		arbitre.distribuerDansChevalet(statistique.joueurs());

		for (Joueur joueur : statistique.joueurs()) {
			afficherChevalet(joueur);
		}

		System.out.println(statistique.actionsEffectuees());
		DndImgControleur.dndPourGridPane(gridPane(), statistique, btnPasser);
		activerSon();
	}

	private void majLabelsTour() {
		idLblTour.setText("Tour de " + joueurActuel().pseudo());
		idLblNbTour.setText("Tour " + statistique.nbTour());
	}

	public void afficherChevalet(Joueur joueur) {
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

				if (joueur == statistique.joueurActuel()) {
					imageView.setOpacity(1.0);
					DndImgControleur.manageSourceDragAndDrop(imageView, joueur, tuile, this);
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
		PlateauDeJeu plateau = statistique.plateau();
		for (Map.Entry<Case, Tuile> entry : plateau.plateauEntrySet()) {
			Case uneCase = entry.getKey();
			int col = uneCase.coordonneeX();
			int ligne = uneCase.coordonneeY();

			if (plateau.tuileSur(uneCase) == null) {
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

	public Joueur joueurActuel() {
		return statistique.joueurActuel();
	}

	@FXML
	void echanger(ActionEvent event) {
		if (statistique.peutJouer()) {
			statistique.augmentationActionsEffectuees();
			statistique.majLabelActionAutomatique();
			Joueur joueur = joueurActuel();
			joueur.viderChevalet();
			joueur.remplirChevalet();
			afficherChevalet(joueur);
		}
	}

	@FXML
	void nouvelleAction(ActionEvent event) {
		Joueur joueur = joueurActuel();
		if (joueur.score() >= 2) {
			joueur.ajouterScore(-2);
			statistique.augmentationMaxParTour();
			statistique.majLabelActionAutomatique();
			joueur.lblScore().setText("Score : " + joueur.score());
		}
	}

	@FXML
	void passer(ActionEvent event) {
		statistique.reenitialiserActions();
		statistique.majLabelActionAutomatique();
		if (!partieFinie()) {
			if (statistique.indexJoueurActuel() == statistique.joueurs().size() - 1) {
				statistique.augmentationNombreTour();
			}

			if (statistique.nbTour() <= 10) {
				statistique.joueurSuivant();
				btnPasser.setText("Passer tour");
				majLabelsTour();

				for (Joueur joueur : statistique.joueurs()) {
					afficherChevalet(joueur);
				}
				DndImgControleur.dndPourGridPane(gridPane(), statistique, btnPasser);
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
		return statistique.nbTour() > 10;
	}

	private void finDePartie() {
		Alert dialogue = new Alert(Alert.AlertType.INFORMATION);
		List<Joueur> joueurGagne = Arbitre.joueurGagnant(statistique.joueurs());
		dialogue.setTitle("Fin de partie");
		dialogue.setHeaderText(null);
		dialogue.setContentText(Arbitre.gestionVictoire(joueurGagne));
		dialogue.showAndWait();

		retourMenu(btnQuitter);
	}

	public void retourMenu(Node event) {
		try {
			MusicManager.stop();
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

	@FXML
	private void modifierSon(ActionEvent event) {
		try {
			if (sonActive) {
				couperSon();
			} else {
				activerSon();
			}
			sonActive = !sonActive;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void activerSon() {
		btnSon.setText("");
		btnSon.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
		btnSon.setFocusTraversable(false);
		sonOnImage = new Image(getClass().getResource("/images/JeuSon.png").toExternalForm());
		MusicManager.play("/sons/Jeu.mp3");
		ImageView soundView = new ImageView(sonOnImage);
		soundView.setFitWidth(32);
		soundView.setFitHeight(32);
		btnSon.setGraphic(soundView);
	}

	public void couperSon() {
		sonOffImage = new Image(getClass().getResource("/images/JeuMuet.png").toExternalForm());
		MusicManager.stop();
		ImageView muteView = new ImageView(sonOffImage);
		muteView.setFitWidth(32);
		muteView.setFitHeight(32);
		btnSon.setGraphic(muteView);
	}
}
