package metier.arbitre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import metier.joueur.Joueur;

class ArbitreTourGestionTest extends ArbitreBaseTest {
	
    @Test
    void test_nombre_tour_est_10() {
        List<Joueur> joueurs = new ArrayList<>();
        joueurs.add(joueur1);
        joueurs.add(joueur2);
        int nombreTour = Arbitre.determinerNombreTour(joueurs);
        assertEquals(10, nombreTour);
    }
    
    @Test
    void test_nombre_tour_est_8() {
        List<Joueur> joueurs = new ArrayList<>();
        joueurs.add(joueur1);
        joueurs.add(joueur2);
        joueurs.add(new Joueur("Didier"));
        int nombreTour = Arbitre.determinerNombreTour(joueurs);
        assertEquals(8, nombreTour);
    }
    
    @Test
    void test_nombre_tour_est_6() {
        List<Joueur> joueurs = new ArrayList<>();
        joueurs.add(joueur1);
        joueurs.add(joueur2);
        joueurs.add(new Joueur("Didier"));
        joueurs.add(new Joueur("Pedro"));
        int nombreTour = Arbitre.determinerNombreTour(joueurs);
        assertEquals(6, nombreTour);
    }
}