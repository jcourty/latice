package vue;

public class Console {

	public static void message (String text) {
		System.out.println(text);
	}
	
	public static void nombre (int nbr) {
		System.out.println(nbr);
	}
	
	public static void sautLigne () {
		System.out.println("");
	}
	
	public static void ligne (String text) {
		System.out.print(text);
	}

	public static final String SEPARATOR_LINE = "---------------------------------------------------";

	public static void titre(String text) {
		message(SEPARATOR_LINE);
		message(text);
		message(SEPARATOR_LINE);
	}

}