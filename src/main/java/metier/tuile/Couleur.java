package metier.tuile;

public enum Couleur {
	JAUNE("\u001B[33m"), 
	BLEU( "\u001B[34m"), 
	MAGENTA("\u001B[35m"), 
	ROUGE("\u001B[31m"), 
	VERT("\u001B[32m"), 
	CYAN("\u001B[36m"),
	RESET("\u001B[0m");
	
	private String color;
	
	private Couleur(String color) {
		this.color=color;
	}
	
	public String colorier(String text) {
		return color+text+RESET.color;
	}
	
}