package metier.application;

import java.util.ArrayList;
import java.util.List;

import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.tuile.Tuile;

public class LaticeJeuxEssais {

	public static void main(String[] args) {

		List<Joueur> joueurs = new ArrayList<>();
		TasDeTuile pioche = new TasDeTuile();
		
		pioche.creerTasDeTuile();
		pioche.afficherTuiles();
		System.out.println(pioche.taillePioche());
		joueurs.add(new Joueur("Didier"));
		joueurs.add(new Joueur("Pedro"));
		distribuerTuile(pioche, joueurs);
		joueursAfficherMain(joueurs);
		joueursAfficherChevalet(joueurs);

		for (int i = 0; i < 5; i++) {
			for (Joueur joueur : joueurs) {
				Tuile tuile = joueur.piocherDansMain();
				joueur.ajouterDansChevalet(tuile);
			}
		}
		
		joueursAfficherMain(joueurs);
		joueursAfficherChevalet(joueurs);

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
