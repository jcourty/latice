package metier.arbitre;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javafx.scene.layout.GridPane;
import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.tuile.Tuile;
import vue.Console;

public class Arbitre {

	private Scanner scanner = new Scanner(System.in);

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

	public List<Joueur> creationListeJoueurFX(int nombreJoueur,List<GridPane> gridPanes) {
		List<Joueur> joueurs = new ArrayList<>();

		for (int i = 1; i <= nombreJoueur; i++) {
			String nom = "Didier";
			System.out.println(gridPanes.get(i-1));
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

	public static Joueur choisirJoueurAleatoire(List<Joueur> joueurs) {
		Random random = new Random();
		int index = random.nextInt(joueurs.size());
		return joueurs.get(index);
	}

	public int debutDePartie() {
		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();
		int nb_joueurs = this.nombreJoueur();
		List<Joueur> joueurs = this.creationListeJoueur(nb_joueurs);
		distribuerTuile(pioche, joueurs);
		distribuerDansChevalet(joueurs);
		afficherChevaletJoueurs(joueurs);

		int joueurActuel = joueurs.indexOf(choisirJoueurAleatoire(joueurs));

		return joueurActuel;
	}
}