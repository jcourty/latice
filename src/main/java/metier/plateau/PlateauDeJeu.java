package metier.plateau;

import java.util.HashMap;

import metier.tuile.Tuile;

public class PlateauDeJeu {

	private HashMap<Case, Tuile> plateau;
	
	public PlateauDeJeu() {
		plateau = new HashMap<>();
	}

	public HashMap<Case, Tuile> plateau() {
		return plateau;
	}

	public boolean contientTuile(Case uneCase) {
		return plateau.containsKey(uneCase);
	}

	public boolean estVide() {
		return plateau.isEmpty();
	}

	public void poserTuile(Case uneCase, Tuile uneTuile) {
		if (!contientTuile(uneCase)) {
			plateau.put(uneCase, uneTuile);
		}
	}

	public void clear() {
		plateau.clear();
	}

	public Tuile tuileSur(Case uneCase) {
		return plateau.get(uneCase);
	}

	public Case caseSur(Coordonnee coordonnee) {
		Case caseRecherche = new Case(coordonnee);
		for (Case uneCase : plateau.keySet()) {
			if (uneCase.coordonneEgal(caseRecherche)) {
				return uneCase;
			}
		}
		return null;
	}

}