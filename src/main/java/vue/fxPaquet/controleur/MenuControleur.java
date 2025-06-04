package vue.fxPaquet.controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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


}
