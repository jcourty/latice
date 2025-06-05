package vue.fxPaquet.controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import vue.fxPaquet.LaticeFX;
import vue.fxPaquet.RegleJeu;
import vue.fxPaquet.metier.MusicManager;

public class MenuControleur {

    @FXML
    private Button btnJouer;
    @FXML
    private void lancerJeu(ActionEvent event) {
        try {
        	MusicManager.stop();
            LaticeFX jeu = new LaticeFX();
            Stage stage = new Stage();
            jeu.start(stage);

            // Fermer le menu
            Stage menuStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            menuStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button btnQuitter;
    @FXML
	void quitter(ActionEvent event) {
		Platform.exit();
	}

    @FXML
    private Button btnRegle;
    @FXML
    private void voirRegle(ActionEvent event) {
        try {
            RegleJeu regle = new RegleJeu();
            Stage stage = new Stage();
            regle.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private Button btnSon;
    
    private boolean sonActive = true;
    private Image sonOnImage;
    private Image sonOffImage;
    
    @FXML
    private void initialize() {
    	// Charger les images au lancement
        sonOnImage = new Image(getClass().getResource("/images/Son.png").toExternalForm());
        sonOffImage = new Image(getClass().getResource("/images/Muet.png").toExternalForm());

        // Créer un ImageView avec l'image
        ImageView imageView = new ImageView(sonOnImage);
        imageView.setFitWidth(32);  // ajuste selon ton besoin
        imageView.setFitHeight(32);

        // Affecter l'image au bouton
        btnSon.setGraphic(imageView);

        // Supprimer le texte si besoin
        btnSon.setText("");
        
        // Rendre le fond et la bordure du bouton transparents
        btnSon.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // Enlever le focus visuel quand on clique
        btnSon.setFocusTraversable(false);
    }
    @FXML
    private void modifierSon(ActionEvent event) {
        try {
            if (sonActive) {
                MusicManager.stop();
                ImageView muteView = new ImageView(sonOffImage);
                muteView.setFitWidth(32);
                muteView.setFitHeight(32);
                btnSon.setGraphic(muteView);
            } else {
                MusicManager.play("/sons/Kahoot.mp3");
                ImageView soundView = new ImageView(sonOnImage);
                soundView.setFitWidth(32);
                soundView.setFitHeight(32);
                btnSon.setGraphic(soundView);
            }
            sonActive = !sonActive; // Inverser l'état
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
