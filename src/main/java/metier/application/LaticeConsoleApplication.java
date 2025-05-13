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

public class LaticeConsoleApplication {

	private static final String SEPARATEUR = "---------------------------------------------------";

	public static void main(String[] args) {
		
		// Main V1
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
		
		// Main V2
		
		PlateauDeJeu plateau = new PlateauDeJeu();
		
		//remplissagePlateau(plateau);

		System.out.println(plateau.afficherConsole());

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
	
	public static void remplissagePlateau(PlateauDeJeu plateau) {
		Couleur[] couleurs = {Couleur.BLEU, Couleur.CYAN, Couleur.JAUNE, Couleur.MAGENTA, Couleur.ROUGE, Couleur.VERT};
		Symbole[] symboles = {Symbole.DAUPHIN, Symbole.FLEUR, Symbole.GECKO, Symbole.OISEAU, Symbole.PLUME, Symbole.TORTUE};
		
        for (int row = 1; row <= 9; row++) {
            for (int col = 1; col <= 9; col++) {
                Coordonnee coordonnee = new Coordonnee(row, col);
                Tuile tuile = new Tuile(symboles[(int) (Math.random() * symboles.length)], couleurs[(int) (Math.random() * couleurs.length)]);

                Case c = plateau.caseSur(coordonnee);
                plateau.poserTuile(c, tuile); 
             
            }
        }
    }
}
