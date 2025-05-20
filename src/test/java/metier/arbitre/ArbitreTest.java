package metier.arbitre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;

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

}
