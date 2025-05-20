package metier.joueur;


import org.junit.jupiter.api.BeforeEach;

class JoueurTest {
	Joueur joueur1 ;
	Joueur joueur2 ;
	Joueur joueur3 ;
	Joueur joueur4 ;
	
	TasDeTuile pioche ;
	
	@BeforeEach
	void initialiser() {
		pioche = new TasDeTuile();
		pioche.creerTasDeTuile();
		pioche.melanger();
		
		joueur1 = new Joueur("Joueur1");
		joueur2 = new Joueur("Joueur2");
		joueur3 = new Joueur("Joueur3");
		joueur4 = new Joueur("Joueur4");
	}
	
}
