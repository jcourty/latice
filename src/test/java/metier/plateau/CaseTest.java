package metier.plateau;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CaseTest {
	
	@Test
	void tuile_egale_a_une_autre() {
		Coordonnee coord = new Coordonnee(0, 0);
		Case case1 = new Case(coord);
		Case case2 = new Case(coord);
		assertTrue(case1.coordonneeEgal(case2));
	}
	
	@Test
	void tuile_differente_a_une_autre() {
		Coordonnee coord1 = new Coordonnee(0, 0);
		Coordonnee coord2 = new Coordonnee(1, 3);
		Case case1 = new Case(coord1);
		Case case2 = new Case(coord2);
		assertFalse(case1.coordonneeEgal(case2));
	}
	
	@Test
	void type_correct() {
		Coordonnee coord = new Coordonnee(0, 0);
		Case uneCase = new Case(coord, Type.SOLEIL);
		assertEquals(Type.SOLEIL, uneCase.type());
	}
	
}
