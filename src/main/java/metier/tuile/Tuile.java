package metier.tuile;



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

}