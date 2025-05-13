package metier.joueur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import metier.tuile.Tuile;

public class TasDeTuile {

	public List<Tuile> listeTuiles;

	public TasDeTuile() {
		this.listeTuiles = new ArrayList<>();
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

}
