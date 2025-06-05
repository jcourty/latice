package vue.fxPaquet.controleur;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import metier.statistique.StatistiqueJeu;
import metier.tuile.Tuile;

public class DndImgControleur {

	private static PlateauDeJeu plateau;
    private static final int NB_COL = 9;
    private static final int NB_LIGNE = 9;

	public static void manageSourceDragAndDrop(ImageView imageView, Joueur joueur, Tuile tuile,
			LaticeFXControleur controleur) {

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
						controleur.afficherChevalet(joueur);
					}
				}
				event.consume();
			}
		});
	}

	public static void dndPourGridPane(GridPane gridPane, StatistiqueJeu statistique, Button btnPasser) {
        plateau = statistique.plateau();

        gridPane.setOnDragOver(event -> {
            if (event.getGestureSource() != gridPane && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        gridPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean succes = false;

            if (db.hasImage()) {
                ImageView source = (ImageView) event.getGestureSource();
                Tuile tuileADeplacer = (Tuile) source.getUserData();

                int colCible = (int) (event.getX() / (gridPane.getWidth() / NB_COL));
                int ligneCible = (int) (event.getY() / (gridPane.getHeight() / NB_LIGNE));

                if (estCoordonneeValide(colCible, ligneCible)) {
                    succes = traiterDepotTuile(gridPane, db, tuileADeplacer, colCible, ligneCible, statistique);
                }
            }
            event.setDropCompleted(succes);
            mettreAJourBoutonPasser(btnPasser, statistique);
            event.consume();
        });
    }

    private static boolean estCoordonneeValide(int col, int ligne) {
        return col >= 0 && col < NB_COL && ligne >= 0 && ligne < NB_LIGNE;
    }

    private static boolean traiterDepotTuile(GridPane gridPane, Dragboard db, Tuile tuileADeplacer, int colCible, int ligneCible, StatistiqueJeu statistique) {
        ImageView imgViewCible = trouverImageViewCible(gridPane, colCible, ligneCible);
        
        if (imgViewCible == null) {
            return ajouterNouvelleTuileAuGrid(gridPane, db, tuileADeplacer, colCible, ligneCible, statistique);
        } else {
            return remplacerTuileExistanteDansGrid(imgViewCible, db, tuileADeplacer, colCible, ligneCible, statistique);
        }
    }

    private static ImageView trouverImageViewCible(GridPane gridPane, int col, int ligne) {
        for (Node noeud : gridPane.getChildren()) {
            Integer colNoeud = GridPane.getColumnIndex(noeud);
            Integer ligneNoeud = GridPane.getRowIndex(noeud);
            int c = (colNoeud == null) ? 0 : colNoeud;
            int l = (ligneNoeud == null) ? 0 : ligneNoeud;
            if (c == col && l == ligne && noeud instanceof ImageView) {
                return (ImageView) noeud;
            }
        }
        return null;
    }

    private static boolean ajouterNouvelleTuileAuGrid(GridPane gridPane, Dragboard db, Tuile tuileADeplacer, int col, int ligne, StatistiqueJeu statistique) {
        if (coupValide(plateau, tuileADeplacer, col + 1, ligne + 1, statistique)) {
            ImageView nouvImageView = new ImageView(db.getImage());
            nouvImageView.setFitWidth(gridPane.getWidth() / NB_COL);
            nouvImageView.setFitHeight(gridPane.getHeight() / NB_LIGNE);
            nouvImageView.setUserData("tuile");
            gridPane.add(nouvImageView, col, ligne);
            return true;
        }
        return false;
    }

    private static boolean remplacerTuileExistanteDansGrid(ImageView imgViewCible, Dragboard db, Tuile tuileADeplacer,
                                                          int col, int ligne, StatistiqueJeu statistique) {
        Object tag = imgViewCible.getUserData();
        if ((tag == null || "fond".equals(tag)) && coupValide(plateau, tuileADeplacer, col + 1, ligne + 1, statistique)) {
            imgViewCible.setImage(db.getImage());
            imgViewCible.setFitWidth(imgViewCible.getFitWidth());
            imgViewCible.setFitHeight(imgViewCible.getFitHeight());
            imgViewCible.setUserData("tuile");
            return true;
        }
        return false;
    }

    private static void mettreAJourBoutonPasser(Button btnPasser, StatistiqueJeu statistique) {
        if (statistique.actionsEffectuees() >= statistique.actionsMaxParTour()) {
            btnPasser.setText("Terminer");
        } else {
            btnPasser.setText("Passer le tour");
        }
    }

	public static boolean coupValide(PlateauDeJeu plateau, Tuile tuile, int col, int ligne,
			StatistiqueJeu statistique) {
		if (!statistique.peutJouer()) {
			System.out.println("Action impossible");
			return false;
		}

		Joueur joueur = statistique.joueurActuel();
		Case uneCase = plateau.caseSur(new Coordonnee(col, ligne));
		if (Arbitre.peutPoserTuile(plateau, uneCase, tuile)) {
			plateau.poserTuile(uneCase, tuile);
			Arbitre.calculeScore(joueur, uneCase);
			System.out.println(joueur.score());
			joueur.lblScore().setText("Score : " + joueur.score());
			System.out.println(joueur.score());
			joueur.incrementerNbTuilePose();
			joueur.lblTuilePose().setText("Tuiles posées : " + joueur.nbTuilePose());
			statistique.augmentationActionsEffectuees();
			statistique.majLabelActionAutomatique();

			return true;
		}
		return false;
	}

}
