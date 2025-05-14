package metier.plateau;

import java.util.HashMap;
import java.util.Map;

import metier.tuile.Couleur;
import metier.tuile.Tuile;

public class PlateauDeJeu {

	private HashMap<Case, Tuile> plateau;
	private String soleil=" \u2600 ";
	private String lune=" \uD83C\uDF19 ";
	
	public PlateauDeJeu() {
		plateau = new HashMap<>();
		creationPlateau();
	}

	public Map<Case, Tuile> plateau() {
		return plateau;
	}

	public boolean contientTuile(Case uneCase) {
		if (plateau.get(uneCase) == null) {
			return false;
		}
		return plateau.containsKey(uneCase);
	}
	
	public boolean estVide() {
        return plateau.values().stream().allMatch(tuile -> tuile == null);
    }

	public boolean poserTuile(Case uneCase, Tuile uneTuile) {
		if (!contientTuile(uneCase)) {
			plateau.put(uneCase, uneTuile);
			return true;
		}
		else {
			return false;
		}
	}
	
	public int nombreTuileSurPlateau() {
        return (int) plateau.values().stream().filter(tuile -> tuile != null).count();
    }

	public void vider() {
		plateau.clear();
	}
	
	public Tuile retirerTuileSur(Case c) {
		return plateau.remove(c);
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
        for (int row = 1; row <= 9; row++) {
            for (int col = 1; col <= 9; col++) {
                Coordonnee coordonnee = new Coordonnee(row, col);
                Type type;
                Tuile tuile = null;
         
                // Cases soleils
                if ((row == 1 && col == 1) || (row == 2 && col == 2) || (row == 3 && col == 3) || 
                    (row == 7 && col == 7) || (row == 8 && col == 8) || (row == 9 && col == 9) ||
                    (row == 1 && col == 9) || (row == 2 && col == 8) || (row == 3 && col == 7) ||
                    (row == 9 && col == 1) || (row == 8 && col == 2) || (row == 7 && col == 3) ||
                    (row == 1 && col == 5) || (row == 5 && col == 1) || 
                    (row == 9 && col == 5) || (row == 5 && col == 9)) {
                	type = Type.SOLEIL;
                }
                // Case lune
                else if (row == 5 && col == 5) {
                    type = Type.LUNE;
                }
                // Cases simples
                else {
                    type = Type.SIMPLE;
                }

                Case c = new Case(coordonnee, type);
                plateau.put(c, tuile); 
              
            }
        }
    }
	
	public String afficherConsole() {
	    StringBuilder plateauConsole = new StringBuilder();

	    plateauConsole.append("   "); 
	    for (int col = 1; col <= 9; col++) {
	        plateauConsole.append("  " + col + "  ");
	    }
	    plateauConsole.append("\n");

	    plateauConsole.append("   "); 
	    plateauConsole.append(generationMultiple(Couleur.BLEU.couleur()+"-----", 9)+"\n\u001B[0m");

	    for (int row = 1; row <= 9; row++) {
	        plateauConsole.append(" " + row + " \u001B[34m|");

	        for (int col = 1; col <= 9; col++) {
	            Coordonnee coordonnee = new Coordonnee(row, col);
	            Case c = this.caseSur(coordonnee);
	            Tuile tuile = this.tuileSur(c);

	            if (tuile != null) {
	                plateauConsole.append(" " + tuile.afficher() + " \u001B[34m|");
	            } else {
	                if (c.type() == Type.SOLEIL) {
	                	plateauConsole.append(Couleur.JAUNE.couleur()+soleil+ Couleur.BLEU.couleur()+"|"  );
	                } else if (c.type() == Type.LUNE) {
	                	plateauConsole.append(Couleur.JAUNE.couleur()+lune+ Couleur.BLEU.couleur()+"|" );
	                } else {
	                    plateauConsole.append("    |");
	                }
	            }
	        }
	        plateauConsole.append("\n");
	        plateauConsole.append("   "); 
	        plateauConsole.append(generationMultiple(Couleur.BLEU.couleur()+"-----", 9)+"\n\u001B[0m");
	    }

	    return plateauConsole.toString();
	}

	private String generationMultiple(String pattern, int count) {
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < count; i++) {
	        builder.append(pattern);
	    }
	    return builder.toString();
	}

}