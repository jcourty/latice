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

	private ByteArrayInputStream donneesEntree;
	private final ByteArrayOutputStream donneesSorties = new ByteArrayOutputStream();
	private Arbitre arbitre;

	@BeforeEach
	void changeSortiePourLaSave() {
		System.setOut(new PrintStream(donneesSorties));
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
		arbitre = new Arbitre();
		List<Joueur> joueurs = arbitre.creationListeJoueur(3);
		assertEquals(3, joueurs.size());
		assertEquals("Karim", joueurs.get(0).pseudo());
		assertEquals("Mohamed", joueurs.get(1).pseudo());
		assertEquals("Didier", joueurs.get(2).pseudo());
	}

	@Test
	void test_distribuer_tuile() {
		Joueur joueur1 = new Joueur("Mohamed");
		Joueur joueur2 = new Joueur("Didier");
		List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);

		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();

		arbitre = new Arbitre();
		arbitre.distribuerTuile(pioche, joueurs);

		for (Joueur joueur : joueurs) {
			assertTrue(joueur.tailleMain() > 0);
		}

		assertTrue(pioche.estVide());
	}

	@Test
	void test_distribuer_dans_chevalet() {
		Joueur joueur1 = new Joueur("Alice");
		Joueur joueur2 = new Joueur("Bob");
		List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);

		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();

		arbitre = new Arbitre();
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
		PlateauDeJeu plateau = new PlateauDeJeu();
		Case uneCase = new Case(new Coordonnee(5, 5), Type.SIMPLE);
		Tuile tuile = new Tuile(Symbole.GECKO, Couleur.ROUGE);
		plateau.poserTuile(uneCase, tuile);

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("4\n5\n");
		Coordonnee coordonnee = arbitre.choisirCoordonnee(plateau);

		assertEquals(4, coordonnee.x());
		assertEquals(5, coordonnee.y());
	}

	@Test
	void test_choisirCoordonnee_plateauVide() {
		PlateauDeJeu plateau = new PlateauDeJeu();
		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("\n");
		Coordonnee coordonnee = arbitre.choisirCoordonnee(plateau);

		assertEquals(5, coordonnee.x());
		assertEquals(5, coordonnee.y());
	}

	@Nested
	class ChoixChevaletTests {

		private Joueur joueur;
		private List<Joueur> joueurs;
		private TasDeTuile pioche;

		@BeforeEach
		void setUp() {
			joueur = new Joueur("pedro");
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

		@BeforeEach
		void setUp() {
			plateau = new PlateauDeJeu();
			tuileReference = new Tuile(Symbole.DAUPHIN, Couleur.BLEU);
			centre = plateau.caseSur(new Coordonnee(5, 5));
		}

		@Test
		void test_aucune_tuile_adjacente() {
			boolean resultat = Arbitre.tuileAdjacenteSimilaire(plateau, centre, tuileReference);
			assertEquals(false, resultat);
		}

		@Test
		void test_une_tuile_adjacente_similaire() {
			Case adjacente = plateau.caseSur(new Coordonnee(4, 5));
			Tuile nouvelleTuile = new Tuile(Symbole.DAUPHIN,Couleur.BLEU);
			plateau.poserTuile(centre, tuileReference);
			boolean resultat = Arbitre.tuileAdjacenteSimilaire(plateau, adjacente, nouvelleTuile);
			
			assertEquals(true, resultat);
		}

		@Test
		void test_une_tuile_adjacente_differente() {
			Case adjacente = plateau.caseSur(new Coordonnee(4, 5));
			plateau.poserTuile(adjacente, new Tuile(Symbole.FLEUR, Couleur.ROUGE));
			boolean resultat = Arbitre.tuileAdjacenteSimilaire(plateau, centre, tuileReference);
			assertEquals(false, resultat);
		}

		@Test
		void test_trois_adjacentes_similaires_et_une_vide() {
			plateau.poserTuile(centre, tuileReference);
			plateau.poserTuile(new Case(new Coordonnee(4, 5), Type.SIMPLE), new Tuile(Symbole.DAUPHIN, Couleur.BLEU));
			plateau.poserTuile(new Case(new Coordonnee(6, 5), Type.SIMPLE), new Tuile(Symbole.DAUPHIN, Couleur.BLEU));
			plateau.poserTuile(new Case(new Coordonnee(5, 4), Type.SIMPLE), new Tuile(Symbole.DAUPHIN, Couleur.BLEU));

			boolean result = Arbitre.tuileAdjacenteSimilaire(plateau, centre, tuileReference);
			assertEquals(true, result); 
		}

		@Test
		void test_mix_similaire_et_different() {
			plateau.poserTuile(new Case(new Coordonnee(4, 5), Type.SIMPLE), new Tuile(Symbole.DAUPHIN, Couleur.BLEU));
			plateau.poserTuile(new Case(new Coordonnee(5,4), Type.SIMPLE), new Tuile(Symbole.DAUPHIN, Couleur.ROUGE));
			boolean result = Arbitre.tuileAdjacenteSimilaire(plateau, centre, tuileReference);
			assertEquals(false, result);
		}
	}
	
	@Test
	void test_peutPoserTuile_quand_valide() {
		PlateauDeJeu plateau = new PlateauDeJeu() ;
		Case centre = plateau.caseSur(new Coordonnee(5, 5));
		Case adjacente = plateau.caseSur(new Coordonnee(5,4));
		plateau.poserTuile(centre, new Tuile(Symbole.DAUPHIN,Couleur.ROUGE));
		boolean resultat = Arbitre.peutPoserTuile(plateau, adjacente, new Tuile(Symbole.DAUPHIN,Couleur.ROUGE));
		assertTrue(resultat);
	}
	
	@Test
	void test_peutPoserTuile_quand_plateau_vide() {
		PlateauDeJeu plateau = new PlateauDeJeu() ;
		Case centre = plateau.caseSur(new Coordonnee(5, 5));
		boolean resultat = Arbitre.peutPoserTuile(plateau, centre, new Tuile(Symbole.DAUPHIN,Couleur.ROUGE));
		assertTrue(resultat);
	}
	
	@Test
	void test_ne_peut_pas_poser_quand_tuile_sur_case() {
		PlateauDeJeu plateau = new PlateauDeJeu() ;
		Case centre = plateau.caseSur(new Coordonnee(5, 5));
		plateau.poserTuile(centre, new Tuile(Symbole.DAUPHIN,Couleur.ROUGE));
		boolean resultat = Arbitre.peutPoserTuile(plateau, centre, new Tuile(Symbole.DAUPHIN,Couleur.ROUGE));
		assertFalse(resultat);
	}


}
