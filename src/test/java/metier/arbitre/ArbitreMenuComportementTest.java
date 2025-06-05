package metier.arbitre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ArbitreMenuComportementTest extends ArbitreBaseTest {
	
    @Test
    void test_menu_choix_invalide_puis_valide() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("0\n4\n");
        arbitre.menu(plateau, joueur1, 0);
        String output = donneesSorties.toString();
        assertTrue(output.contains("Choix invalide."));
    }
    
    @Test
    void test_menu_acheter_action_assez_de_points() {
        joueur1.setScore(5);
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("3\n4\n");
        arbitre.menu(plateau, joueur1, 0);
        assertEquals(3, joueur1.score());
    }
    
    @Test
    void test_menu_acheter_action_pas_assez_de_points() {
        joueur1.setScore(1);
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("3\n4\n");
        arbitre.menu(plateau, joueur1, 0);
        assertEquals(1, joueur1.score());
        String output = donneesSorties.toString();
        assertTrue(output.contains("Pas assez de points pour acheter une action."));
    }
    
    @Test
    void test_lancementDePartie_gestion_exception_nb_joueur() {
        StringBuilder inputBuilder = new StringBuilder();
        inputBuilder.append("abc\n");
        inputBuilder.append("2\n");
        inputBuilder.append("JoueurA\n");
        inputBuilder.append("JoueurB\n");
        for (int i = 0; i < 10; i++) {
            inputBuilder.append("4\n");
            inputBuilder.append("4\n");
        }
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier(inputBuilder.toString());
        arbitre.lancementDePartie();
        String output = donneesSorties.toString();
        assertTrue(output.contains("Le nombre de joueurs doit être un nombre entier."));
        assertTrue(output.contains("Le joueur gagnant est") || output.contains("Les gagnants sont :"));
    }
}