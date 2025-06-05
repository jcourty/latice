package metier.application;

import metier.arbitre.Arbitre;
import vue.Console;

public class LaticeConsoleApplication {

    public static void main(String[] args) {

        Console.titre("-- Bienvenue dans notre magnifique jeu de latice --");
        Console.message(""
        		+ "En jouant à Latice, vous n'avez qu'un seul objectif : être le premier à vider votre pioche en seulement dix tours."
        		+ "\r\n"
        		+ "Pour cela, rien de plus simple : il vous suffit de poser vos tuiles à côté de celles déjà sur le plateau."
        		+ "\r\n"
        		+ "Mais attention, seule une tuile similaire, par le symbole ou bien la couleur, est acceptée.\r\n"
        		+ "\r\n"
        		+ "À chaque tour vous pouvez soit échanger votre chevalet, soit passer votre tour, soit obtenir des actions supplémentaires coûtant 2 points.\r\n"
        		+ "\r\n"
        		+ "Le système de points fonctionne ainsi :\r\n"
        		+ "\r\n"
        		+ "Un double : la tuile jouée correspond par couleur ou symbole sur deux de ses côtés, vous gagnez 1 point\r\n"
        		+ "Un triple : la tuile jouée correspond par couleur ou symbole sur trois de ses côtés, vous gagnez 2 points\r\n"
        		+ "Un Latice : la tuile jouée correspond par couleur ou symbole sur quatre de ses côtés, vous gagnez 4 points\r\n"
        		+ "Placez votre tuile sur un soleil pour gagner 2 points\r\n"
        		+ "Latice Pro ©"
        		);
        Console.sautLigne();
        Arbitre arbitre = new Arbitre();
		arbitre.lancementDePartie();

    }

}
