package metier.arbitre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.tuile.Couleur;
import metier.tuile.Symbole;
import metier.tuile.Tuile;

class ArbitreDistributionTuilesTest extends ArbitreBaseTest {
	
    @Test
    void test_distribuer_tuile() {
        List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);
        TasDeTuile pioche = new TasDeTuile();
        pioche.creerTasDeTuile();
        arbitre.distribuerTuile(pioche, joueurs);
        for (Joueur joueur : joueurs) {
            assertTrue(joueur.tailleMain() > 0);
        }
        assertTrue(pioche.estVide());
    }
    
    @Test
    void test_distribuer_tuile_pioche_vide() {
        List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);
        TasDeTuile piocheVide = new TasDeTuile();
        arbitre.distribuerTuile(piocheVide, joueurs);
        assertEquals(0, joueur1.tailleMain());
        assertEquals(0, joueur2.tailleMain());
        assertTrue(piocheVide.estVide());
    }
    
    @Test
    void test_distribuer_dans_chevalet() {
        List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);
        TasDeTuile pioche = new TasDeTuile();
        pioche.creerTasDeTuile();
        arbitre.distribuerTuile(pioche, joueurs);
        arbitre.distribuerDansChevalet(joueurs);
        for (Joueur joueur : joueurs) {
            assertEquals(5, joueur.tailleChevalet());
        }
        for (Joueur joueur : joueurs) {
            assertTrue(joueur.tailleMain() < 36);
        }
    }
    
    @Test
    void test_distribuer_dans_chevalet_main_insuffisante() {
        List<Joueur> joueurs = Arrays.asList(joueur1);
        joueur1.ajouterDansMain(new Tuile(Symbole.TORTUE, Couleur.BLEU));
        joueur1.ajouterDansMain(new Tuile(Symbole.DAUPHIN, Couleur.ROUGE));
        joueur1.ajouterDansMain(new Tuile(Symbole.FLEUR, Couleur.VERT));
        arbitre.distribuerDansChevalet(joueurs);
        assertEquals(3, joueur1.tailleChevalet());
    }
    
    @Test
    void test_afficherChevaletJoueur() {
        joueur1.ajouterDansMain(new Tuile(Symbole.GECKO, Couleur.BLEU));
        joueur1.remplirChevalet();
        arbitre.afficherChevaletJoueur(joueur1);
        assertFalse(donneesSorties.toString().isEmpty());
    }
    
    @Test
    void test_echangerChevalet_vide_puis_rempli() {
        joueur1.viderChevalet();
        assertEquals(0, joueur1.tailleChevalet());
        for (int i = 0; i < 5; i++) {
            joueur1.ajouterDansMain(new Tuile(Symbole.TORTUE, Couleur.ROUGE));
        }
        arbitre.echangerChevalet(joueur1);
        assertEquals(5, joueur1.tailleChevalet());
        assertEquals(0, joueur1.tailleMain());
    }
}