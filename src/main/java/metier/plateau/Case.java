package metier.plateau;

import java.util.Objects;

public class Case {

	private final Coordonnee coordonnee;
	private final Type type;

	public Case(Coordonnee coordonnee, Type type) {
		this.coordonnee = coordonnee;
		this.type = type;
	}

	public Case(Coordonnee coordonne) {
		this(coordonne, null);
	}

	public Coordonnee coordonnee() {
		return coordonnee;
	}

	public Type type() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(coordonnee, type);
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
		return Objects.equals(coordonnee, other.coordonnee) && type == other.type;
	}

	public boolean coordonneEgal(Case uneCase) {
		return coordonnee.equals(uneCase.coordonnee);
	}

}