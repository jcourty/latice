package metier.joueur;

import metier.tuile.Tuile;

public class Chevalet extends TasDeTuile {
	
	public Chevalet() {
		super();
	}
	
	public Tuile piocherTuile(Integer rangTuile) {
		Tuile tuile = listeTuiles.get(rangTuile);
		listeTuiles.remove(tuile);
		return tuile;
	}

	
	
}

