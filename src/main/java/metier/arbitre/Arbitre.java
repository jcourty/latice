package metier.arbitre;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import metier.joueur.Chevalet;
import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.plateau.Case;
import metier.plateau.Coordonnee;
import metier.plateau.PlateauDeJeu;
import metier.tuile.Tuile;
import vue.Console;

public class Arbitre {

	private final Scanner scanner = new Scanner(System.in);

	public int nombreJoueur() {
		int nombreJoueur = 0;
		boolean saisieValide = false;

		Console.ligne("Entrez le nombre de joueurs (entre 2 et 4) : ");
		while (!saisieValide) {
			if (scanner.hasNextInt()) {
				nombreJoueur = scanner.nextInt();
				scanner.nextLine(); 
				if (nombreJoueur >= 2 && nombreJoueur <= 4) {
					saisieValide = true;
				} else {
					Console.ligne("Erreur : le nombre doit être entre 2 et 4.");
				}
			} else {
				Console.ligne("Erreur : veuillez entrer un nombre entier : ");
				scanner.nextLine(); 
			}
		}

		return nombreJoueur;
	}

	public List<Joueur> creationListeJoueur(int nombreJoueur) {
		List<Joueur> joueurs = new ArrayList<>();

		for (int i = 1; i <= nombreJoueur; i++) {
			boolean saisieValide = false;
			while (!saisieValide) {
				Console.ligne("Joueur " + i + ", veuillez entrer votre pseudo : ");
				String pseudo = scanner.nextLine();
				if (pseudo.isEmpty()) {
					Console.ligne("Le pseudo ne peut pas être vide.");
				} else {
					joueurs.add(new Joueur(pseudo));
					saisieValide = true;
				}
			}
		}

		return joueurs;
	}

	public void distribuerTuile(TasDeTuile pioche, List<Joueur> joueurs) {
		while (!pioche.estVide()) {
			for (Joueur joueur : joueurs) {
				Tuile tuile = pioche.piocherTuile();
				joueur.ajouterDansMain(tuile);
			}
		}
	}

	public void distribuerDansChevalet(List<Joueur> joueurs) {
		for (int i = 0; i < 5; i++) {
			for (Joueur joueur : joueurs) {
				Tuile tuile = joueur.piocherDansMain();
				joueur.ajouterDansChevalet(tuile);
			}
		}
	}

	public void afficherChevaletJoueurs(List<Joueur> joueurs) {
		for (Joueur joueur : joueurs) {
			joueur.afficherChevalet();
			Console.nombre(joueur.tailleChevalet());
		}
	}

	public void afficherChevaletJoueur(Joueur joueur) {
		joueur.afficherChevalet();
	}

	public static Joueur choisirJoueurAleatoire(List<Joueur> joueurs) {
		Random random = new Random();
		int index = random.nextInt(joueurs.size());
		return joueurs.get(index);
	}

	public Coordonnee choisirCoordonnee(PlateauDeJeu plateau,Tuile tuile) {
		Coordonnee coordonnee = null;
		Boolean saisieValide = false;
		if (!plateau.estVide()) {
			while (!saisieValide) {
				Console.ligne("Entrer le x : ");
				int x = scanner.nextInt();
				Console.ligne("Entrer le y : ");
				int y = scanner.nextInt();
				coordonnee = new Coordonnee(x, y);
				Case uneCase = plateau.caseSur(coordonnee);
				if (plateau.peutPoserTuile(uneCase, tuile)) {
					plateau.poserTuile(uneCase, tuile);
					saisieValide = true ;
				} else {
					Console.ligne("Coordonnee invalide");
				}
			}
		} else {
			Case uneCase = plateau.caseSur(new Coordonnee(5,5));
			plateau.poserTuile(uneCase, tuile);
		}
		return coordonnee;
	}

	public Tuile choisirDansChevalet(TasDeTuile main, Chevalet chevalet) {
		boolean saisieValide = false;
		Tuile tuile = null;

		while (!saisieValide) {
			Console.ligne("Quelle tuile voulez-vous poser ? (entre 1 et 5) : ");

			if (scanner.hasNextInt()) {
				int index = scanner.nextInt()-1;
				scanner.nextLine();
				if (index >= 0 && index <= 4) {
					tuile = chevalet.piocherTuile(index);
					saisieValide = true;
					chevalet.remplirChevalet(main);
				} else {
					Console.ligne("Index invalide : entrez un nombre entre 1 et 5 : ");
				}
			} else {
				Console.ligne("Entrée non valide : veuillez entrer un nombre entier : ");
				scanner.nextLine();
			}
		}

		return tuile;
	}

	public void debutDePartie(TasDeTuile pioche, List<Joueur> joueurs, int nombreJoueur, PlateauDeJeu plateau) {
		distribuerTuile(pioche, joueurs);
		distribuerDansChevalet(joueurs);
		afficherChevaletJoueurs(joueurs);

		int joueurActuel = joueurs.indexOf(choisirJoueurAleatoire(joueurs));

		for (int tour = 0; tour < 10; tour++) {
			Console.message(plateau.afficherConsole());
			Joueur joueur = joueurs.get(joueurActuel);
			Console.message("Tour de : " + joueur.pseudo());
			joueur.afficherChevalet();

			Tuile tuile = choisirDansChevalet(joueur.main(), joueur.chevalet());
			Coordonnee coordonnee = choisirCoordonnee(plateau,tuile);
			Console.message(tuile.afficher());
			//TODO Permettre de changer de tuile dans le cas ou il n'est pas possible de la poser
			
			joueurActuel = (joueurActuel + 1) % nombreJoueur;
		}
	}
}