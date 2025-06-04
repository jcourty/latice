package exception;

@SuppressWarnings("serial")
public class SaisieInvalideException extends RuntimeException {

    public SaisieInvalideException() {
        super("La saisie est invalide. Veuillez entrer un format attendu.");
    }

    public SaisieInvalideException(String message) {
        super(message);
    }

    public SaisieInvalideException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaisieInvalideException(Throwable cause) {
        super(cause);
    }
}
