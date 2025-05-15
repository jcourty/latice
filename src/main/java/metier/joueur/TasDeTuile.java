package metier.joueur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import metier.tuile.Couleur;
import metier.tuile.Symbole;
import metier.tuile.Tuile;
import vue.Console;

public class TasDeTuile {
	protected List<Tuile> listeTuiles;

	public TasDeTuile(List<Tuile> listeTuiles) {
		this.listeTuiles = listeTuiles;
	}

	public TasDeTuile(TasDeTuile autreTasDeTuile) {
		this.listeTuiles = new ArrayList<>(autreTasDeTuile.listeTuiles);
	}

	public TasDeTuile() {
		this.listeTuiles = new ArrayList<>();
	}

	public List<Tuile> listeTuiles() {
		return listeTuiles;
	}


	public void ajouterTuile(Tuile tuile) {
		listeTuiles.add(tuile);
	}

	public Tuile piocherTuile() {

		if (estVide()) {
			return null; // exception par la suite ?????
		} else {
			int nombreAleatoire = (int) (Math.random() * listeTuiles.size());
			Tuile tuile1 = listeTuiles.get(nombreAleatoire);
			listeTuiles.remove(nombreAleatoire);
			return tuile1;
		}
	}

	public void melanger() {
		Collections.shuffle(listeTuiles);
	}

	public boolean estVide() {
		return listeTuiles.isEmpty();
	}

	public void creerTasDeTuile() {
		Tuile tuile;
		for (Couleur couleur : Couleur.values()) {
			for (Symbole symbole : Symbole.values()) {
				tuile = new Tuile(symbole, couleur);
				ajouterTuile(tuile);
				ajouterTuile(tuile);
			}
		}
	}

	public void afficherTuiles() {
		for (Tuile tuile : listeTuiles) {
			Console.message(tuile.afficher());
		}
		Console.sautLigne();
	}

	public int taillePioche() {
		return listeTuiles.size();
	}

	public void distribuerTuile(List<Joueur> joueurs) {
		while (!estVide()) {
			for (Joueur joueur : joueurs) {
				Tuile tuile = piocherTuile();
				joueur.ajouterDansMain(tuile);
				
			}
		}
	}
}
