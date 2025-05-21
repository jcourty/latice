package metier.tuile;

import javafx.scene.image.Image;

public class Tuile {

	public static final Tuile NO = null;

	private final Symbole symbole;
	private final Couleur couleur;
	
	public Tuile(Symbole symbole, Couleur couleur) {
		this.symbole = symbole;
		this.couleur = couleur;
	}

	public Symbole symbole() {
		return symbole;
	}

	public Couleur couleur() {
		return couleur;
	}

	public String afficher() {
		return couleur.colorier(symbole.symbole());
	}

	public boolean estSimilaire(Tuile tuile) {
		return couleur == tuile.couleur || symbole == tuile.symbole;
		
	}
	
	public Image getImage() {
        return new Image(getClass().getResourceAsStream("/images/" + symbole + couleur + ".png"));
    }

}