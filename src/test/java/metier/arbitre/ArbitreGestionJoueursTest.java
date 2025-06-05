package metier.arbitre;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import metier.joueur.Joueur;

class ArbitreGestionJoueursTest extends ArbitreBaseTest {
	
    @Test
    void test_nombre_joueur_entree_valide() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("3\n");
        int result = arbitre.nombreJoueur();
        assertEquals(3, result);
    }
    
    @Test
    void test_nombre_joueur_entree_vide() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier(" \n2\n");
        int result = arbitre.nombreJoueur();
        assertEquals(2, result);
    }
    
    @Test
    void test_nombre_joueur_trop_petit() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("1\n2\n");
        int result = arbitre.nombreJoueur();
        assertEquals(2, result);
    }
    
    @Test
    void test_nombre_joueur_trop_grand() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("5\n2\n");
        int result = arbitre.nombreJoueur();
        assertEquals(2, result);
    }
    
    @Test
    void test_creation_liste_joueur() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("Karim\nMohamed\nDidier\n");
        List<Joueur> joueurs = arbitre.creationListeJoueur(3);
        assertEquals(3, joueurs.size());
        assertEquals("Karim", joueurs.get(0).pseudo());
        assertEquals("Mohamed", joueurs.get(1).pseudo());
        assertEquals("Didier", joueurs.get(2).pseudo());
    }
    
    @Test
    void test_creation_liste_joueur_pseudo_vide_puis_valide() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("\nPseudoValide\n");
        List<Joueur> joueurs = arbitre.creationListeJoueur(1);
        assertEquals(1, joueurs.size());
        assertEquals("PseudoValide", joueurs.get(0).pseudo());
    }
}