package metier.application;

import java.util.List;

import metier.arbitre.Arbitre;
import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.plateau.PlateauDeJeu;
import metier.tuile.Tuile;
import vue.Console;

public class LaticeConsoleApplication {

    public static void main(String[] args) {

        Console.titre("-- Bienvenue dans notre magnifique jeu de latice --");
        Console.sautLigne();

        // Main V1
        Console.titre("--                   Main V1                     --");
        Console.sautLigne();

        Arbitre arbitre = new Arbitre();
        int nombreJoueur = arbitre.nombreJoueur();
        List<Joueur> joueurs = arbitre.creationListeJoueur(nombreJoueur);
        PlateauDeJeu plateau = new PlateauDeJeu();
        // Main V4
        Console.titre("--                   Main V4                     --");
        Console.sautLigne();

        arbitre.debutDePartie(joueurs, nombreJoueur, plateau);

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
