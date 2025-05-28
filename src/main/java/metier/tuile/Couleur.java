package metier.tuile;

public enum Couleur {
	JAUNE("\u001B[33m"), 
	BLEU( "\u001B[34m"), 
	MAGENTA("\u001B[35m"), 
	ROUGE("\u001B[31m"), 
	VERT("\u001B[32m"), 
	CYAN("\u001B[36m");
	
	private String codeCouleur;
	
	private Couleur(String couleur) {
		this.codeCouleur=couleur;
	}
	
	public String colorier(String text) {
		return codeCouleur+text+"\u001B[0m";
	}
	
	public String codeCouleur() {
		return codeCouleur;
	}
	
}