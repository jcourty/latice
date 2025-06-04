package metier.arbitre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import exception.SaisieInvalideException;
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
	private static int nombreCaseAdjacente;

	public Arbitre() {
		scanner = new Scanner(System.in);
	}

	public int nombreJoueur() {
		int nombreJoueur = 0;
		boolean saisieValide = false;

		Console.ligne("Entrez le nombre de joueurs (entre 2 et 4) : ");
		while (!saisieValide) {
			try {
				nombreJoueur = scanner.nextInt();
				scanner.nextLine();
				if (nombreJoueur >= 2 && nombreJoueur <= 4) {
					saisieValide = true;
				} else {
					Console.ligne("Erreur : le nombre doit être entre 2 et 4 : ");
				}
			} catch (InputMismatchException e) {
				scanner.nextLine();
				throw new SaisieInvalideException("Le nombre de joueurs doit être un nombre entier.", e);
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

	public List<Joueur> creationListeJoueurFX(int nombreJoueur, List<GridPane> gridPanes, List<Label> labels,
			List<Label> scores, List<Label> tuilesposses) {
		List<Joueur> joueurs = new ArrayList<>();
		for (int i = 1; i <= nombreJoueur; i++) {
			Label label = labels.get(i - 1);
			TextInputDialog dialogue = new TextInputDialog("Joueur " + i);
			dialogue.setTitle("Nom du joueur " + i);
			dialogue.setHeaderText(null);
			dialogue.setContentText("Entrez le nom du joueur " + i + " :");
			String nom = dialogue.showAndWait().orElse("Joueur " + i);

			label.setText(nom);
			joueurs.add(new Joueur(nom, gridPanes.get(i - 1), scores.get(i - 1), tuilesposses.get(i - 1)));
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
			int x = 0;
			int y = 0;
			boolean saisieValideX = false;
			boolean saisieValideY = false;

			while (!saisieValideX) {
				Console.ligne("Entrez la ligne : ");
				try {
					x = scanner.nextInt();
					saisieValideX = true;
				} catch (InputMismatchException e) {
					scanner.next();
					throw new SaisieInvalideException("La ligne saisie n'est pas un nombre entier.", e);
				}
			}

			while (!saisieValideY) {
				Console.ligne("Entrer la colonne : ");
				try {
					y = scanner.nextInt();
					saisieValideY = true;
				} catch (InputMismatchException e) {
					scanner.next();
					throw new SaisieInvalideException("La colonne saisie n'est pas un nombre entier.", e);
				}
			}
			scanner.nextLine();
			return new Coordonnee(x, y);
		}
	}

	public Tuile choixChevalet(Joueur joueur) {
		boolean saisieValide = false;
		Tuile tuile = null;
		while (!saisieValide) {
			Console.ligne("Quelle tuile voulez-vous poser ? (entre 1 et 5) : ");
			joueur.afficherChevalet();
			try {
				int choix = scanner.nextInt();
				scanner.nextLine();
				if (choix >= 1 && choix <= 5) {
					tuile = joueur.piocherDansChevalet(choix - 1);
					saisieValide = true;
				} else {
					Console.ligne("Index invalide : entrez un nombre entre 1 et 5 : ");
				}
			} catch (InputMismatchException e) {
				Console.ligne("Entrée non valide : veuillez entrer un nombre entier : ");
				scanner.nextLine();
			}
		}
		return tuile;
	}

	public static boolean tuileAdjacenteSimilaire(PlateauDeJeu plateau, Case uneCase, Tuile uneTuile, Joueur joueur) {
		nombreCaseAdjacente = 0;
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

	public void lancementDePartie() {
		List<Joueur> joueurs = null;
		boolean nbJoueurValide = false;
		int nbTourMax;
		while (!nbJoueurValide) {
			try {
				joueurs = creationListeJoueur(nombreJoueur());
				nbJoueurValide = true;
			} catch (SaisieInvalideException e) {
				Console.message(e.getMessage());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}
		}

		PlateauDeJeu plateau = new PlateauDeJeu();
		TasDeTuile pioche = new TasDeTuile();
		pioche.creerTasDeTuile();
		distribuerTuile(pioche, joueurs);
		distribuerDansChevalet(joueurs);

		List<Joueur> ordreDuTour = new ArrayList<>(joueurs);
		Collections.shuffle(ordreDuTour);
		
		if (joueurs.size() == 2) {
			nbTourMax = 10;
		}
		else if (joueurs.size() == 3) {
			nbTourMax = 8;
		}
		else if (joueurs.size() == 4) {
			nbTourMax = 6;
		}
		else {
			nbTourMax = 0;
		}

		for (int tour = 0; tour < nbTourMax; tour++) {
			for (Joueur joueurActuel : ordreDuTour) {
				menu(plateau, joueurActuel, tour);
			}
		}
		scanner.close();
		Console.message(gestionVictoire(joueurGagnant(joueurs)));
	}

	private void poserTuileAvecValidation(PlateauDeJeu plateau, Joueur joueurActuel) {
		boolean tuilePosee = false;
		while (!tuilePosee) {
			Tuile tuile = null;
			Coordonnee coordonnee = null;
			Case uneCase = null;
			boolean choixValide = false;

			while (!choixValide) {
				try {
					tuile = choixChevalet(joueurActuel);
					choixValide = true;
				} catch (SaisieInvalideException e) {
					Console.message(e.getMessage());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
						Thread.currentThread().interrupt();
					}
				}
			}
			choixValide = false;
			while (!choixValide) {
				try {
					coordonnee = choisirCoordonnee(plateau);
					choixValide = true;
				} catch (SaisieInvalideException e) {
					Console.message(e.getMessage());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
						Thread.currentThread().interrupt();
					}
				}
			}

			uneCase = plateau.caseSur(coordonnee);
			tuilePosee = peutPoserTuile(plateau, uneCase, tuile, joueurActuel);
			if (tuilePosee) {
				plateau.poserTuile(uneCase, tuile, joueurActuel);
				calculeScore(joueurActuel, uneCase);
				joueurActuel.remplirChevalet();
			} else {
				joueurActuel.ajouterDansChevalet(tuile);
				Console.message(plateau.afficherConsole());
				Console.message("Tuile invalide : ");
			}
		}
	}

	public void echangerChevalet(Joueur joueur) {
		joueur.viderChevalet();
		joueur.remplirChevalet();
	}

	public void menu(PlateauDeJeu plateau, Joueur joueur, int tour) {
		int choix = 0;
		boolean choix_valide = false;
		int nbAction = 0;
		int nbActionMax = 1;

		while (!choix_valide) {
			Console.effacerConsole();
			Console.separateur();
			Console.message("Tour de : " + joueur.pseudo());
			Console.message((tour + 1) + " tour");
			Console.message(plateau.afficherConsole());
			Console.titre("Menu");
			Console.message("Score de " + joueur.pseudo() + " : " + joueur.score());
			Console.message("Nombre d'actions : " + nbAction + "/" + nbActionMax);
			Console.message("1. Jouer une tuile (1 action)");
			Console.message("2. Echanger le chevalet (1 action)");
			Console.message("3. Acheter une action (2 points)");
			Console.message("4. Passer le tour");
			Console.ligne("Chevalet : ");
			joueur.afficherChevalet();
			Console.ligne("Saisir un choix : ");

			try {
				try {
					choix = scanner.nextInt();

					if (choix == 1 && nbAction < nbActionMax) {
						poserTuileAvecValidation(plateau, joueur);
						nbAction++;
					} else if (choix == 2 && nbAction < nbActionMax) {
						echangerChevalet(joueur);
						nbAction++;
						Console.effacerConsole();
					} else if (choix == 3) {
						if (joueur.score() >= 2) {
							joueur.ajouterScore(-2);
							nbActionMax++;
						} else {
							Console.message("Pas assez de points pour acheter une action");
							Thread.sleep(1000);
						}
					} else if (choix == 4) {
						choix_valide = true;
					} else {
						Console.message("Choix invalide ou Pas assez d'actions.");
						Thread.sleep(1000);
					}
				} catch (InputMismatchException e) {
					scanner.next();
					throw new SaisieInvalideException("Le choix de menu n'est pas un nombre entier.", e);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
			} catch (SaisieInvalideException e) {
				Console.message(e.getMessage());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	public static List<Joueur> joueurGagnant(List<Joueur> joueurs) {
		int nbTuilePoseMax = 0;
		List<Joueur> joueursGagnants = new ArrayList<>();
		for (Joueur joueur : joueurs) {
			if (joueur.nbTuilePose() >= nbTuilePoseMax) {
				nbTuilePoseMax = joueur.nbTuilePose();
				joueursGagnants.add(joueur);
			}
		}
		return joueursGagnants;
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

	public static String gestionVictoire(List<Joueur> joueurs) {
		String message = new String();
		if (joueurs.size() == 1) {
			Joueur joueur = joueurs.get(0);
			message = "Le joueur gagnant est " + joueur.pseudo();
		} else {
			message = "Les gagnants sont : ";
			for (Joueur joueur : joueurs) {
				message = message + joueur.pseudo() + " ";
			}
		}

		return message;

	}
}