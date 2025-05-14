package metier.plateau;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metier.tuile.Couleur;
import metier.tuile.Symbole;
import metier.tuile.Tuile;

class PlateauDeJeuTest {
    PlateauDeJeu plateau;

    @BeforeEach
    void initialiser() {
        plateau = new PlateauDeJeu();
    }

    @Test
    void plateau_initialement_vide() {
    	assertTrue(plateau.estVide());
    }

    @Test
    void poserTuile_sur_case_vide() {
        Case uneCase = plateau.caseSur(new Coordonnee(1, 1));
        Tuile tuile = new Tuile(Symbole.DAUPHIN, Couleur.BLEU);
        assertTrue(plateau.poserTuile(uneCase, tuile));
        assertEquals(tuile, plateau.tuileSur(uneCase));
    }

    @Test
    void ne_peut_pas_poser_tuile_sur_case_occupee() {
        Case uneCase = plateau.caseSur(new Coordonnee(1, 1));
        Tuile tuile1 = new Tuile(Symbole.GECKO, Couleur.VERT);
        Tuile tuile2 = new Tuile(Symbole.OISEAU, Couleur.CYAN);
        plateau.poserTuile(uneCase, tuile1);
        assertFalse(plateau.poserTuile(uneCase, tuile2));
        assertEquals(tuile1, plateau.tuileSur(uneCase));
    }

    @Test
    void retirerTuile_retourne_la_bonne_tuile_et_la_retire() {
        Case uneCase = plateau.caseSur(new Coordonnee(2, 2));
        Tuile tuile = new Tuile(Symbole.FLEUR, Couleur.MAGENTA);
        plateau.poserTuile(uneCase, tuile);
        assertEquals(tuile, plateau.retirerTuileSur(uneCase));
        assertNull(plateau.tuileSur(uneCase));
    }

    @Test
    void nombre_tuiles_sur_plateau_apres_ajout_et_retrait() {
        Case case1 = plateau.caseSur(new Coordonnee(1, 1));
        Case case2 = plateau.caseSur(new Coordonnee(3, 3));
        Tuile tuile1 = new Tuile(Symbole.PLUME, Couleur.VERT);
        Tuile tuile2 = new Tuile(Symbole.TORTUE, Couleur.ROUGE);
        
        plateau.poserTuile(case1, tuile1);
        plateau.poserTuile(case2, tuile2);
        assertEquals(2, plateau.nombreTuileSurPlateau());

        plateau.retirerTuileSur(case1);
        assertEquals(1, plateau.nombreTuileSurPlateau());
    }

    @Test
    void vider_plateau_le_rend_vide() {
        Case case1 = plateau.caseSur(new Coordonnee(5, 5));
        plateau.poserTuile(case1, new Tuile(Symbole.DAUPHIN, Couleur.MAGENTA));
        assertFalse(plateau.estVide());

        plateau.vider();
        assertTrue(plateau.estVide());
    }
}

