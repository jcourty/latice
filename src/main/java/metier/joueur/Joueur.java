package metier.joueur;

import java.util.List;

import metier.tuile.Tuile;

public class Joueur {

	private final String pseudo;
	private Chevalet chevalet;
	private TasDeTuile main;
	
	public Joueur(String pseudo,Chevalet chevalet, TasDeTuile main) {
		this.pseudo = pseudo;
		this.chevalet = chevalet ;
		this.main = main;
	}
	
	public Joueur(String pseudo) {
		this(pseudo,new Chevalet(),new TasDeTuile());
	}
	
	public String pseudo() {
		return pseudo;
	}

	public TasDeTuile main() {
		return main;
	}
	
	public List<Tuile> chevalet() {
		return chevalet.listeTuiles();
	}
	
	public void ajouterDansChevalet(Tuile tuile) {
		chevalet.ajouterTuile(tuile);	
	}
	
	public Tuile piocherDansChevalet(int rang) {
		return chevalet.piocherTuile(rang);
	}
	
	public Tuile piocherDansMain() {
		return main.piocherTuile();
	}
	
	public void ajouterDansMain(Tuile tuile) {
		main.ajouterTuile(tuile);
	}
	
	public void afficherMain() {
		main.afficherTuiles();
	}
	
	public void afficherChevalet() {
		chevalet.afficherTuiles();
	}
	
	public int tailleMain() {
		return main.taillePioche();
	}
	
	public int tailleChevalet() {
		return chevalet.taillePioche();
	}
	
	public void distribuerDansChevalet() {
		for (int i = 0; i < 5; i++) {
			Tuile tuile = piocherDansMain();
			ajouterDansChevalet(tuile);
		}
	}

	
	
}
