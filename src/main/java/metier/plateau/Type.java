package metier.plateau;

public enum Type {
    SOLEIL(" \u2600 "), 
    LUNE(" \uD83C\uDF19 "), 
    SIMPLE("simple");

    private String type;

    private Type(String type) {
        this.type=type;
    }

    public String afficher() {
        return type;
    }

}