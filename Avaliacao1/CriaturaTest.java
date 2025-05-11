package Avaliação1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CriaturaTest {

    @Test
    public void testInicializacao() {
        Criatura c = new Criatura(1);
        assertEquals(1_000_000, c.getGi(), 1e-5);
        assertEquals(0.0, c.getXi(), 1e-5);
    }

    @Test
    public void testAtualizarPosicao() {
        Criatura c = new Criatura(1);
        c.atualizarPosicao();
        // r ∈ [-1, 1], xi ∈ [-1_000_000, +1_000_000]
        assertTrue(Math.abs(c.getXi()) <= 1_000_000);
    }

    @Test
    public void testAdicionarRemoverOuro() {
        Criatura c = new Criatura(1);
        c.addOuro(500_000);
        assertEquals(1_500_000, c.getGi(), 1e-5);
        c.removeOuro(200_000);
        assertEquals(1_300_000, c.getGi(), 1e-5);
    }
}

