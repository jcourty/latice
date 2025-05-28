package metier.arbitre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javafx.scene.layout.GridPane;
import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.plateau.Case;
import metier.plateau.Coordonnee;
import metier.plateau.PlateauDeJeu;
import metier.tuile.Tuile;
import vue.Console;

public class Arbitre {

	private final Scanner scanner;

	public Arbitre() {
		scanner = new Scanner(System.in);
	}
	
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
					Console.ligne("Erreur : le nombre doit être entre 2 et 4 : ");
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
	
	public List<Joueur> creationListeJoueurFX(int nombreJoueur,List<GridPane> gridPanes) {
		List<Joueur> joueurs = new ArrayList<>();

		for (int i = 1; i <= nombreJoueur; i++) {
			String nom = "Didier";
			joueurs.add(new Joueur(nom,gridPanes.get(i-1)));
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
		for (Joueur joueur : joueurs) {
			joueur.remplirChevalet();
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

	public Coordonnee choisirCoordonnee(PlateauDeJeu plateau, Tuile tuile) {
		Coordonnee coordonnee = null;
		if (!plateau.estVide()) {

			Console.ligne("Entrer le x : ");
			int x = scanner.nextInt();
			Console.ligne("Entrer le y : ");
			int y = scanner.nextInt();
			coordonnee = new Coordonnee(x, y);

		} else {
			coordonnee = new Coordonnee(5, 5);
		}
		return coordonnee;
	}

	public Tuile choixChevalet(Joueur joueur) {
		boolean saisieValide = false;
		Tuile tuile = null;
		while (!saisieValide) {
			Console.ligne("Quelle tuile voulez-vous poser ? (entre 1 et 5, 6 pour changer de chevalet) : ");
			joueur.afficherChevalet();
			if (scanner.hasNextInt()) {

				int choix = scanner.nextInt() - 1;
				scanner.nextLine();
				if (choix >= 0 && choix <= 5) {
					if (choix == 5) {
						joueur.viderChevalet();
						joueur.remplirChevalet();

					} else {
						tuile = joueur.piocherDansChevalet(choix);
						saisieValide = true;
					}
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

	public void debutDePartie(List<Joueur> joueurs, int nombreJoueur, PlateauDeJeu plateau) {
		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();
		distribuerTuile(pioche, joueurs);
		distribuerDansChevalet(joueurs);

		List<Joueur> ordreDuTour = new ArrayList<>(joueurs);
		Collections.shuffle(ordreDuTour);

		for (int tour = 0; tour < 10; tour++) {
			for (Joueur joueurActuel : ordreDuTour) {

				Console.message(plateau.afficherConsole());
				Console.message("Tour de : " + joueurActuel.pseudo());
				Console.message((tour + 1) + " tour");

				boolean tuilePosee = false;

				if (plateau.estVide()) {
					Tuile tuile = choixChevalet(joueurActuel);
					Coordonnee coordonnee = choisirCoordonnee(plateau, tuile);
					Case uneCase = plateau.caseSur(coordonnee);
					plateau.poserTuile(uneCase, tuile);
					joueurActuel.remplirChevalet();
				} else {
					while (!tuilePosee) {
						Tuile tuile = choixChevalet(joueurActuel);
						Coordonnee coordonnee = choisirCoordonnee(plateau, tuile);
						Case uneCase = plateau.caseSur(coordonnee);

						tuilePosee = plateau.peutPoserTuile(uneCase, tuile);
						if (tuilePosee) {
							tuilePosee = true;
							plateau.poserTuile(uneCase, tuile);
							joueurActuel.remplirChevalet();
						} else {
							joueurActuel.ajouterDansChevalet(tuile);
							Console.message(plateau.afficherConsole());
							Console.message("Tuile invalide : ");
						}
					}
				}
			}
		}
		scanner.close();
		System.out.println("Fin de partie");
	}
}