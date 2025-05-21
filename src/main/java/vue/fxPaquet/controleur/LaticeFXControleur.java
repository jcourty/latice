package vue.fxPaquet.controleur;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import metier.joueur.Joueur;
import metier.tuile.Tuile;

public class LaticeFXControleur {

    @FXML
    private VBox idChevalet2;

    @FXML
    private VBox idChevalet1;
    
    @FXML
    private GridPane idGridPaneChevalet1; // le GridPane défini dans le FXML

    @FXML
    private GridPane idGridPaneChevalet2;
    
    public List<GridPane> listeGridPanes() {
    	List<GridPane> gridPanes = new ArrayList<>();
    	gridPanes.add(idGridPaneChevalet1);
    	gridPanes.add(idGridPaneChevalet2);
    	
    	return gridPanes;
    }
    
    public void afficherChevalet(Joueur joueur) {
        List<Tuile> tuiles = joueur.listeChevalet();
        
        for (int i = 0; i < tuiles.size(); i++) {
            Tuile tuile = tuiles.get(i);
            ImageView imageView = new ImageView(tuile.getImage());
            imageView.setFitWidth(80);
            imageView.setFitHeight(60);
            imageView.setPreserveRatio(true);
            joueur.ajouterDansGridPane(imageView, 0, i);
        }
    }

    @FXML
    private GridPane idPlateauJeu;
    
 
}
