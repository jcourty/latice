package metier.joueur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metier.tuile.Tuile;

class TasDeTuileTest {
	private static final int TAILLE_PIOCHE = 72;
	TasDeTuile pioche;

	@BeforeEach
	private void initialiser() {
		pioche = new TasDeTuile();
	}

	@Test
	void nouvelle_pioche_vide() {
		assertEquals(pioche.taillePioche(), 0);
	}

	@Test
	void pioche_pleine() {
		pioche.creerTasDeTuile();
		assertEquals(pioche.taillePioche(), TAILLE_PIOCHE);
		assertFalse(pioche.estVide());
	}

	@Test
	void pioche_est_melanger() {
		pioche.creerTasDeTuile();
		TasDeTuile deuxiemePioche = new TasDeTuile(pioche);
		pioche.melanger();
		assertFalse(pioche.equals(deuxiemePioche));
	}

	@Test
	void piocherTuile_aleatoire() {
		pioche.creerTasDeTuile();
		TasDeTuile deuxiemePioche = new TasDeTuile(pioche);
		List<Tuile> listeTuilesPremiere = new ArrayList<>();
		List<Tuile> listeTuilesSeconde = new ArrayList<>();
		for (int i = 0; i < TAILLE_PIOCHE; i++) {
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
		assertEquals(tuilePioche, null);
	}

}
