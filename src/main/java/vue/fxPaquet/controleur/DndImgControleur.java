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
import metier.joueur.Joueur;
import metier.tuile.Tuile;

public class DndImgControleur {

	public static void manageSourceDragAndDrop(ImageView imageView, Joueur joueur, Tuile tuile) {
		imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {

				Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putImage(imageView.getImage());
				joueur.piocherDansChevalet(joueur.listeChevalet().indexOf(tuile));

				db.setContent(content);
				event.consume();
			}
		});

		imageView.setOnDragDone(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				
				joueur.remplirChevalet();
				if (joueur.tailleChevalet( ) < 5) {
					((GridPane) imageView.getParent()).getChildren().remove(imageView);
				} else {
					LaticeFXControleur.afficherChevalet(joueur);
				}
				event.consume();
			}
		});
	}

	public static void DndPourGridPane(GridPane gridPane) {
		for (Node uneCase : gridPane.getChildren()) {
			if (uneCase instanceof ImageView) {
				ImageView imageView = (ImageView) uneCase;

				imageView.setOnDragOver((DragEvent event) -> {
					if (event.getGestureSource() != imageView && event.getDragboard().hasImage()) {
						event.acceptTransferModes(TransferMode.MOVE);
					}
					event.consume();
				});

				imageView.setOnDragDropped((DragEvent event) -> {
					Dragboard db = event.getDragboard();
					boolean success = false;
					if (db.hasImage()) {
						imageView.setImage(db.getImage());
						success = true;
					}
					event.setDropCompleted(success);
					event.consume();
				});
			}
		}
	}

}
