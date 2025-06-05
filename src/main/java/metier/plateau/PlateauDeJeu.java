package metier.plateau;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import metier.tuile.Couleur;
import metier.tuile.Tuile;

public class PlateauDeJeu {

	private HashMap<Case, Tuile> plateau;
	private static final List<Coordonnee> CASES_SOLEIL = Arrays.asList(
		    new Coordonnee(1, 1), new Coordonnee(2, 2), new Coordonnee(3, 3),
		    new Coordonnee(7, 7), new Coordonnee(8, 8), new Coordonnee(9, 9),
		    new Coordonnee(1, 9), new Coordonnee(2, 8), new Coordonnee(3, 7),
		    new Coordonnee(9, 1), new Coordonnee(8, 2), new Coordonnee(7, 3),
		    new Coordonnee(1, 5), new Coordonnee(5, 1), new Coordonnee(9, 5),
		    new Coordonnee(5, 9)
	);
	private static final String SOLEIL = Type.SOLEIL.afficher();
	private static final String LUNE = Type.LUNE.afficher();
	private static final String BLANC = "\u001B[0m";

	public PlateauDeJeu() {
		plateau = new HashMap<>();
		creationPlateau();
	}

	public Map<Case, Tuile> plateau() {
		return plateau;
	}
	
	public Set<Entry<Case, Tuile>> plateauEntrySet() {
		return plateau.entrySet();
	}
	
	public boolean contientTuile(Case uneCase) {
		if (plateau.get(uneCase) == null) {
			return false;
		}
		return plateau.containsKey(uneCase);
	}

	public boolean estVide() {
	    for (Tuile tuile : plateau.values()) {
	        if (tuile != null) {
	            return false;
	        }
	    }
	    return true;
	}

	public void poserTuile(Case uneCase, Tuile uneTuile) {
		plateau.put(uneCase, uneTuile);
	}

	public int nombreTuileSurPlateau() {
	    int compteur = 0;
	    for (Tuile tuile : plateau.values()) {
	        if (tuile != null ) {
	            compteur++;
	        }
	    }
	    return compteur;
	}

	public void vider() {
		plateau.clear();
	}

	public Tuile retirerTuileSur(Case uneCase) {
		return plateau.remove(uneCase);
	}

	public Tuile tuileSur(Case uneCase) {
		return plateau.get(uneCase);
	}

	public Case caseSur(Coordonnee coordonnee) {
		for (Case uneCase : plateau.keySet()) {
			if (uneCase.coordonnee().equals(coordonnee)) {
				return uneCase;
			}
		}
		return null;
	}

	public void creationPlateau() {
	    for (int ligne = 1; ligne <= 9; ligne++) {
	        for (int col = 1; col <= 9; col++) {
	            Coordonnee coordonnee = new Coordonnee(ligne, col);
	            Type type = recupererTypeCase(ligne, col);
	            Tuile tuile = null;
	            Case c = new Case(coordonnee, type);
	            plateau.put(c, tuile);
	        }
	    }
	}

	private Type recupererTypeCase(int ligne, int col) {
	    if (estCaseSoleil(ligne, col)) {
	        return Type.SOLEIL;
	    } else if (estCaseLune(ligne, col)) {
	        return Type.LUNE;
	    } else {
	        return Type.SIMPLE;
	    }
	}

	private boolean estCaseSoleil(int ligne, int col) {
	    return CASES_SOLEIL.contains(new Coordonnee(ligne, col));
	}

	private boolean estCaseLune(int ligne, int col) {
	    return ligne == 5 && col == 5;
	}

	public String afficherConsole() {
		StringBuilder plateauConsole = new StringBuilder();

		plateauConsole.append("   ");
		for (int ligne = 1; ligne <= 9; ligne++) {
			plateauConsole.append("  " + ligne + "  ");
		}
		plateauConsole.append("\n");

		plateauConsole.append("   ");
		plateauConsole.append(generationMultiple(Couleur.BLEU.codeCouleur() + "-----", 9) + "\n" + BLANC);

		for (int ligne = 1; ligne <= 9; ligne++) {
			plateauConsole.append(" " + ligne + Couleur.BLEU.codeCouleur() + " |");

			for (int col = 1; col <= 9; col++) {
				Coordonnee coordonnee = new Coordonnee(ligne, col);
				Case c = this.caseSur(coordonnee);
				Tuile tuile = this.tuileSur(c);

				if (tuile != null) {
					plateauConsole.append(" " + tuile.afficher() + Couleur.BLEU.codeCouleur() + " |");
				} else {
					if (c.type() == Type.SOLEIL) {
						plateauConsole.append(Couleur.JAUNE.codeCouleur() + SOLEIL + Couleur.BLEU.codeCouleur() + "|");
					} else if (c.type() == Type.LUNE) {
						plateauConsole.append(Couleur.JAUNE.codeCouleur() + LUNE + Couleur.BLEU.codeCouleur() + "|");
					} else {
						plateauConsole.append("    |");
					}
				}
			}
			plateauConsole.append("\n");
			plateauConsole.append("   ");
			plateauConsole.append(generationMultiple(Couleur.BLEU.codeCouleur() + "-----", 9) + "\n" + BLANC);
		}
		return plateauConsole.toString();
	}

	private String generationMultiple(String paterne, int compteur) {
		StringBuilder constructeur = new StringBuilder();
		for (int i = 0; i < compteur; i++) {
			constructeur.append(paterne);
		}
		return constructeur.toString();
	}

}