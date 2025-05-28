package vue.fxPaquet.controleur;

import javafx.event.EventHandler;
import javafx.scene.Node;
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
                System.out.println(tuile.afficher());
                event.consume();
            }
        });

        imageView.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    System.out.println("Drop complété !");
                    joueur.piocherDansChevalet(joueur.listeChevalet().indexOf(tuile));
                    joueur.remplirChevalet();
                    if (joueur.tailleChevalet() < 5) {
                        ((GridPane) imageView.getParent()).getChildren().remove(imageView);
                    } else {
                        LaticeFXControleur.afficherChevalet(joueur);
                    }
                } else {
                    System.out.println("Drop échoué !");
                }
                event.consume();
            }
        });
    }

    public static void dndPourGridPane(GridPane gridPane) {
        int nbCol = 9;
        int nbLigne = 9;

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
                    System.out.println("Drop sur GridPane : " + (col + 1) + ", " + (ligne + 1));

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
                        if ((tag == null || "fond".equals(tag)) && coupValide(plateau, tuile, colNoeud+1, ligneNoeud+1)) {
                            imgViewCible.setImage(db.getImage());
                            imgViewCible.setFitWidth(largeurCase);
                            imgViewCible.setFitHeight(hauteurCase);
                            imgViewCible.setUserData("tuile"); // devient une vraie tuile
                            succes = true;
                        } else {
                            System.out.println("Case déjà occupée par une tuile, drop refusé.");
                        }
                    }
                } else {
                    System.out.println("Drop hors GridPane !");
                }
            }

            event.setDropCompleted(succes);
            event.consume();
        });
    }
    
    public static boolean coupValide(PlateauDeJeu plateau, Tuile tuile, int col, int ligne) {
    	Case uneCase = new Case(new Coordonnee(col, ligne));
    	if (Arbitre.peutPoserTuile(plateau, uneCase, tuile)) {
    		plateau.poserTuile(uneCase, tuile);
    		return true;
    	}
    	return false;
    }
}
