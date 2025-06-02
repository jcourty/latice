package metier.arbitre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.plateau.Case;
import metier.plateau.Coordonnee;
import metier.plateau.PlateauDeJeu;
import metier.plateau.Type;
import metier.tuile.Tuile;
import vue.Console;

public class Arbitre {

	private final Scanner scanner;
	private static int nombreCaseAdjacente ;
	
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

	public List<Joueur> creationListeJoueurFX(int nombreJoueur, List<GridPane> gridPanes, List<Label> labels, List<Label> scores) {
		List<Joueur> joueurs = new ArrayList<>();
		for (int i = 1; i <= nombreJoueur; i++) {
			Label label = labels.get(i - 1);
			TextInputDialog dialogue = new TextInputDialog("Joueur " + i);
			dialogue.setTitle("Nom du joueur " + i);
			dialogue.setHeaderText(null);
			dialogue.setContentText("Entrez le nom du joueur " + i + " :");
			String nom = dialogue.showAndWait().orElse("Joueur " + i);
			
			label.setText(nom);
			joueurs.add(new Joueur(nom, gridPanes.get(i - 1), scores.get(i - 1)));
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

	public Coordonnee choisirCoordonnee(PlateauDeJeu plateau) {
		if (plateau.estVide()) {
			return new Coordonnee(5, 5);
		} else {
			Console.ligne("Entrez la ligne : ");
			int x = scanner.nextInt();
			Console.ligne("Entrer la colonne : ");
			int y = scanner.nextInt();
			return new Coordonnee(x, y);
		}
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

	public static boolean tuileAdjacenteSimilaire(PlateauDeJeu plateau, Case uneCase, Tuile uneTuile, Joueur joueur) {
		nombreCaseAdjacente = 0 ;
		int x = uneCase.coordonneeX();
		int y = uneCase.coordonneeY();
		int nombreCaseSimilaire = 0;
		int taille = 9;

		int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		for (int[] direction : directions) {
			int xAdjacent = x + direction[0];
			int yAdjacent = y + direction[1];
			if (xAdjacent >= 0 && xAdjacent <= taille && yAdjacent >= 0 && yAdjacent <= taille) {
				Coordonnee coordonnee = new Coordonnee(xAdjacent, yAdjacent);
				Case caseAdjacente = plateau.caseSur(coordonnee);
				if (plateau.contientTuile(caseAdjacente)) {
					nombreCaseAdjacente++;
					if (uneTuile.estSimilaire(plateau.tuileSur(caseAdjacente))) {
						nombreCaseSimilaire++;
					}
				}
			}
		}
		if (nombreCaseAdjacente > 0) {
			return (nombreCaseSimilaire == nombreCaseAdjacente);
		} else {
			return false;
		}
	}

	public static boolean peutPoserTuile(PlateauDeJeu plateau, Case uneCase, Tuile uneTuile, Joueur joueur) {
		if (plateau.estVide() && uneCase.coordonneeX() == 5 && uneCase.coordonneeY() == 5) {
			return true;
		}
		if (plateau.contientTuile(uneCase)) {
			return false;
		}
		return tuileAdjacenteSimilaire(plateau, uneCase, uneTuile, joueur);
	}

	public void debutDePartie(List<Joueur> joueurs, PlateauDeJeu plateau) {
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
				Console.message("Score de " + joueurActuel.pseudo() + " : " + joueurActuel.score());

				if (plateau.estVide()) {
					Tuile tuile = choixChevalet(joueurActuel);
					Coordonnee coordonnee = choisirCoordonnee(plateau);
					Case uneCase = plateau.caseSur(coordonnee);
					plateau.poserTuile(uneCase, tuile, joueurActuel);
					joueurActuel.remplirChevalet();
				} else {
					poserTuileAvecValidation(plateau, joueurActuel);
				}
			}
		}
		scanner.close();
		Joueur joueurGagne = joueurGagnant(joueurs);
		Console.message("Le joueur gagnant est : " + joueurGagne.pseudo());
	}

	private void poserTuileAvecValidation(PlateauDeJeu plateau, Joueur joueurActuel) {
		boolean tuilePosee = false;
		while (!tuilePosee) {
			Tuile tuile = choixChevalet(joueurActuel);
			Coordonnee coordonnee = choisirCoordonnee(plateau);
			Case uneCase = plateau.caseSur(coordonnee);
			tuilePosee = peutPoserTuile(plateau, uneCase, tuile, joueurActuel);
			if (tuilePosee) {
				plateau.poserTuile(uneCase, tuile, joueurActuel);
				joueurActuel.remplirChevalet();
			} else {
				joueurActuel.ajouterDansChevalet(tuile);
				Console.message(plateau.afficherConsole());
				Console.message("Tuile invalide : ");
			}
		}
	}

	public static Joueur joueurGagnant(List<Joueur> joueurs) {
		int nbTuilePoseMax = 0;
		Joueur joueurGagnant = joueurs.get(0); //recupère le premier joueur de la liste
		for (Joueur joueur : joueurs) {
			if (joueur.nbTuilePose() > nbTuilePoseMax) {
				nbTuilePoseMax = joueur.nbTuilePose();
				joueurGagnant = joueur;
			}
		}
		return joueurGagnant;
	}

	public static void calculeScore(Joueur joueur, Case uneCase) {
		int score = 0;
		if (uneCase.type() == Type.SOLEIL) {
			score += 2;
		}
		if (nombreCaseAdjacente == 4) {
			score += nombreCaseAdjacente;
		} else if (nombreCaseAdjacente > 1) {
	
			score += nombreCaseAdjacente - 1;
		}
		joueur.ajouterScore(score);

	}


}
