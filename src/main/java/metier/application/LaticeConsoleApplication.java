package metier.application;

import java.util.ArrayList;
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
		
		// Main V1
		List<Joueur> joueurs = new ArrayList<>();
		TasDeTuile pioche = new TasDeTuile();
		PlateauDeJeu plateau = new PlateauDeJeu();

		pioche.creerTasDeTuile();
		Console.message("Pioche du jeu avant mélange :");
		pioche.afficherTuiles();
		pioche.melanger();
		Console.message("Pioche du jeu après mélange :");
		pioche.afficherTuiles();
		Console.nombre(pioche.taillePioche());
		joueurs.add(new Joueur("Didier"));
		joueurs.add(new Joueur("Pedro"));
		Console.message("Main joueurs avant distribution :");
		joueursAfficherMain(joueurs);
		joueursAfficherChevalet(joueurs);
		distribuerTuile(pioche, joueurs);
		Console.message("Main joueurs avant mise dans chevalet :");
		joueursAfficherMain(joueurs);
		joueursAfficherChevalet(joueurs);
		distribuerDansChevalet(joueurs);
		Console.message("Main joueurs après mise dans chevalet :");
		joueursAfficherMain(joueurs);
		Console.message("Chevalet joueurs après mise dans chevalet :");
		joueursAfficherChevalet(joueurs);

		// Main V2
		Console.message(plateau.afficherConsole());

		Tuile tuile1 = new Tuile(Symbole.DAUPHIN, Couleur.BLEU);
		Case case1 = new Case(new Coordonnee(5, 5));
		plateau.poserTuile(case1, tuile1);
		Console.message(plateau.afficherConsole());

		Tuile tuile2 = new Tuile(Symbole.DAUPHIN, Couleur.VERT);
		Case case2 = new Case(new Coordonnee(5, 6));
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
}