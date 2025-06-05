package metier.arbitre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metier.joueur.Joueur;
import metier.joueur.TasDeTuile;
import metier.tuile.Tuile;

class ArbitreChoixChevaletTest extends ArbitreBaseTest {
	
    private Joueur joueur;
    private List<Joueur> joueurs;
    private TasDeTuile pioche;
    
    @BeforeEach
    void initialiserChoixChevalet() {
        super.initialiser();
        joueur = new Joueur("Andromaque");
        joueurs = new ArrayList<>();
        joueurs.add(joueur);
        pioche = new TasDeTuile();
        pioche.creerTasDeTuile();
        arbitre.distribuerTuile(pioche, joueurs);
        arbitre.distribuerDansChevalet(joueurs);
    }
    
    @Test
    void test_recherche_tuile_correct() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("2\n");
        List<Tuile> tuiles = joueur.listeChevalet();
        Tuile expected = tuiles.get(1);
        Tuile actual = arbitre.choixChevalet(joueur);
        assertEquals(expected, actual);
    }
    
    @Test
    void test_changement_de_chevalet() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("5\n2\n");
        List<Tuile> ancienChevalet = new ArrayList<>(joueur.listeChevalet());
        arbitre.choixChevalet(joueur);
        List<Tuile> nouveauChevalet = joueur.listeChevalet();
        assertFalse(ancienChevalet.equals(nouveauChevalet));
    }
    
    @Test
    void test_entree_index_invalide_puis_valide() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("9\n1\n");
        List<Tuile> tuiles = joueur.listeChevalet();
        Tuile attendue = tuiles.get(0);
        Tuile actuelle = arbitre.choixChevalet(joueur);
        assertEquals(attendue, actuelle);
    }
    
    @Test
    void test_choixChevalet_index_negatif_puis_valide() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("-1\n4\n");
        List<Tuile> tuiles = joueur.listeChevalet();
        Tuile attendue = tuiles.get(3);
        Tuile actuelle = arbitre.choixChevalet(joueur);
        assertEquals(attendue, actuelle);
    }
}
