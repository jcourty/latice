package metier.plateau;

import java.util.Objects;

import metier.tuile.Symbole;

public class Case {

	private final Coordonnee coordonnee;
	private final Symbole symbole;

	public Case(Coordonnee coordonnee, Symbole symbole) {
		this.coordonnee = coordonnee;
		this.symbole = symbole;
	}

	public Case(Coordonnee coordonne) {
		this(coordonne, null);
	}

	public Coordonnee coordonnee() {
		return coordonnee;
	}

	public Symbole symbole() {
		return symbole;
	}

	@Override
	public int hashCode() {
		return Objects.hash(coordonnee, symbole);
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
		return Objects.equals(coordonnee, other.coordonnee) && symbole == other.symbole;
	}

	public boolean coordonneEgal(Case uneCase) {
		return coordonnee.equals(uneCase.coordonnee);
	}

}