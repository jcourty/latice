package metier.statistique;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import metier.joueur.Joueur;
import metier.plateau.Case;
import metier.plateau.PlateauDeJeu;
import metier.tuile.Tuile;

public class StatistiqueJeu {

    private PlateauDeJeu plateau;
    private List<Joueur> joueurs;
    private int indexJoueurActuel = 0;
    private int nbTour = 1;
    private int actionsEffectuees = 0;
    private int actionsMaxParTour = 1;
    private StringProperty actionTexteProperty = new SimpleStringProperty("Nombre d'actions : 0/1");

    public StatistiqueJeu(PlateauDeJeu plateau, List<Joueur> joueurs) {
        this.plateau = plateau;
        this.joueurs = joueurs;
    }

    public PlateauDeJeu plateau() {
        return plateau;
    }
    
    public Set<Entry<Case, Tuile>> plateauEntrySet() {
        return plateau.plateauEntrySet();
    }

    public List<Joueur> joueurs() {
        return joueurs;
    }

    public Joueur joueurActuel() {
        return joueurs.get(indexJoueurActuel);
    }

    public int indexJoueurActuel() {
        return indexJoueurActuel;
    }

    public void setIndexJoueurActuel(int index) {
        this.indexJoueurActuel = index;
    }

    public int nbTour() {
        return nbTour;
    }
    
    public void augmentationNombreTour() {
    	nbTour++;
    }

    public int actionsEffectuees() {
        return actionsEffectuees;
    }

    public void augmentationActionsEffectuees() {
        actionsEffectuees++;
    }

    public void resetActionsEffectuees() {
        actionsEffectuees = 0;
    }

    public int actionsMaxParTour() {
        return actionsMaxParTour;
    }
    
    public void augmentationMaxParTour() {
    	actionsMaxParTour++;
    }

    public void setActionTexte(String texte) {
        actionTexteProperty.set(texte);
    }

    public void joueurSuivant() {
        indexJoueurActuel = (indexJoueurActuel + 1) % joueurs.size();
        resetActionsEffectuees();
    }

    public void melangerListeJoueurs() {
    	Collections.shuffle(joueurs);
    }
    
    
    public boolean peutJouer() {
		return actionsEffectuees < actionsMaxParTour;
	}
    
    public void reenitialiserActions() {
		actionsMaxParTour = 1;
		actionsEffectuees = 0;
	}
    
    public String majLblAction() {
		return "Nombre d'actions : " + actionsEffectuees + "/" + actionsMaxParTour;
	}
    
    public StringProperty actionTexteProperty() {
		return actionTexteProperty;
	}
    
    public void majLabelActionAutomatique() {
		actionTexteProperty.set("Nombre d'actions : " + actionsEffectuees + "/" + actionsMaxParTour);
	}
    
}
