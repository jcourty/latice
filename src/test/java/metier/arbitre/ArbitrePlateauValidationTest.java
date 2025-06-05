package metier.arbitre;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metier.plateau.Case;
import metier.plateau.Coordonnee;
import metier.plateau.PlateauDeJeu;
import metier.tuile.Couleur;
import metier.tuile.Symbole;
import metier.tuile.Tuile;

class ArbitrePlateauValidationTest extends ArbitreBaseTest {
	
    private PlateauDeJeu plateauTest; 
    private Tuile tuileReference;
    private Case centre;
    
    @BeforeEach
    void initialiserValidation() {
        super.initialiser();
        plateauTest = new PlateauDeJeu();
        tuileReference = new Tuile(Symbole.DAUPHIN, Couleur.BLEU);
        centre = plateauTest.caseSur(new Coordonnee(5, 5));
    }
    
    @Test
    void test_peutPoserTuile_quand_valide() {
        Case centre = plateauTest.caseSur(new Coordonnee(5, 5));
        Case adjacente = plateauTest.caseSur(new Coordonnee(5, 4));
        plateauTest.poserTuile(centre, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE));
        boolean resultat = Arbitre.peutPoserTuile(plateauTest, adjacente, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE));
        assertTrue(resultat);
    }
    
    @Test
    void test_peutPoserTuile_quand_plateau_vide() {
        Case centre = plateauTest.caseSur(new Coordonnee(5, 5));
        boolean resultat = Arbitre.peutPoserTuile(plateauTest, centre, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE));
        assertTrue(resultat);
    }
    
    @Test
    void test_ne_peut_pas_poser_quand_tuile_sur_case() {
        Case centre = plateauTest.caseSur(new Coordonnee(5, 5));
        plateauTest.poserTuile(centre, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE));
        boolean resultat = Arbitre.peutPoserTuile(plateauTest, centre, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE));
        assertFalse(resultat);
    }
    
    @Test
    void test_aucune_tuile_adjacente() {
        boolean resultat = Arbitre.tuileAdjacenteSimilaire(plateauTest, centre, tuileReference);
        assertFalse(resultat);
    }
    
    @Test
    void test_une_tuile_adjacente_similaire() {
        Case adjacente = plateauTest.caseSur(new Coordonnee(4, 5));
        Tuile nouvelleTuile = new Tuile(Symbole.DAUPHIN, Couleur.BLEU);
        plateauTest.poserTuile(centre, tuileReference);
        boolean resultat = Arbitre.tuileAdjacenteSimilaire(plateauTest, adjacente, nouvelleTuile);
        assertTrue(resultat);
    }
    
    @Test
    void test_une_tuile_adjacente_differente() {
        Case adjacente = plateauTest.caseSur(new Coordonnee(4, 5));
        plateauTest.poserTuile(adjacente, new Tuile(Symbole.FLEUR, Couleur.ROUGE));
        boolean resultat = Arbitre.tuileAdjacenteSimilaire(plateauTest, centre, tuileReference);
        assertFalse(resultat);
    }
    
    @Test
    void test_trois_adjacentes_similaires_et_une_vide() {
        plateauTest.poserTuile(centre, tuileReference);
        plateauTest.poserTuile(plateauTest.caseSur(new Coordonnee(4, 5)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU));
        plateauTest.poserTuile(plateauTest.caseSur(new Coordonnee(6, 5)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU));
        plateauTest.poserTuile(plateauTest.caseSur(new Coordonnee(5, 4)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU));
        boolean result = Arbitre.tuileAdjacenteSimilaire(plateauTest, centre, tuileReference);
        assertTrue(result);
    }
    
    @Test
    void test_toutes_adjacentes_differentes() {
        plateauTest.poserTuile(plateauTest.caseSur(new Coordonnee(4, 5)), new Tuile(Symbole.FLEUR, Couleur.ROUGE));
        plateauTest.poserTuile(plateauTest.caseSur(new Coordonnee(6, 5)), new Tuile(Symbole.TORTUE, Couleur.VERT));
        plateauTest.poserTuile(plateauTest.caseSur(new Coordonnee(5, 4)), new Tuile(Symbole.DAUPHIN, Couleur.JAUNE));
        plateauTest.poserTuile(plateauTest.caseSur(new Coordonnee(5, 6)), new Tuile(Symbole.FLEUR, Couleur.BLEU));
        boolean result = Arbitre.tuileAdjacenteSimilaire(plateauTest, centre, tuileReference);
        assertFalse(result);
    }
    
    @Test
    void test_tuiles_adjacentes_partiellement_similaires() {
        plateauTest.poserTuile(centre, tuileReference);
        plateauTest.poserTuile(plateauTest.caseSur(new Coordonnee(4, 5)), new Tuile(Symbole.DAUPHIN, Couleur.BLEU));
        plateauTest.poserTuile(plateauTest.caseSur(new Coordonnee(6, 5)), new Tuile(Symbole.FLEUR, Couleur.ROUGE));
        boolean result = Arbitre.tuileAdjacenteSimilaire(plateauTest, centre, tuileReference);
        assertFalse(result);
    }
}