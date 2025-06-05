package vue.fxPaquet.controleur;

import java.util.ArrayList;
import java.util.Arrays;
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

	@FXML private Label idLblTour, idLblNbTour,idAction;
	@FXML private Label idScore1, idScore2, idScore3, idScore4;
	@FXML private Label idLblChevalet1, idLblChevalet2,idLblChevalet3, idLblChevalet4;
	@FXML private Label idTuilesPosees1, idTuilesPosees2,idTuilesPosees3,idTuilesPosees4;

	@FXML private VBox idChevalet1, idChevalet2, idChevalet3, idChevalet4;

	@FXML private GridPane idGridPaneChevalet1, idGridPaneChevalet2, idGridPaneChevalet4, idGridPaneChevalet3, idPlateauJeu;

	@FXML private Button btnEchange, btnPasser, btnNouvAction, btnQuitter;
	
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
		TasDeTuile pioche = new TasDeTuile();
		int nombreJoueur = arbitre.nombreJoueurFX();
		
		List<VBox> chevalets = Arrays.asList(idChevalet1, idChevalet2, idChevalet3, idChevalet4);
		List<GridPane> listeGridPanes = Arrays.asList(idGridPaneChevalet1, idGridPaneChevalet2, idGridPaneChevalet3, idGridPaneChevalet4);
		List<Label> listeLabels = Arrays.asList(idLblChevalet1, idLblChevalet2, idLblChevalet3, idLblChevalet4);
		List<Label> listeScores = Arrays.asList(idScore1, idScore2, idScore3, idScore4);
		List<Label> listeTuilesPosees = Arrays.asList(idTuilesPosees1, idTuilesPosees2, idTuilesPosees3, idTuilesPosees4);

	    List<GridPane> gridPanes = new ArrayList<>();
	    List<Label> labels = new ArrayList<>();
	    List<Label> scores = new ArrayList<>();
	    List<Label> tuilesPosees = new ArrayList<>();


		for (int i = 0; i < chevalets.size(); i++) {
		    boolean joueurActif = i < nombreJoueur;
		
		    
		    chevalets.get(i).setVisible(joueurActif);
		    chevalets.get(i).setManaged(joueurActif); 
		
		    if (joueurActif) {
		        gridPanes.add(listeGridPanes.get(i));
		        labels.add(listeLabels.get(i));
		        scores.add(listeScores.get(i));
		        tuilesPosees.add(listeTuilesPosees.get(i));
		        scores.get(i).setText("Score : 0");
				tuilesPosees.get(i).setText("Tuiles posées : 0");
		    }
		}	  
		
		List<Joueur> joueurs = arbitre.creationListeJoueurFX(nombreJoueur, gridPanes, labels, scores, tuilesPosees) ;
		statistique = new StatistiqueJeu(plateau,joueurs);
		statistique.melangerListeJoueurs();
		statistique.resetActionsEffectuees();
		
		majLabelsTour();
		idAction.textProperty().bind(statistique.actionTexteProperty());
		
		pioche.creerTasDeTuile();
		arbitre.distribuerTuile(pioche, statistique.joueurs());
		arbitre.distribuerDansChevalet(statistique.joueurs());

		for (Joueur joueur : statistique.joueurs()) {
			afficherChevalet(joueur);
		}

		DndImgControleur.dndPourGridPane(gridPane(), statistique, btnPasser);
		activerSon();
	}

	private void majLabelsTour() {
		idLblTour.setText("Tour de " + joueurActuel().pseudo());
		idLblNbTour.setText("Tour " + statistique.nombreTour());
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
			btnPasser.setText("Passer le tour");
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

			if (statistique.nombreTour() <= statistique.nombreTourMaximum()) {
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
		return statistique.nombreTour() > statistique.nombreTourMaximum();
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
			couperSon();
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
		ImageView sonView = new ImageView(sonOnImage);
		MusicManager.play("/sons/Jeu.mp3",btnSon,sonView);
	}

	public void couperSon() {
		sonOffImage = new Image(getClass().getResource("/images/JeuMuet.png").toExternalForm());
		ImageView sonView = new ImageView(sonOffImage);
		MusicManager.stop(btnSon,sonView);
	}
}
