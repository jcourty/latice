package vue.fxPaquet.controleur;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import metier.joueur.Joueur;
import metier.tuile.Tuile;

public class DndImgControleur {

    public static void manageSourceDragAndDrop(ImageView imageView, Joueur joueur, Tuile tuile) {
        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent contenu = new ClipboardContent();
                contenu.putImage(imageView.getImage());
                db.setContent(contenu);
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

    public static void DndPourGridPane(GridPane gridPane) {
    	// Dimensions du plateau/GridPane
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
            boolean succes = false;
            
            // Si on a bien sélectionné une tuile
            if (db.hasImage()) {
            	// Taille d'une cellule
                double largeurCase = gridPane.getWidth() / nbCol;
                double hauteurCase = gridPane.getHeight() / nbLigne;
                
                // Colonne et ligne de la case
                int col = (int) (event.getX() / largeurCase);
                int ligne = (int) (event.getY() / hauteurCase);
                
                // Si on est bien sur la GridPane
                if (col >= 0 && col < nbCol && ligne >= 0 && ligne < nbLigne) {
                    System.out.println("Drop sur GridPane : " + (col + 1) + ", " + (ligne + 1));
                    
                    // On parcours la GridPane pour chercher la case correspondante à l'endroit où on veut poser la tuile
                    ImageView imgViewCible = null;
                    for (Node noeud : gridPane.getChildren()) {
                        Integer colNoeud = GridPane.getColumnIndex(noeud);
                        Integer ligneNoeud = GridPane.getRowIndex(noeud);

                        int c = (colNoeud == null) ? 0 : colNoeud;
                        int l = (ligneNoeud == null) ? 0 : ligneNoeud;

                        if (c == col && l == ligne && noeud instanceof ImageView) {
                            imgViewCible = (ImageView) noeud;
                            break;
                        }
                    }
                    // Si la case est nulle (pas trop possible normalement mais sinon y'a des problèmes
                    if (imgViewCible == null) {
                        // Pas d'ImageView à cet emplacement donc on créer la tuile
                        ImageView nouvImageView = new ImageView(db.getImage());
                        nouvImageView.setFitWidth(largeurCase);
                        nouvImageView.setFitHeight(hauteurCase);
                        gridPane.add(nouvImageView, col, ligne);
                        succes = true;
                    } else {
                        // ImageView existe donc on regarde son image
                        Image img = imgViewCible.getImage();
                        if (img == null) {
                            // ImageView sans image donc on pose la tuile
                            imgViewCible.setImage(db.getImage());
                            imgViewCible.setFitWidth(largeurCase);
                            imgViewCible.setFitHeight(hauteurCase);
                            succes = true;
                        } else {
                        	// à changer car j'ai pas trouvé de remplacement 
                            @SuppressWarnings("deprecation")
							String url = img.impl_getUrl();
                            if (url != null && (
                                url.endsWith("bg_sun.png") || 
                                url.endsWith("bg_moon.png") || 
                                url.endsWith("bg_sea.png"))) {
                                // ImageView avec image de fond donc on remplace par la tuile
                                imgViewCible.setImage(db.getImage());
                                imgViewCible.setFitWidth(largeurCase);
                                imgViewCible.setFitHeight(hauteurCase);
                                succes = true;
                            } else {
                                // Case déjà occupée par une tuile donc on refuse le drop
                                System.out.println("Case déjà occupée par une tuile, drop refusé.");
                                succes = false;
                            }
                        }
                    }
                } else {
                	// Sinon si on drop dehors, ça ne marche pas
                    System.out.println("Drop hors GridPane !");
                }
            }

            event.setDropCompleted(succes);
            event.consume();
        });
    }
}
