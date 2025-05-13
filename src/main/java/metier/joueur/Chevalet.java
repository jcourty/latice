package metier.joueur;

import metier.tuile.Tuile;

public class Chevalet extends TasDeTuile {
	
	public Chevalet() {
		super();
	}
	
	public void afficher() {
		for (Tuile tuiles: listeTuiles)
			System.out.println(tuiles);
	}
	
	public Tuile piocherTuileAvecIndice(Integer rangTuile) {
		Tuile tuile = listeTuiles.get(rangTuile);
		listeTuiles.remove(rangTuile);
		return tuile;
	}
	
	public void ajouterTuile(Tuile tuileAjouter) {
		listeTuiles.add(tuileAjouter);
	}
}
