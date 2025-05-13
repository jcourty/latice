package metier.tuile;

public enum Symbole {
	PLUME("\ud83e\udeb6"),
	OISEAU("\ud83d\udc26"), 
	TORTUE("\ud83d\udc22"), 
	FLEUR("\ud83c\udf39"), 
	GECKO("\ud83e\udd8e"), 
	DAUPHIN("\ud83d\udc2c");
	
	private String symbole;
	
	private Symbole(String symbole) {
		this.symbole=symbole;
	}
	
	public String symbole() {
		return this.symbole;
	}
	
}