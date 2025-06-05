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

	private static final String JOUEUR = "Joueur ";
	private final Scanner scanner;
	private static int nombreCaseAdjacente;
	private int nbAction;

	public Arbitre() {
		scanner = new Scanner(System.in);
	}

	public int nombreJoueur() throws SaisieInvalideException {
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
				Console.ligne(JOUEUR + i + ", veuillez entrer votre pseudo : ");
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
			TextInputDialog dialogue = new TextInputDialog(JOUEUR + i);
			dialogue.setTitle("Nom du joueur " + i);
			dialogue.setHeaderText(null);
			dialogue.setContentText("Entrez le nom du joueur " + i + " :");
			String nom = dialogue.showAndWait().orElse(JOUEUR + i);
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

	public Coordonnee choisirCoordonnee(PlateauDeJeu plateau) throws SaisieInvalideException {
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

	public Tuile choixChevalet(Joueur joueur) throws SaisieInvalideException { 
		boolean saisieValide = false;
		Tuile tuile = null;
		while (!saisieValide) {
			Console.message("Quelle tuile voulez-vous poser ? (entre 1 et 5) :");
			Console.message("Pour un retour en arrière entrer 6 :");
			joueur.afficherChevalet();
			try {
				int choix = scanner.nextInt();
				scanner.nextLine();
				if (choix >= 1 && choix <= 5) {
					tuile = joueur.piocherDansChevalet(choix - 1);
					saisieValide = true;
				} else if (choix == 6) {
					saisieValide = true;
				} else {
					Console.ligne("Index invalide : entrez un nombre entre 1 et 5 ou 6 pour retour :");
				}
			} catch (InputMismatchException e) {
				Console.ligne("Entrée non valide : veuillez entrer un nombre entier :");
				scanner.nextLine();
				throw new SaisieInvalideException("Le choix de tuile n'est pas un nombre entier.", e); // Lancer l'exception ici
			}
		}
		return tuile;
	}

	public static boolean tuileAdjacenteSimilaire(PlateauDeJeu plateau, Case uneCase, Tuile uneTuile) {
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

	public static boolean peutPoserTuile(PlateauDeJeu plateau, Case uneCase, Tuile uneTuile) {
		if (plateau.estVide() && uneCase.coordonneeX() == 5 && uneCase.coordonneeY() == 5) {
			return true;
		}
		if (plateau.contientTuile(uneCase)) {
			return false;
		}
		return tuileAdjacenteSimilaire(plateau, uneCase, uneTuile);
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
                pause(1000); 
            }
        }

        PlateauDeJeu plateau = new PlateauDeJeu();
        TasDeTuile pioche = new TasDeTuile();
        pioche.creerTasDeTuile();
        distribuerTuile(pioche, joueurs);
        distribuerDansChevalet(joueurs);

        List<Joueur> ordreDuTour = new ArrayList<>(joueurs);
        Collections.shuffle(ordreDuTour);
        
        nbTourMax = determinerNombreTour(joueurs);

        for (int tour = 0; tour < nbTourMax; tour++) {
            for (Joueur joueurActuel : ordreDuTour) {
                menu(plateau, joueurActuel, tour);
            }
        }
        scanner.close();
        Console.message(gestionVictoire(joueurGagnant(joueurs)));
    }


	public static int determinerNombreTour(List<Joueur> joueurs) {
		if (joueurs.size() == 2) {
			return 10;
		} else if (joueurs.size() == 3) {
			return 8;
		} else {
			return 6;
		}
	}

	private void poserTuileAvecValidation(PlateauDeJeu plateau, Joueur joueurActuel) throws SaisieInvalideException {
		Tuile tuileAPoser = null;
		Coordonnee coordonneeChoisie = null;
		Case caseCible = null;
		boolean tuileEstPosee = false;

		while (!tuileEstPosee) {
			tuileAPoser = demanderTuileAuJoueur(joueurActuel);
			if (tuileAPoser == null) {
				return; 
			}

			coordonneeChoisie = demanderCoordonneesAuJoueur(plateau);
			caseCible = plateau.caseSur(coordonneeChoisie);
			tuileEstPosee = tenterDePoserTuile(plateau, caseCible, tuileAPoser, joueurActuel);
		}
		joueurActuel.incrementerNbTuilePose();
		nbAction++;
	}

	private Tuile demanderTuileAuJoueur(Joueur joueur) throws SaisieInvalideException {
		while (true) {
			try {
				Tuile tuile = choixChevalet(joueur);
				if (tuile == null) { 
					return null;
				}
				return tuile;
			} catch (SaisieInvalideException e) {
				Console.message(e.getMessage());
				pause(1000);
			}
		}
	}

	private Coordonnee demanderCoordonneesAuJoueur(PlateauDeJeu plateau) throws SaisieInvalideException {
		while (true) {
			try {
				return choisirCoordonnee(plateau);
			} catch (SaisieInvalideException e) {
				Console.message(e.getMessage());
				pause(1000);
			}
		}
	}

	private boolean tenterDePoserTuile(PlateauDeJeu plateau, Case caseCible, Tuile tuile, Joueur joueur) {
		if (peutPoserTuile(plateau, caseCible, tuile)) {
			plateau.poserTuile(caseCible, tuile);
			calculeScore(joueur, caseCible);
			joueur.remplirChevalet();
			return true;
		} else {
			joueur.ajouterDansChevalet(tuile);
			Console.message(plateau.afficherConsole());
			Console.message("Tuile invalide :");
			return false;
		}
	}

	private void pause(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

	public void echangerChevalet(Joueur joueur) {
		joueur.viderChevalet();
		joueur.remplirChevalet();
	}

	public void menu(PlateauDeJeu plateau, Joueur joueur, int tour) {
        int choix = 0;
        boolean choixValide = false;
        nbAction = 0;
        int nbActionMax = 1;

        while (!choixValide) {
            afficherMenu(plateau, joueur, tour, nbActionMax);
            try {
                choix = scanner.nextInt();
                scanner.nextLine();
                switch (choix) {
                    case 1:
                        if (nbAction < nbActionMax) {
                            poserTuileAvecValidation(plateau, joueur);
                        } else {
                            Console.message("Pas assez d'actions pour jouer une tuile.");
                            pause(1000);
                        }
                        break;
                    case 2:
                        if (nbAction < nbActionMax) {
                            echangerChevalet(joueur);
                            nbAction++;
                            Console.effacerConsole();
                        } else {
                            Console.message("Pas assez d'actions pour échanger le chevalet.");
                            pause(1000);
                        }
                        break;
                    case 3:
                        if (joueur.score() >= 2) {
                            joueur.ajouterScore(-2);
                            nbActionMax++;
                            Console.message("Une action supplémentaire achetée.");
                            pause(1000);
                        } else {
                            Console.message("Pas assez de points pour acheter une action.");
                            pause(1000);
                        }
                        break;
                    case 4:
                        choixValide = true;
                        break;
                    default:
                        Console.message("Choix invalide.");
                        pause(1000);
                        break;
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                Console.message("Entrée non valide : veuillez entrer un nombre entier.");
                pause(1000);
            }
        }
    }

    public void afficherMenu(PlateauDeJeu plateau, Joueur joueur, int tour, int nbActionMax) {
        Console.effacerConsole();
        Console.separateur();
        Console.message("Tour de : " + joueur.pseudo());
        Console.message((tour + 1) + " tour");
        Console.message(plateau.afficherConsole());
        Console.titre("Menu");
        Console.message("Score de " + joueur.pseudo() + " : " + joueur.score());
        Console.message("Nombre d'actions effectuées : " + nbAction + "/" + nbActionMax);
        Console.message("Nombre de tuiles posées : " + joueur.nbTuilePose());
        Console.sautLigne();
        Console.message("1. Jouer une tuile (1 action)");
        Console.message("2. Echanger le chevalet (1 action)");
        Console.message("3. Acheter une action (2 points)");
        if (nbAction >= nbActionMax) {
            Console.message("4. Terminer le tour");
        } else {
            Console.message("4. Passer le tour");
        }
        Console.sautLigne();
        Console.ligne("Chevalet : ");
        joueur.afficherChevalet();
        Console.ligne("Saisir un choix : ");
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
		String message;
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