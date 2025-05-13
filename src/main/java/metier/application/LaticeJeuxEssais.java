package metier.application;

import java.util.ArrayList;
import java.util.List;

import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.tuile.Tuile;

public class LaticeJeuxEssais {
	
	private static final String SEPARATEUR = "---------------------------------------------------";

	public static void main(String[] args) {
		
		System.out.println(SEPARATEUR);
		System.out.println("-- Latice jeux d'essais--");
		System.out.println(SEPARATEUR);
		System.out.println("");

		List<Joueur> joueurs = new ArrayList<>();
		TasDeTuile pioche = new TasDeTuile();
		boolean mainValideJ1 = false;
		boolean chevaletValideJ1 = false;
		boolean mainValideJ2 = false;
		boolean chevaletValideJ2 = false;
		boolean piocheVide = false;
		boolean taillePiocheValide = false;
		int tailleMain;
		
		pioche.creerTasDeTuile();
		System.out.println(pioche.taillePioche());
		if (pioche.taillePioche() == 72) {
			taillePiocheValide = true;
		}
		
		pioche.melanger();
		joueurs.add(new Joueur("Didier"));
		joueurs.add(new Joueur("Pedro"));
		tailleMain = pioche.taillePioche()/joueurs.size();
		distribuerTuile(pioche, joueurs);
		
		if (joueurs.get(0).tailleMain() == tailleMain){
			mainValideJ1 = true;
		}
		if (joueurs.get(0).tailleMain() == tailleMain){
			mainValideJ2 = true;
		}
		
		piocheVide=pioche.estVide();
		
		distribuerDansChevalet(joueurs);
		
		if (joueurs.get(0).tailleChevalet() == 5){
			chevaletValideJ1 = true;
		}
		if (joueurs.get(0).tailleChevalet() == 5){
			chevaletValideJ2 = true;
		}

		System.out.println("Main du joueur 1 valide : " + mainValideJ1);
		System.out.println("Main du joueur 2 valide : " + mainValideJ2);
		
		System.out.println("Chevalet du joueur 1 valide : " + chevaletValideJ1);
		System.out.println("Chevalet du joueur 2 valide : " + chevaletValideJ2);
		
		System.out.println("Taille de la pioche valide au départ : " + taillePiocheValide);
		System.out.println("Pioche vide après distribution : " + piocheVide);



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