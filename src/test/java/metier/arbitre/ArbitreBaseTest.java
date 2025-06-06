package metier.arbitre;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import metier.joueur.Joueur;
import metier.plateau.PlateauDeJeu;

public abstract class ArbitreBaseTest {
	
    protected Joueur joueur1;
    protected Joueur joueur2;
    protected static Arbitre arbitre;
    protected PlateauDeJeu plateau;
    protected ByteArrayInputStream donneesEntree;
    protected final ByteArrayOutputStream donneesSorties = new ByteArrayOutputStream();
    
    @BeforeEach
    void initialiser() {
        plateau = new PlateauDeJeu();
        joueur1 = new Joueur("Joueur1");
        joueur2 = new Joueur("Joueur2");
        arbitre = new Arbitre();
        System.setOut(new PrintStream(donneesSorties));
    }
    
    @AfterEach
    void remmetSortieSurLaSortieDeBase() {
        System.setIn(System.in);
        System.setOut(System.out);
    }
    
    protected void transformeCeQuiEstEnParametreEnEntreeCommeAuClavier(String donnee) {
        donneesEntree = new ByteArrayInputStream(donnee.getBytes());
        System.setIn(donneesEntree);
        arbitre = new Arbitre();
    }

}