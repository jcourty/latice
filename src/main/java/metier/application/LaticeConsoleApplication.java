package metier.application;

import java.util.List;

import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.plateau.Case;
import metier.plateau.Coordonnee;
import metier.plateau.PlateauDeJeu;
import metier.tuile.Couleur;
import metier.tuile.Symbole;
import metier.tuile.Tuile;
import vue.Console;

public class LaticeConsoleApplication {


	public static void main(String[] args) {

		Console.titre("-- Bienvenue dans notre magnifique jeu de latice --");
		Console.sautLigne();
		
		
		PlateauDeJeu plateau = new PlateauDeJeu();
		
		//[TEST] à décommenter pour remplir le plateau de symboles aléatoires
		//remplissagePlateau(plateau);
		
		Case case1 = plateau.caseSur(new Coordonnee(1, 1));
        Case case2 = plateau.caseSur(new Coordonnee(1, 2));
        Tuile tuile1 = new Tuile(Symbole.PLUME, Couleur.VERT);
        Tuile tuile2 = new Tuile(Symbole.PLUME, Couleur.ROUGE);
        
        plateau.poserTuile(case1, tuile1);
        plateau.poserTuile(case2, tuile2);
		Console.message(plateau.afficherConsole());

	}

	public static void distribuerDansChevalet(List<Joueur> joueurs) {
		for (int i = 0; i < 5; i++) {
			for (Joueur joueur : joueurs) {
				Tuile tuile = joueur.piocherDansMain();
				joueur.ajouterDansChevalet(tuile);
			}
		}
	}

	static void joueursAfficherMain(List<Joueur> joueurs) {
		for (Joueur joueur : joueurs) {
			joueur.afficherMain();
			Console.nombre(joueur.tailleMain());
		}
	}

	static void joueursAfficherChevalet(List<Joueur> joueurs) {
		for (Joueur joueur : joueurs) {
			joueur.afficherChevalet();
			Console.nombre(joueur.tailleChevalet());
		}
	}

	static void distribuerTuile(TasDeTuile pioche, List<Joueur> joueurs) {
		while (!pioche.estVide()) {
			for (Joueur joueur : joueurs) {
				Tuile tuile = pioche.piocherTuile();
				joueur.ajouterDansMain(tuile);
			}
		}
	}

	public static void remplissagePlateau(PlateauDeJeu plateau) {
		Couleur[] couleurs = { Couleur.BLEU, Couleur.CYAN, Couleur.JAUNE, Couleur.MAGENTA, Couleur.ROUGE,
				Couleur.VERT };
		Symbole[] symboles = { Symbole.DAUPHIN, Symbole.FLEUR, Symbole.GECKO, Symbole.OISEAU, Symbole.PLUME,
				Symbole.TORTUE };

		for (int row = 1; row <= 9; row++) {
			for (int col = 1; col <= 9; col++) {
				Coordonnee coordonnee = new Coordonnee(row, col);
				Symbole symbole = symboles[(int) (Math.random() * symboles.length)];
				Couleur couleur = couleurs[(int) (Math.random() * couleurs.length)];
				Tuile tuile = new Tuile(symbole, couleur);
				Case uneCase = new Case(coordonnee, null);

				if (plateau.estVide()) {
					plateau.poserTuile(uneCase, tuile);
				} else {
					uneCase = plateau.caseSur(coordonnee);
					boolean booleanAleatoire = Math.random() < 0.5;
					Coordonnee coordonneePrecedente;
					if (row <= 1) {
						coordonneePrecedente = new Coordonnee(row, col - 1);
					} else {
						coordonneePrecedente = new Coordonnee(row - 1, col);
					}

					Case casePrecedente = plateau.caseSur(coordonneePrecedente);
					if (booleanAleatoire) {
						symbole = plateau.tuileSur(casePrecedente).symbole();
						while (!plateau.tuileAdjacenteSimilaire(uneCase, tuile)) {
							couleur = couleurs[(int) (Math.random() * couleurs.length)];
							tuile = new Tuile(symbole, couleur);
						}
					} else {
						couleur = plateau.tuileSur(casePrecedente).couleur();
						while (!plateau.tuileAdjacenteSimilaire(uneCase, tuile)) {
							symbole = symboles[(int) (Math.random() * symboles.length)];
							tuile = new Tuile(symbole, couleur);
						}
					}

					uneCase = new Case(coordonnee, null);
					plateau.poserTuile(uneCase, tuile);
				}

			}
		}
	}
}