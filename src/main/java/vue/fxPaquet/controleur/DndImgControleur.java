package vue.fxPaquet.controleur;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import metier.arbitre.Arbitre;
import metier.joueur.Joueur;
import metier.plateau.Case;
import metier.plateau.Coordonnee;
import metier.plateau.PlateauDeJeu;
import metier.tuile.Tuile;

public class DndImgControleur {
	
	private static PlateauDeJeu plateau = new PlateauDeJeu();

    public static void manageSourceDragAndDrop(ImageView imageView, Joueur joueur, Tuile tuile) {
    	
		imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
				ClipboardContent contenu = new ClipboardContent();
				imageView.setUserData(tuile);
				
				contenu.putImage(imageView.getImage());
				db.setContent(contenu);
				event.consume();
			}
		});

		imageView.setOnDragDone(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				if (event.getTransferMode() == TransferMode.MOVE) {
					joueur.piocherDansChevalet(joueur.listeChevalet().indexOf(tuile));
					joueur.remplirChevalet();
					if (joueur.tailleChevalet() < 5) {
						((GridPane) imageView.getParent()).getChildren().remove(imageView);
					} else {
						LaticeFXControleur.afficherChevalet(joueur);
					}
				}
				event.consume();
			}
		});
	}

    public static void dndPourGridPane(GridPane gridPane, Label lblAction) {
        int nbCol = 9;
        int nbLigne = 9;
        Joueur joueur = LaticeFXControleur.joueurActuel();

        gridPane.setOnDragOver(event -> {
            if (event.getGestureSource() != gridPane && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        gridPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            Integer colNoeud = null;
            Integer ligneNoeud = null;
            ImageView source = (ImageView) event.getGestureSource();
            Tuile tuile = (Tuile) source.getUserData();
            boolean succes = false;

            if (db.hasImage()) {
                double largeurCase = gridPane.getWidth() / nbCol;
                double hauteurCase = gridPane.getHeight() / nbLigne;

                int col = (int) (event.getX() / largeurCase);
                int ligne = (int) (event.getY() / hauteurCase);

                if (col >= 0 && col < nbCol && ligne >= 0 && ligne < nbLigne) {

                    ImageView imgViewCible = null;
                    for (Node noeud : gridPane.getChildren()) {
                        colNoeud = GridPane.getColumnIndex(noeud);
                        ligneNoeud = GridPane.getRowIndex(noeud);

                        int c = (colNoeud == null) ? 0 : colNoeud;
                        int l = (ligneNoeud == null) ? 0 : ligneNoeud;

                        if (c == col && l == ligne && noeud instanceof ImageView) {
                            imgViewCible = (ImageView) noeud;
                            break;
                        }
                    }

                    if (imgViewCible == null) {
                        ImageView nouvImageView = new ImageView(db.getImage());
                        nouvImageView.setFitWidth(largeurCase);
                        nouvImageView.setFitHeight(hauteurCase);
                        nouvImageView.setUserData("tuile"); // on pose une vraie tuile
                        gridPane.add(nouvImageView, col, ligne);
                        succes = true;
                    } else {
                        Object tag = imgViewCible.getUserData();
                        if ((tag == null || "fond".equals(tag)) && coupValide(plateau, tuile, colNoeud+1, ligneNoeud+1, joueur, lblAction)) {
                            imgViewCible.setImage(db.getImage());
                            imgViewCible.setFitWidth(largeurCase);
                            imgViewCible.setFitHeight(hauteurCase);
                            imgViewCible.setUserData("tuile"); // devient une vraie tuile
                            succes = true;
                        }
                    }
                }
            }

            event.setDropCompleted(succes);
            event.consume();
        });
    }
    
    public static boolean coupValide(PlateauDeJeu plateau, Tuile tuile, int col, int ligne, Joueur joueur, Label lblAction) {
        if (!LaticeFXControleur.peutJouer()) {
            System.out.println("Action impossible");
            return false; 
        }
        Case uneCase = plateau.caseSur(new Coordonnee(col, ligne));
        if (Arbitre.peutPoserTuile(plateau, uneCase, tuile, joueur)) {
            plateau.poserTuile(uneCase, tuile, joueur);
            joueur.lblScore().setText("Score : " + joueur.score());

            Arbitre.calculeScore(joueur, uneCase);
            LaticeFXControleur.actionEffectuee();
            lblAction.setText(LaticeFXControleur.majLblAction());

            return true;
        }
        return false;
    }

}
