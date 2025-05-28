package metier.application;

import java.util.List;

import metier.arbitre.Arbitre;
import metier.joueur.Joueur;
import metier.plateau.PlateauDeJeu;
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

        arbitre.debutDePartie(joueurs, plateau);

    }
}
