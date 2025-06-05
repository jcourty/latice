package metier.plateau;

public enum Type {
    SOLEIL(" \u2600 "), 
    LUNE(" \uD83C\uDF19 "), 
    SIMPLE("simple");

    private String chaine;

    private Type(String type) {
        this.chaine=type;
    }

    public String afficher() {
        return chaine;
    }

}