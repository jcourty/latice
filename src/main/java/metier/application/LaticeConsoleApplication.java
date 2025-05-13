package metier.application;

import java.util.ArrayList;
import java.util.List;

import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.tuile.Tuile;

public class LaticeConsoleApplication {

	private static final String SEPARATEUR = "---------------------------------------------------";

	public static void main(String[] args) {
		
		System.out.println(SEPARATEUR);
		System.out.println("-- Bienvenue dans notre magnifique jeu de latice --");
		System.out.println(SEPARATEUR);
		System.out.println("");
		
		List<Joueur> joueurs = new ArrayList<>();
		TasDeTuile pioche = new TasDeTuile();
		
		pioche.creerTasDeTuile();
		System.out.println("Pioche du jeu avant mélange :");
		pioche.afficherTuiles();
		pioche.melanger();
		System.out.println("Pioche du jeu après mélange :");
		pioche.afficherTuiles();
		System.out.println(pioche.taillePioche());
		joueurs.add(new Joueur("Didier"));
		joueurs.add(new Joueur("Pedro"));
		System.out.println("Main joueurs avant distribution :");
		joueursAfficherMain(joueurs);
		joueursAfficherChevalet(joueurs);
		distribuerTuile(pioche, joueurs);
		System.out.println("Main joueurs avant mise dans chevalet :");
		joueursAfficherMain(joueurs);
		joueursAfficherChevalet(joueurs);
		distribuerDansChevalet(joueurs);
		System.out.println("Main joueurs après mise dans chevalet :");
		joueursAfficherMain(joueurs);
		System.out.println("Chevalet joueurs après mise dans chevalet :");
		joueursAfficherChevalet(joueurs);

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
			System.out.println(joueur.tailleMain());
		}
	}

	static void joueursAfficherChevalet(List<Joueur> joueurs) {
		for (Joueur joueur : joueurs) {
			joueur.afficherChevalet();
			System.out.println(joueur.tailleChevalet());
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
