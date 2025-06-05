package metier.arbitre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import exception.SaisieInvalideException;
import metier.plateau.Case;
import metier.plateau.Coordonnee;
import metier.plateau.Type;
import metier.tuile.Couleur;
import metier.tuile.Symbole;
import metier.tuile.Tuile;

class ArbitreInteractionUtilisateurTest extends ArbitreBaseTest {
	
    @Test
    void test_choisirCoordonnee_plateauNonVide() {
        Case uneCase = new Case(new Coordonnee(5, 5), Type.SIMPLE);
        Tuile tuile = new Tuile(Symbole.GECKO, Couleur.ROUGE);
        plateau.poserTuile(uneCase, tuile);
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("4\n5\n");
        Coordonnee coordonnee = arbitre.choisirCoordonnee(plateau);
        assertEquals(4, coordonnee.x());
        assertEquals(5, coordonnee.y());
    }
    
    @Test
    void test_choisirCoordonnee_plateauVide() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("\n");
        Coordonnee coordonnee = arbitre.choisirCoordonnee(plateau);
        assertEquals(5, coordonnee.x());
        assertEquals(5, coordonnee.y());
    }
    
    @Test
    void test_choisirCoordonnee_entree_non_numerique_x_lance_exception() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("abc\n5\n");
        plateau.poserTuile(plateau.caseSur(new Coordonnee(5, 5)), new Tuile(Symbole.TORTUE, Couleur.BLEU));
        SaisieInvalideException thrown = assertThrows(SaisieInvalideException.class, () -> {
            arbitre.choisirCoordonnee(plateau);
        });
        assertEquals("La ligne saisie n'est pas un nombre entier.", thrown.getMessage());
    }
    
    @Test
    void test_choisirCoordonnee_entree_non_numerique_y_lance_exception() {
        transformeCeQuiEstEnParametreEnEntreeCommeAuClavier("5\ndef\n");
        plateau.poserTuile(plateau.caseSur(new Coordonnee(5, 5)), new Tuile(Symbole.TORTUE, Couleur.BLEU));
        SaisieInvalideException thrown = assertThrows(SaisieInvalideException.class, () -> {
            arbitre.choisirCoordonnee(plateau);
        });
        assertEquals("La colonne saisie n'est pas un nombre entier.", thrown.getMessage());
    }
}