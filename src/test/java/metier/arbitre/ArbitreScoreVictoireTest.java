package metier.arbitre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metier.joueur.Joueur;
import metier.plateau.Case;
import metier.plateau.Coordonnee;
import metier.plateau.PlateauDeJeu;
import metier.tuile.Couleur;
import metier.tuile.Symbole;
import metier.tuile.Tuile;

class ArbitreScoreVictoireTest extends ArbitreBaseTest {
	
    private PlateauDeJeu plateauScore;
    private Tuile tuileReference;
    private Case centre;
    private Joueur joueurTest;
    
    @BeforeEach
    void initialiserScore() {
        super.initialiser();
        plateauScore = new PlateauDeJeu();
        tuileReference = new Tuile(Symbole.DAUPHIN, Couleur.BLEU);
        centre = plateauScore.caseSur(new Coordonnee(5, 5));
        joueurTest = new Joueur("Argan");
        joueurTest.setScore(0);
    }
    
    @Test
    void gagne_0_point_sans_adjacence() {
        plateauScore.poserTuile(centre, tuileReference);
        Arbitre.calculeScore(joueurTest, centre);
        assertEquals(0, joueurTest.score());
    }
    
    @Test
    void gagne_0_point_quand_pas_de_latice() {
        plateauScore.poserTuile(centre, tuileReference);
        Case uneCase = plateauScore.caseSur(new Coordonnee(5, 6));
        plateauScore.poserTuile(uneCase, new Tuile(Symbole.DAUPHIN, Couleur.ROUGE));
        Arbitre.calculeScore(joueurTest, uneCase);
        assertEquals(0, joueurTest.score());
    }
    
    @Test
    void test_joueurGagnant_un_gagnant() {
        joueur1.incrementerNbTuilePose();
        joueur1.incrementerNbTuilePose();
        joueur2.incrementerNbTuilePose();
        List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);
        List<Joueur> gagnants = Arbitre.joueurGagnant(joueurs);
        assertEquals(1, gagnants.size());
        assertEquals(joueur1, gagnants.get(0));
    }
    
    @Test
    void test_joueurGagnant_egalite_parfaite() {
        joueur1.incrementerNbTuilePose();
        joueur2.incrementerNbTuilePose();
        List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);
        List<Joueur> gagnants = Arbitre.joueurGagnant(joueurs);
        assertEquals(2, gagnants.size());
        assertTrue(gagnants.contains(joueur1));
        assertTrue(gagnants.contains(joueur2));
    }
}