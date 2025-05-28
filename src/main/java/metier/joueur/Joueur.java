package metier.joueur;

import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import metier.tuile.Tuile;

public class Joueur {

	private final String pseudo;
	private Chevalet chevalet;
	private TasDeTuile main;
	private GridPane idGridPane;
	
	public Joueur(String pseudo,Chevalet chevalet, TasDeTuile main, GridPane idGridPane) {
		this.pseudo = pseudo;
		this.chevalet = chevalet ;
		this.main = main;
		this.idGridPane = idGridPane ;
	}
	
	public Joueur(String pseudo,GridPane idGridPane) {
		this(pseudo,new Chevalet(),new TasDeTuile(),idGridPane);
	}
	
	public Joueur(String pseudo) {
		this(pseudo,new Chevalet(),new TasDeTuile(),null);
	}
	
	public String pseudo() {
		return pseudo;
	}

	public TasDeTuile main() {
		return main;
	}
	
	public Chevalet chevalet() {
		return chevalet ;
	}
	
	public GridPane idGridPane() {
		return idGridPane ;
	}
	
	public List<Tuile> listeChevalet() {
		return chevalet.listeTuiles();
	}
	
	public void ajouterDansChevalet(Tuile tuile) {
		chevalet.ajouterTuile(tuile);	
	}
	
	public Tuile piocherDansChevalet(int rang) {
		return chevalet.piocherTuile(rang) ;
	}
	
	public Tuile piocherDansMain() {
		return main.piocherTuile();
	}
	
	public void ajouterDansMain(Tuile tuile) {
		main.ajouterTuile(tuile);
	}
	
	public void afficherMain() {
		main.afficherTuiles();
	}
	
	public void afficherChevalet() {
		chevalet.afficherTuiles();
	}
	
	public int tailleMain() {
		return main.taillePioche();
	}
	
	public int tailleChevalet() {
		return chevalet.taillePioche();
	}
	
	public void distribuerDansChevalet() {
		for (int i = 0; i < 5; i++) {
			Tuile tuile = piocherDansMain();
			ajouterDansChevalet(tuile);
		}
	}
	
	public void viderChevalet() {
		while(!chevalet.estVide()) {
			Tuile tuile = chevalet.piocherTuile();
			main.ajouterTuile(tuile);
		}
	}
	
	public void remplirChevalet() {
		while (chevalet.taillePioche() < 5 && !main.estVide()) {
			Tuile tuile = main.piocherTuile() ;
			chevalet.ajouterTuile(tuile);
		}
	}
	
	public void ajouterDansGridPane(ImageView imageView, int x , int y) {
		idGridPane().add(imageView, x, y);
	}

	
	
}
