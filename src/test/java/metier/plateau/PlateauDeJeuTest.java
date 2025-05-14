package metier.plateau;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
        Case uneCase = plateau.caseSur(new Coordonnee(5, 5));
        Tuile tuile = new Tuile(Symbole.DAUPHIN, Couleur.BLEU);
        assertTrue(plateau.peutPoserTuile(uneCase, tuile));
        plateau.poserTuile(uneCase, tuile);
        assertEquals(tuile, plateau.tuileSur(uneCase));
    }

    @Test
    void ne_peut_pas_poser_tuile_sur_case_occupee() {
        Case uneCase = plateau.caseSur(new Coordonnee(5, 5));
        Tuile tuile1 = new Tuile(Symbole.GECKO, Couleur.VERT);
        Tuile tuile2 = new Tuile(Symbole.OISEAU, Couleur.CYAN);
        plateau.poserTuile(uneCase, tuile1);
        assertFalse(plateau.peutPoserTuile(uneCase, tuile2));
        assertEquals(tuile1, plateau.tuileSur(uneCase));
    }

    @Test
    void retirerTuile_retourne_la_bonne_tuile_et_la_retire() {
        Case uneCase = plateau.caseSur(new Coordonnee(5, 5));
        Tuile tuile = new Tuile(Symbole.FLEUR, Couleur.MAGENTA);
        plateau.poserTuile(uneCase, tuile);
        assertEquals(tuile, plateau.retirerTuileSur(uneCase));
        assertNull(plateau.tuileSur(uneCase));
    }

    @Test
    void nombre_tuiles_sur_plateau_apres_ajout_et_retrait() {
        Case case1 = plateau.caseSur(new Coordonnee(5, 5));
        Case case2 = plateau.caseSur(new Coordonnee(5, 6));
        Tuile tuile1 = new Tuile(Symbole.PLUME, Couleur.VERT);
        Tuile tuile2 = new Tuile(Symbole.PLUME, Couleur.ROUGE);

        plateau.poserTuile(case1, tuile1);
        plateau.poserTuile(case2, tuile2);
        assertEquals(2, plateau.nombreTuileSurPlateau());

        plateau.retirerTuileSur(case1);
        assertEquals(1, plateau.nombreTuileSurPlateau());
    }
    
    @Test
    void poser_tuile_similaire_sur_case() {
 		Case uneCase = plateau.caseSur(new Coordonnee(5, 5));
         Case uneAutreCase = plateau.caseSur(new Coordonnee(5,6));
         Tuile tuile = new Tuile(Symbole.TORTUE, Couleur.ROUGE);
         Tuile uneAutreTuile = new Tuile(Symbole.GECKO, Couleur.ROUGE);
         plateau.poserTuile(uneCase, tuile);
         plateau.poserTuile(uneAutreCase, uneAutreTuile);
         assertTrue(plateau.tuileAdjacenteSimilaire(uneCase,tuile));
         assertEquals(uneAutreTuile, plateau.retirerTuileSur(uneAutreCase));
    }
     
     @Test
    void tuile_differente_non_pose() {
     	Case uneCase = plateau.caseSur(new Coordonnee(2, 2));
         Tuile tuile = new Tuile(Symbole.TORTUE, Couleur.ROUGE);
         plateau.poserTuile(uneCase, tuile);
         Case uneAutreCase = plateau.caseSur(new Coordonnee(2, 3));
         Tuile uneAutreTuile = new Tuile(Symbole.PLUME, Couleur.CYAN);
         plateau.poserTuile(uneAutreCase, uneAutreTuile);
         assertNull(plateau.tuileSur(uneAutreCase));        
     }
    
    @Nested
    class test_double {

        Case case55, case56, case66,case65;
        Tuile tuileTortueVert, tuilePlumeVert, tuilePlumeRouge, tuileDauphinMagenta;

        @BeforeEach
        void setUp() {
        	case55 = plateau.caseSur(new Coordonnee(5, 5));
        	case56 = plateau.caseSur(new Coordonnee(5, 6));
        	case66 = plateau.caseSur(new Coordonnee(6, 6));
        	case65 = plateau.caseSur(new Coordonnee(6, 5));
        	tuileTortueVert = new Tuile(Symbole.TORTUE, Couleur.VERT);
            tuilePlumeVert = new Tuile(Symbole.PLUME, Couleur.VERT);
            tuilePlumeRouge = new Tuile(Symbole.OISEAU, Couleur.ROUGE);
            tuileDauphinMagenta = new Tuile(Symbole.DAUPHIN, Couleur.MAGENTA);

            plateau.poserTuile(case55, tuileTortueVert);
            plateau.poserTuile(case56, tuilePlumeVert);
            plateau.poserTuile(case66, tuilePlumeVert);

        }

        @Test
        void posable_si_trois_tuiles_similaires_sur_trois() {
            assertTrue(plateau.peutPoserTuile(case65, tuilePlumeVert));
        }

        @Test
        void non_posable_si_trois_tuiles_non_similaires() {
            assertFalse(plateau.peutPoserTuile(case65, tuileDauphinMagenta));
        }
    }
    
    @Nested
    class test_triple {

        Case case55, case65, case66, case56, case67, case57;
        Tuile tuileGeckoVert, tuileOiseauVert, tuileOiseauRouge, tuileDauphinVert;

        @BeforeEach
        void setUp() {
            case55 = plateau.caseSur(new Coordonnee(5, 5));
            case65 = plateau.caseSur(new Coordonnee(6, 5));
            case66 = plateau.caseSur(new Coordonnee(6, 6));
            case56 = plateau.caseSur(new Coordonnee(5, 6));
            case67 = plateau.caseSur(new Coordonnee(6, 7));
            case57 = plateau.caseSur(new Coordonnee(5, 7));

            tuileGeckoVert = new Tuile(Symbole.GECKO, Couleur.VERT);
            tuileOiseauVert = new Tuile(Symbole.OISEAU, Couleur.VERT);
            tuileOiseauRouge = new Tuile(Symbole.OISEAU, Couleur.ROUGE);
            tuileDauphinVert = new Tuile(Symbole.DAUPHIN, Couleur.VERT);

            plateau.poserTuile(case55, tuileGeckoVert);
            plateau.poserTuile(case65, tuileGeckoVert);
            plateau.poserTuile(case66, tuileOiseauVert);
            plateau.poserTuile(case67, tuileOiseauRouge);
            plateau.poserTuile(case57, tuileOiseauRouge);
        }

        @Test
        void posable_si_trois_tuiles_similaires_sur_trois() {
            assertTrue(plateau.peutPoserTuile(case56, tuileOiseauVert));
        }

        @Test
        void non_posable_si_trois_tuiles_non_similaires() {
            assertFalse(plateau.peutPoserTuile(case56, tuileOiseauRouge));
        }
    }
    @Nested
    class test_latice {

        Case case55, case46, case66, case56, case57;
        Tuile tuileGeckoVert, tuilePlumeVert, tuileDauphinMagenta, tuileDauphinRouge, tuileDauphinVert, tuileOiseauJaune;

        @BeforeEach
        void setUp() {
            case55 = plateau.caseSur(new Coordonnee(5, 5));
            case46 = plateau.caseSur(new Coordonnee(4, 6));
            case66 = plateau.caseSur(new Coordonnee(6, 6));
            case56 = plateau.caseSur(new Coordonnee(5, 6));
            case57 = plateau.caseSur(new Coordonnee(5, 7));

            tuileGeckoVert = new Tuile(Symbole.GECKO, Couleur.VERT);
            tuilePlumeVert = new Tuile(Symbole.PLUME, Couleur.VERT);
            tuileDauphinMagenta = new Tuile(Symbole.DAUPHIN, Couleur.MAGENTA);
            tuileDauphinVert = new Tuile(Symbole.DAUPHIN, Couleur.VERT);
            tuileDauphinRouge = new Tuile(Symbole.DAUPHIN, Couleur.ROUGE);
            tuileOiseauJaune= new Tuile(Symbole.OISEAU, Couleur.JAUNE);

            plateau.poserTuile(case55, tuileDauphinMagenta);
            plateau.poserTuile(case46, tuileDauphinRouge);
            plateau.poserTuile(case66, tuileGeckoVert);
            plateau.poserTuile(case57, tuilePlumeVert);
        }

        @Test
        void posable_si_trois_tuiles_similaires_sur_trois() {
            assertTrue(plateau.peutPoserTuile(case56, tuileDauphinVert));
        }

        @Test
        void non_posable_si_trois_tuiles_non_similaires() {
            assertFalse(plateau.peutPoserTuile(case56, tuileOiseauJaune));
        }
    }
}