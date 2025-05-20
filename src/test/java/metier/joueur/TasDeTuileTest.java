package metier.joueur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metier.tuile.Tuile;

class TasDeTuileTest {
	private static final int NB_TUILE_MAX = 72;
	TasDeTuile pioche ;
	
	@BeforeEach
	void initialiser() {
		pioche = new TasDeTuile();
	}
	
	@Test
	void nouvelle_pioche_vide() {
		assertEquals(0, pioche.taillePioche());
	}
	
	@Test
	void pioche_pleine() {
		pioche.creerTasDeTuile();
		assertEquals(NB_TUILE_MAX,pioche.taillePioche());
		assertFalse(pioche.estVide());
	}

	@Test
	void pioche_est_melanger() {
		pioche.creerTasDeTuile();
		TasDeTuile deuxiemePioche = new TasDeTuile(pioche) ;
		pioche.melanger();
		assertFalse(pioche.equals(deuxiemePioche));
	}

	@Test
	void piocherTuile_aleatoire() {
		pioche.creerTasDeTuile();
		TasDeTuile deuxiemePioche = new TasDeTuile(pioche) ;
		List<Tuile> listeTuilesPremiere = new ArrayList<>() ;
		List<Tuile> listeTuilesSeconde = new ArrayList<>() ;
		for (int i = 0; i < 72; i++) {
			Tuile tuilePioche = pioche.piocherTuile();
			Tuile tuileDeuxiemePioche = deuxiemePioche.piocherTuile();
			listeTuilesPremiere.add(tuilePioche);
			listeTuilesSeconde.add(tuileDeuxiemePioche);
		}
		
		assertFalse(listeTuilesPremiere.equals(listeTuilesSeconde));
	}
	
	@Test
	void piocherTuile_null_quand_pioche_vide() {
		Tuile tuilePioche = pioche.piocherTuile();
		assertEquals(null,tuilePioche);
	}
	
	
}