package vue.fxPaquet.metier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import metier.plateau.Case;
import metier.plateau.PlateauDeJeu;
import metier.tuile.Tuile;

public class AffichagePlateau {
	private ObservableMap<Case, Tuile> plateau; 

	public void setPlateau(ObservableMap<Case, Tuile> plateau) {
		this.plateau = plateau;
	}

	public AffichagePlateau(PlateauDeJeu plateau) {
		this.plateau = FXCollections.observableMap(plateau.plateau());
	}
	
	public AffichagePlateau() {
		this.plateau = FXCollections.observableHashMap();
	}
	
	public String afficher2() {
	    StringBuilder sb = new StringBuilder("{");
	    boolean first = true;

	    for (Map.Entry<Case, Tuile> entry : plateau.entrySet()) {
	        if (!first) {
	            sb.append(", ");
	        }
	        // Appelle la méthode afficher() sur les clés et les valeurs
	        sb.append(entry.getKey().afficher()).append("=").append(entry.getValue() != null ? entry.getValue().afficher() : "null");
	        first = false;
	    }

	    sb.append("}");
	    return sb.toString();
	}
	
	public String afficher() {
	    StringBuilder sb = new StringBuilder("{");
	    boolean debut = true;

	    List<Map.Entry<Case, Tuile>> entries = new ArrayList<>(plateau.entrySet());
	    entries.sort(Comparator.comparingInt(entry -> entry.getKey().coordonnee().x() * 10 + entry.getKey().coordonnee().y()));
	    for (Map.Entry<Case, Tuile> entry : entries) {
	        if (!debut) {
	            sb.append(", ");
	        }
	        sb.append(entry.getKey().afficher()).append("=")
	          .append(entry.getValue() != null ? entry.getValue().afficher() : "null");
	        debut = false;
	    }
	    
	    sb.append("}");
	    return sb.toString();
	}

	public ObservableMap<Case, Tuile> plateau() {
		return plateau;
	}

}
