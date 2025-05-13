package metier.joueur;

import metier.tuile.Tuile;

public class Joueur {

	private String pseudo;
	private Chevalet chevalet;
	private TasDeTuile main;
	
	public Joueur(String pseudo, Chevalet chevalet, TasDeTuile main) {
		this.pseudo = pseudo;
		this.chevalet = chevalet;
		this.main = main;
	}
	
	public void mettreDansChevalet(Tuile tuile) {
		chevalet.ajouterTuile(tuile);	
	}
	
	public Tuile piocherDansMain() {
		return main.piocherTuile();
	}
	
	public Tuile piocherDansChevalet(int rang) {
		return chevalet.piocherTuile(rang);
	}
	
	
	
}
