package metier.joueur;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metier.arbitre.Arbitre;
import metier.tuile.Couleur;
import metier.tuile.Symbole;
import metier.tuile.Tuile;

class JoueurTest {
	Joueur joueur1 ;
	Joueur joueur2 ;
	Joueur joueur3 ;
	Joueur joueur4 ;
	
	TasDeTuile pioche ;
	
	Arbitre arbitre;
	
	@BeforeEach
	void initialiser() {
		pioche = new TasDeTuile();
		pioche.creerTasDeTuile();
		pioche.melanger();
		
		joueur1 = new Joueur("Joueur1");
		joueur2 = new Joueur("Joueur2");
		joueur3 = new Joueur("Joueur3");
		joueur4 = new Joueur("Joueur4");
		
		arbitre = new Arbitre();
	}
	
	
	@Test
	void distribution_egale_deux_joueurs() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);
		joueurs.add(joueur2);
		int tailleMain = pioche.taillePioche()/joueurs.size();
		arbitre.distribuerTuile(pioche, joueurs);
		assertEquals(joueur1.tailleMain(),tailleMain);
	}
	
	@Test
	void distribution_egale_trois_joueurs() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);
		joueurs.add(joueur2);
		joueurs.add(joueur3);
		int tailleMain = pioche.taillePioche()/joueurs.size();
		arbitre.distribuerTuile(pioche, joueurs);
		assertEquals(joueur1.tailleMain(),tailleMain);
	}
	
	@Test
	void distribution_egale_quatre_joueurs() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);
		joueurs.add(joueur2);
		joueurs.add(joueur3);
		joueurs.add(joueur4);
		int tailleMain = pioche.taillePioche()/joueurs.size();
		arbitre.distribuerTuile(pioche, joueurs);
		assertEquals(joueur1.tailleMain(),tailleMain);
	}
	
	@Test 
	void distribution_dans_chevalet() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);
		joueurs.add(joueur2);
		arbitre.distribuerTuile(pioche, joueurs);
		joueur1.distribuerDansChevalet();
		joueur2.distribuerDansChevalet();
		assertEquals(5,joueur1.tailleChevalet());
		assertEquals(5,joueur2.tailleChevalet());
	}
	
	@Test
	void piocher_indice_dans_chevalet() {
	    List<Joueur> joueurs = new ArrayList<>();
	    joueurs.add(joueur1);
	    
	    arbitre.distribuerTuile(pioche, joueurs);
	    joueur1.distribuerDansChevalet();

	    List<Tuile> chevaletJoueur1 = joueur1.listeChevalet();
	    Tuile tuileAttendue = chevaletJoueur1.get(3);
	    Tuile tuilePiochee = joueur1.piocherDansChevalet(3);

	    assertEquals(tuileAttendue, tuilePiochee);
	}
	
	@Test
	void remplissage_chevalet_correct() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);
		arbitre.distribuerTuile(pioche, joueurs);
		assertEquals(0, joueur1.tailleChevalet());
		joueur1.remplirChevalet();
		assertEquals(5, joueur1.tailleChevalet());
		joueur1.piocherDansChevalet(1);
		joueur1.remplirChevalet();
		assertEquals(5, joueur1.tailleChevalet());
	}
	
	@Test
	void remplissage_chevalet_main_vide() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);
		assertEquals(0, joueur1.tailleChevalet());
		joueur1.remplirChevalet();
		assertEquals(0, joueur1.tailleChevalet());
	}
	
	@Test
	void remplissage_chevalet_main_moins_de_5() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(joueur1);
		for (int i=0;i<4;i++) {
			joueur1.ajouterDansMain(new Tuile(Symbole.GECKO, Couleur.ROUGE));
		}
		joueur1.remplirChevalet();
		assertEquals(4, joueur1.tailleChevalet());
	}


	
}
