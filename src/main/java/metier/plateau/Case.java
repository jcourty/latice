package metier.plateau;

import java.util.Objects;

public class Case {

	private final Coordonnee coordonnee;
	private final Type type;

	public Case(Coordonnee coordonnee, Type type) {
		this.coordonnee = coordonnee;
		this.type = type;
	}

	public Case(Coordonnee coordonnee) {
		this(coordonnee, null);
	}

	public Coordonnee coordonnee() {
		return coordonnee;
	}

	public Type type() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(coordonnee);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Case other = (Case) obj;
		return Objects.equals(coordonnee, other.coordonnee);
	}

	public boolean coordonneEgal(Case uneCase) {
		return coordonnee.equals(uneCase.coordonnee);
	}

	public int coordonneeX() {
		return coordonnee.x();
	}
	public int coordonneeY() {
		return coordonnee.y();
	}

}