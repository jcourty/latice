package metier.application;

import metier.arbitre.Arbitre;
import vue.Console;

public class LaticeConsoleApplication {

    public static void main(String[] args) {

        Console.titre("-- Bienvenue dans notre magnifique jeu de latice --");
        Console.sautLigne();
        Arbitre arbitre = new Arbitre();
        arbitre.lancementDePartie();
    }
}
