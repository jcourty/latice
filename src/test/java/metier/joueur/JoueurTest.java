package metier.joueur;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metier.tuile.Tuile;

class JoueurTest {
	private static final int TAILLE_CHEVALET = 5;
	Joueur joueur1;
	Joueur joueur2;
	Joueur joueur3;
	Joueur joueur4;

	TasDeTuile pioche;

	@BeforeEach
	void initialiser() {
		pioche = new TasDeTuile();
		pioche.creerTasDeTuile();
		pioche.melanger();

		joueur1 = new Joueur("Joueur1");
		joueur2 = new Joueur("Joueur2");
		joueur3 = new Joueur("Joueur3");
		joueur4 = new Joueur("Joueur4");
	}

	@Test
	void distribution_egale_deux_joueurs() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);
		joueurs.add(joueur2);
		int tailleMain = pioche.taillePioche() / joueurs.size();
		pioche.distribuerTuile(joueurs);
		assertEquals(joueur1.tailleMain(), tailleMain);
	}

	@Test
	void distribution_egale_trois_joueurs() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);
		joueurs.add(joueur2);
		joueurs.add(joueur3);
		int tailleMain = pioche.taillePioche() / joueurs.size();
		pioche.distribuerTuile(joueurs);
		assertEquals(joueur1.tailleMain(), tailleMain);
	}

	@Test
	void distribution_egale_quatre_joueurs() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);
		joueurs.add(joueur2);
		joueurs.add(joueur3);
		joueurs.add(joueur4);
		int tailleMain = pioche.taillePioche() / joueurs.size();
		pioche.distribuerTuile(joueurs);
		assertEquals(joueur1.tailleMain(), tailleMain);
	}

	@Test
	void distribution_dans_chevalet() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);
		joueurs.add(joueur2);
		pioche.distribuerTuile(joueurs);

		joueur1.remplirChevalet();
		joueur2.remplirChevalet();
		joueur2.piocherDansChevalet(3);
		joueur2.remplirChevalet();
		
		assertEquals(joueur1.tailleChevalet(), TAILLE_CHEVALET);
		assertEquals(joueur2.tailleChevalet(), TAILLE_CHEVALET);
	}

	@Test
	void piocher_indice_dans_chevalet() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);

		pioche.distribuerTuile(joueurs);
		joueur1.remplirChevalet();

		List<Tuile> chevaletJoueur1 = joueur1.chevalet();
		Tuile tuileAttendue = chevaletJoueur1.get(3);
		Tuile tuilePiochee = joueur1.piocherDansChevalet(3);

		assertEquals(tuileAttendue, tuilePiochee);
	}

}
