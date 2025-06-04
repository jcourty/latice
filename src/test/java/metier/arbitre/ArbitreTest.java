package metier.arbitre;



import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;

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



import exception.SaisieInvalideException;

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

		joueur1 = new Joueur("Joueur1");

		joueur2 = new Joueur("Joueur2");

		arbitre = new Arbitre();

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

	void test_nombre_joueur_entree_vide() {

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier(" \n2\n");

		int result = arbitre.nombreJoueur();

		assertEquals(2, result);

	}



	@Test

	void test_nombre_joueur_trop_petit() {

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("1\n2\n");

		int result = arbitre.nombreJoueur();

		assertEquals(2, result);

	}



	@Test

	void test_nombre_joueur_trop_grand() {

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("5\n2\n");

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

	void test_creation_liste_joueur_pseudo_vide_puis_valide() {

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("\nPseudoValide\n");

		List<Joueur> joueurs = arbitre.creationListeJoueur(1);

		assertEquals(1, joueurs.size());

		assertEquals("PseudoValide", joueurs.get(0).pseudo());

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

	void test_distribuer_tuile_pioche_vide() {

		List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);

		TasDeTuile piocheVide = new TasDeTuile();



		arbitre.distribuerTuile(piocheVide, joueurs);

		assertEquals(0, joueur1.tailleMain());

		assertEquals(0, joueur2.tailleMain());

		assertTrue(piocheVide.estVide());

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

	void test_distribuer_dans_chevalet_main_insuffisante() {

		List<Joueur> joueurs = Arrays.asList(joueur1);

		joueur1.ajouterDansMain(new Tuile(Symbole.TORTUE, Couleur.BLEU));

		joueur1.ajouterDansMain(new Tuile(Symbole.DAUPHIN, Couleur.ROUGE));

		joueur1.ajouterDansMain(new Tuile(Symbole.FLEUR, Couleur.VERT));



		arbitre.distribuerDansChevalet(joueurs);

		assertEquals(3, joueur1.tailleChevalet());

	}



	@Test

	void test_afficherChevaletJoueur() {



		joueur1.ajouterDansMain(new Tuile(Symbole.GECKO, Couleur.BLEU));

		joueur1.remplirChevalet();

		arbitre.afficherChevaletJoueur(joueur1);

		assertFalse(donneesSorties.toString().isEmpty());

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

	void test_choisirCoordonnee_entree_non_numerique_x_lance_exception() {

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("abc\n5\n");

		plateau.poserTuile(new Case(new Coordonnee(5, 5), Type.SIMPLE), new Tuile(Symbole.TORTUE, Couleur.BLEU),

				joueur1);

		SaisieInvalideException thrown = assertThrows(SaisieInvalideException.class, () -> {

			arbitre.choisirCoordonnee(plateau);

		});

		assertEquals("La ligne saisie n'est pas un nombre entier.", thrown.getMessage());

	}



	@Test

	void test_choisirCoordonnee_entree_non_numerique_y_lance_exception() {

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("5\ndef\n");

		plateau.poserTuile(new Case(new Coordonnee(5, 5), Type.SIMPLE), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),

				joueur1);

		SaisieInvalideException thrown = assertThrows(SaisieInvalideException.class, () -> {

			arbitre.choisirCoordonnee(plateau);

		});

		assertEquals("La colonne saisie n'est pas un nombre entier.", thrown.getMessage());

	}



	@Test

	void test_peutPoserTuile_quand_valide() {

		Case centre = plateau.caseSur(new Coordonnee(5, 5));

		Case adjacente = plateau.caseSur(new Coordonnee(5, 4));

		plateau.poserTuile(centre, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE), joueur1);

		boolean resultat = Arbitre.peutPoserTuile(plateau, adjacente, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE),

				joueur1);

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

		void test_entree_index_invalide_puis_valide() {

			transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("9\n1\n");

			List<Tuile> tuiles = joueur.listeChevalet();

			Tuile attendue = tuiles.get(0);

			Tuile actuelle = arbitre.choixChevalet(joueur);

			assertEquals(attendue, actuelle);

		}



		@Test

		void test_choixChevalet_index_negatif_puis_valide() {

			transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("-1\n4\n");

			List<Tuile> tuiles = joueur.listeChevalet();

			Tuile attendue = tuiles.get(3);

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



		@Test

		void test_toutes_adjacentes_differentes() {

			plateau.poserTuile(plateau.caseSur(new Coordonnee(4, 5)), new Tuile(Symbole.FLEUR, Couleur.ROUGE), joueur1);

			plateau.poserTuile(plateau.caseSur(new Coordonnee(6, 5)), new Tuile(Symbole.TORTUE, Couleur.VERT), joueur1);

			plateau.poserTuile(plateau.caseSur(new Coordonnee(5, 4)), new Tuile(Symbole.DAUPHIN, Couleur.JAUNE),

					joueur1);

			plateau.poserTuile(plateau.caseSur(new Coordonnee(5, 6)), new Tuile(Symbole.FLEUR, Couleur.BLEU), joueur1);



			boolean result = Arbitre.tuileAdjacenteSimilaire(plateau, centre, tuileReference, joueur1);

			assertEquals(false, result);

		}



		@Test

		void test_tuiles_adjacentes_partiellement_similaires() {

			plateau.poserTuile(centre, tuileReference, joueur1);

			plateau.poserTuile(plateau.caseSur(new Coordonnee(4, 5)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU),

					joueur1);

			plateau.poserTuile(plateau.caseSur(new Coordonnee(6, 5)), new Tuile(Symbole.FLEUR, Couleur.ROUGE), joueur1);



			boolean result = Arbitre.tuileAdjacenteSimilaire(plateau, centre, tuileReference, joueur1);

			assertEquals(false, result);

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

		void gagne_0_point_sans_adjacence() {

			plateau.poserTuile(centre, tuileReference, joueur1);

			Arbitre.calculeScore(joueur1, centre);

			assertEquals(0, joueur1.score());

		}



		@Test

		void gagne_0_point_quand_pas_de_latice() {

			plateau.poserTuile(centre, tuileReference, joueur1);

			Case uneCase = plateau.caseSur(new Coordonnee(5, 6));

			plateau.poserTuile(uneCase, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE), joueur1);

			Arbitre.calculeScore(joueur1, uneCase);

			assertEquals(0, joueur1.score());

		}

	}



	@Test

	void test_echangerChevalet() {

		for (int i = 0; i < 5; i++) {

			joueur1.ajouterDansMain(new Tuile(Symbole.DAUPHIN, Couleur.BLEU));

		}

		joueur1.remplirChevalet();

		assertEquals(5, joueur1.tailleChevalet());



		for (int i = 0; i < 3; i++) {

			joueur1.ajouterDansMain(new Tuile(Symbole.FLEUR, Couleur.ROUGE));

		}



		List<Tuile> ancienChevalet = new ArrayList<>(joueur1.listeChevalet());



		arbitre.echangerChevalet(joueur1);



		assertEquals(5, joueur1.tailleChevalet());

		assertNotEquals(ancienChevalet, joueur1.listeChevalet());



	}



	@Test

	void test_joueurGagnant_un_gagnant() {

		joueur1.setScore(10);

		joueur1.incrementerNbTuilePose();

		joueur2.setScore(5);

		joueur2.incrementerNbTuilePose();

		joueur1.incrementerNbTuilePose();



		List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);

		List<Joueur> gagnants = Arbitre.joueurGagnant(joueurs);



		assertEquals(1, gagnants.size());

		assertEquals(joueur1, gagnants.get(0));

	}



	@Test

	void test_joueurGagnant_egalite_parfaite() {

		joueur1.setScore(10);

		joueur1.incrementerNbTuilePose();

		joueur2.setScore(10);

		joueur2.incrementerNbTuilePose();



		List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);

		List<Joueur> gagnants = Arbitre.joueurGagnant(joueurs);



		assertEquals(2, gagnants.size());

		assertTrue(gagnants.contains(joueur1));

		assertTrue(gagnants.contains(joueur2));

	}



	@Test

	void test_joueurGagnant_aucun_joueur() {

		List<Joueur> joueurs = new ArrayList<>();

		List<Joueur> gagnants = Arbitre.joueurGagnant(joueurs);



		assertTrue(gagnants.isEmpty());

	}



	@Test

	void test_lancementDePartie_gestion_exception_nb_joueur() throws InterruptedException {

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("abc\n2\nJoueurA\nJoueurB\n4\n4\n");



		StringBuilder inputBuilder = new StringBuilder();

		inputBuilder.append("abc\n");

		inputBuilder.append("2\n");

		inputBuilder.append("JoueurA\n");

		inputBuilder.append("JoueurB\n");



		for (int i = 0; i < 10; i++) {

			inputBuilder.append("4\n");

			inputBuilder.append("4\n");

		}



		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier(inputBuilder.toString());



		arbitre.lancementDePartie();



		String output = donneesSorties.toString();

		assertTrue(output.contains("Le nombre de joueurs doit être un nombre entier."));

		assertTrue(output.contains("Le joueur gagnant est") || output.contains("Les gagnants sont :"));

	}



	@Test

	void test_lancementDePartie_gestion_exception_choix_chevalet() throws InterruptedException {

		StringBuilder inputBuilder = new StringBuilder();

		inputBuilder.append("2\n");

		inputBuilder.append("JoueurA\n");

		inputBuilder.append("JoueurB\n");



		inputBuilder.append("1\n");

		inputBuilder.append("abc\n");

		inputBuilder.append("1\n");

		inputBuilder.append("5\n5\n");

		inputBuilder.append("4\n");



		inputBuilder.append("4\n");



		for (int i = 1; i < 10; i++) {

			inputBuilder.append("4\n");

			inputBuilder.append("4\n");

		}



		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier(inputBuilder.toString());



		arbitre.lancementDePartie();



		String output = donneesSorties.toString();

		assertTrue(output.contains("Entrée non valide : veuillez entrer un nombre entier : "));

	}



	@Test

	void test_echangerChevalet_vide_puis_rempli() {

		joueur1.viderChevalet();

		assertEquals(0, joueur1.tailleChevalet());



		for (int i = 0; i < 5; i++) {

			joueur1.ajouterDansMain(new Tuile(Symbole.TORTUE, Couleur.ROUGE));

		}



		arbitre.echangerChevalet(joueur1);



		assertEquals(5, joueur1.tailleChevalet());

		assertEquals(0, joueur1.tailleMain());

	}



	@Test

	void test_menu_choix_invalide_puis_valide() throws InterruptedException {

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("0\n4\n");



		arbitre.menu(plateau, joueur1, 0);

		String output = donneesSorties.toString();

		assertTrue(output.contains("Choix invalide ou Pas assez d'actions."));

	}



	@Test

	void test_menu_acheter_action_assez_de_points() throws InterruptedException {

		joueur1.setScore(5);

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("3\n4\n");



		arbitre.menu(plateau, joueur1, 0);

		assertEquals(3, joueur1.score());

	}



	@Test

	void test_menu_acheter_action_pas_assez_de_points() throws InterruptedException {

		joueur1.setScore(1);

		transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("3\n4\n");



		arbitre.menu(plateau, joueur1, 0);

		assertEquals(1, joueur1.score());

		String output = donneesSorties.toString();

		assertTrue(output.contains("Pas assez de points pour acheter une action"));

	}

}