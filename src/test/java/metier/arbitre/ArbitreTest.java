package metier.arbitre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.plateau.Case;
import metier.plateau.Coordonnee;
import metier.plateau.PlateauDeJeu;
import metier.plateau.Type;
import metier.tuile.Couleur;
import metier.tuile.Symbole;
import metier.tuile.Tuile;

class ArbitreTest {

	private Joueur joueur1;
	private Joueur joueur2;
	private Arbitre arbitre;
	private PlateauDeJeu plateau;
	private ByteArrayInputStream donneesEntree;
	private final ByteArrayOutputStream donneesSorties = new ByteArrayOutputStream();

	@BeforeEach
	void initialiser() {
		plateau = new PlateauDeJeu();
		joueur1 = new Joueur("Clotaire");
		joueur2 = new Joueur("Berthenilde");
		arbitre = new Arbitre();
		System.setOut(new PrintStream(donneesSorties)); // change la sortie pour permettre de garder la valeur
	}

	@AfterEach
	void remmetSortieSurLaSortieDeBase() {
		System.setIn(System.in);
		System.setOut(System.out);
	}

	private void transformeCeQuiEstEnParametreEnEntreeCommeAuClavier(String data) {
		donneesEntree = new ByteArrayInputStream(data.getBytes());
		System.setIn(donneesEntree);
		arbitre = new Arbitre();
	}

	@Test
	void test_nombre_joueur_entree_valide() {
		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("3\n");
		int result = arbitre.nombreJoueur();
		assertEquals(3, result);
	}

	@Test
	void test_jombre_joueur_entree_invalide() {
		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("abc\n5\n2\n");
		int result = arbitre.nombreJoueur();
		assertEquals(2, result);
	}

	@Test
	void test_jombre_joueur_entree_vide() {
		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier(" \n2\n");
		int result = arbitre.nombreJoueur();
		assertEquals(2, result);
	}

	@Test
	void test_creation_liste_joueur() {
		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("Karim\nMohamed\nDidier\n");
		List<Joueur> joueurs = arbitre.creationListeJoueur(3);
		assertEquals(3, joueurs.size());
		assertEquals("Karim", joueurs.get(0).pseudo());
		assertEquals("Mohamed", joueurs.get(1).pseudo());
		assertEquals("Didier", joueurs.get(2).pseudo());
	}

	@Test
	void test_distribuer_tuile() {
		List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);

		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();

		arbitre.distribuerTuile(pioche, joueurs);

		for (Joueur joueur : joueurs) {
			assertTrue(joueur.tailleMain() > 0);
		}

		assertTrue(pioche.estVide());
	}

	@Test
	void test_distribuer_dans_chevalet() {
		List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);

		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();

		arbitre.distribuerTuile(pioche, joueurs);
		arbitre.distribuerDansChevalet(joueurs);

		for (Joueur joueur : joueurs) {
			assertEquals(5, joueur.tailleChevalet());
		}

		for (Joueur joueur : joueurs) {
			assertTrue(joueur.tailleMain() < 36);
		}
	}

	@Test
	void test_choisirCoordonnee_plateauNonVide() {
		Case uneCase = new Case(new Coordonnee(5, 5), Type.SIMPLE);
		Tuile tuile = new Tuile(Symbole.GECKO, Couleur.ROUGE);
		plateau.poserTuile(uneCase, tuile, joueur1);

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("4\n5\n");
		Coordonnee coordonnee = arbitre.choisirCoordonnee(plateau);

		assertEquals(4, coordonnee.x());
		assertEquals(5, coordonnee.y());
	}

	@Test
	void test_choisirCoordonnee_plateauVide() {
		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("\n");
		Coordonnee coordonnee = arbitre.choisirCoordonnee(plateau);

		assertEquals(5, coordonnee.x());
		assertEquals(5, coordonnee.y());
	}
	
	@Test
	void test_peutPoserTuile_quand_valide() {
		Case centre = plateau.caseSur(new Coordonnee(5, 5));
		Case adjacente = plateau.caseSur(new Coordonnee(5, 4));
		plateau.poserTuile(centre, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE), joueur1);
		boolean resultat = Arbitre.peutPoserTuile(plateau, adjacente, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE), joueur1);
		assertTrue(resultat);
	}

	@Test
	void test_peutPoserTuile_quand_plateau_vide() {
		Case centre = plateau.caseSur(new Coordonnee(5, 5));
		boolean resultat = Arbitre.peutPoserTuile(plateau, centre, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE), joueur1);
		assertTrue(resultat);
	}

	@Test
	void test_ne_peut_pas_poser_quand_tuile_sur_case() {
		Case centre = plateau.caseSur(new Coordonnee(5, 5));
		plateau.poserTuile(centre, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE), joueur1);
		boolean resultat = Arbitre.peutPoserTuile(plateau, centre, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE), joueur1);
		assertFalse(resultat);
	}
	
	
	@Nested
	class ChoixChevaletTests {

		private Joueur joueur;
		private List<Joueur> joueurs;
		private TasDeTuile pioche;

		@BeforeEach
		void initialiser() {
			joueur = new Joueur("Andromaque");
			joueurs = new ArrayList<>();
			joueurs.add(joueur);
			pioche = new TasDeTuile();
			pioche.creerTasDeTuile();
			arbitre = new Arbitre();
			arbitre.distribuerTuile(pioche, joueurs);
			arbitre.distribuerDansChevalet(joueurs);
		}

		@Test
		void test_recherche_tuile_correct() {
			transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("2\n");
			List<Tuile> tuiles = joueur.listeChevalet();
			Tuile expected = tuiles.get(1);
			Tuile actual = arbitre.choixChevalet(joueur);
			assertEquals(expected, actual);
		}

		@Test
		void test_changement_de_chevalet() {
			transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("6\n2\n");
			List<Tuile> ancienChevalet = new ArrayList<>(joueur.listeChevalet());
			arbitre.choixChevalet(joueur);
			List<Tuile> nouveauChevalet = joueur.listeChevalet();
			assertNotEquals(ancienChevalet, nouveauChevalet);
		}

		@Test
		void test_entree_non_numerique_puis_valide() {
			transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("abc\n3\n");
			List<Tuile> tuiles = joueur.listeChevalet();
			Tuile attendue = tuiles.get(2);
			Tuile actuelle = arbitre.choixChevalet(joueur);
			assertEquals(attendue, actuelle);
		}

		@Test
		void test_entree_index_invalide_puis_valide() {
			transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("9\n1\n");
			List<Tuile> tuiles = joueur.listeChevalet();
			Tuile attendue = tuiles.get(0);
			Tuile actuelle = arbitre.choixChevalet(joueur);
			assertEquals(attendue, actuelle);
		}
	}

	@Nested
	class TuileAdjacenteSimilaireTests {

		private PlateauDeJeu plateau;
		private Tuile tuileReference;
		private Case centre;
		private Joueur joueur1;

		@BeforeEach
		void initialiser() {
			plateau = new PlateauDeJeu();
			tuileReference = new Tuile(Symbole.DAUPHIN, Couleur.BLEU);
			centre = plateau.caseSur(new Coordonnee(5, 5));
			joueur1 = new Joueur("Francois");
		}

		@Test
		void test_aucune_tuile_adjacente() {
			boolean resultat = Arbitre.tuileAdjacenteSimilaire(plateau, centre, tuileReference, joueur1);
			assertEquals(false, resultat);
		}

		@Test
		void test_une_tuile_adjacente_similaire() {
			Case adjacente = plateau.caseSur(new Coordonnee(4, 5));
			Tuile nouvelleTuile = new Tuile(Symbole.DAUPHIN, Couleur.BLEU);
			plateau.poserTuile(centre, tuileReference, joueur1);
			boolean resultat = Arbitre.tuileAdjacenteSimilaire(plateau, adjacente, nouvelleTuile, joueur1);

			assertEquals(true, resultat);
		}

		@Test
		void test_une_tuile_adjacente_differente() {
			Case adjacente = plateau.caseSur(new Coordonnee(4, 5));
			plateau.poserTuile(adjacente, new Tuile(Symbole.FLEUR, Couleur.ROUGE), joueur1);
			boolean resultat = Arbitre.tuileAdjacenteSimilaire(plateau, centre, tuileReference, joueur1);
			assertEquals(false, resultat);
		}

		@Test
		void test_trois_adjacentes_similaires_et_une_vide() {
			plateau.poserTuile(centre, tuileReference, joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(4, 5)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(6, 5)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(5, 4)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);

			boolean result = Arbitre.tuileAdjacenteSimilaire(plateau, centre, tuileReference, joueur1);
			assertEquals(true, result);
		}

	}
	@Nested
	class PointsGagnés {

		private PlateauDeJeu plateau;
		private Tuile tuileReference;
		private Case centre;
		private Joueur joueur1;

		@BeforeEach
		void initialiser() {
			plateau = new PlateauDeJeu();
			tuileReference = new Tuile(Symbole.DAUPHIN, Couleur.BLEU);
			centre = plateau.caseSur(new Coordonnee(5, 5));
			joueur1 = new Joueur("Argan");
			joueur1.setScore(0);
		}
		
		@Test 
		void gagne_1_point_quand_double() {
			plateau.poserTuile(centre, tuileReference, joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(5, 6)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(6, 5)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			Arbitre.peutPoserTuile(plateau,plateau.caseSur(new Coordonnee(6, 6)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			assertEquals(joueur1.score(), 1);
		}
		
		@Test 
		void gagne_2_points_quand_triple() {
			plateau.poserTuile(centre, tuileReference, joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(6, 5)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(5, 6)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(7, 5)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(7, 6)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			Arbitre.peutPoserTuile(plateau,plateau.caseSur(new Coordonnee(6, 6)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			assertEquals(joueur1.score(), 2);
		}
		
		@Test 
		void gagne_4_points_quand_latice() {
			plateau.poserTuile(centre, tuileReference, joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(6, 5)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(5, 6)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(7, 5)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(7, 6)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(7, 7)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(6, 7)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			Arbitre.peutPoserTuile(plateau,plateau.caseSur(new Coordonnee(6, 6)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			assertEquals(joueur1.score(), 4);
		}
		
		@Test 
		void gagne_2_point_quand_posé_sur_soleil() {
			plateau.poserTuile(centre, tuileReference, joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(5, 6)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(6, 6)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			plateau.poserTuile(plateau.caseSur(new Coordonnee(6, 7)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			Arbitre.peutPoserTuile(plateau,plateau.caseSur(new Coordonnee(7, 7)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),
					joueur1);
			assertEquals(joueur1.score(), 2);
		}
	}
	
}