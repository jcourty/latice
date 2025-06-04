package vue;

public class Console {

	public static void message(String texte) {
		System.out.println(texte);
	}

	public static void nombre(int nbr) {
		System.out.println(nbr);
	}

	public static void sautLigne() {
		message("");
	}

	public static void ligne(String texte) {
		System.out.print(texte);
	}

	public static final String SEPARATOR_LINE = "---------------------------------------------------";

	public static void titre(String texte) {
		message(SEPARATOR_LINE);
		message(texte);
		message(SEPARATOR_LINE);
	}

	public static void separateur() {
		message(SEPARATOR_LINE);
	}

	public static void effacerConsole() {
		for (int i = 0; i < 50; ++i) System.out.println();
	}
}