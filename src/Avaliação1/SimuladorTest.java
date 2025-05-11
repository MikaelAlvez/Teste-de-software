package Avaliação1;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SimuladorTest {

    @Test
    public void testCriacaoSimulador() {
        Simulador sim = new Simulador(5);
        assertEquals(5, sim.getCriaturas().size());
    }

    @Test
    public void testIteracaoMantemTotalOuro() {
        Simulador sim = new Simulador(2);
        double totalAntes = sim.getCriaturas().stream().mapToDouble(Criatura::getGi).sum();
        sim.iterar();
        double totalDepois = sim.getCriaturas().stream().mapToDouble(Criatura::getGi).sum();
        assertEquals(totalAntes, totalDepois, 1e-5);
    }

    @Test
    public void testRouboExecutado() {
        Simulador sim = new Simulador(2);
        Criatura c0 = sim.getCriaturas().get(0);
        Criatura c1 = sim.getCriaturas().get(1);
        c0.addOuro(1_000_000);
        c1.removeOuro(500_000); // c1 = 500k, c0 = 2M
        sim.iterar();
        assertTrue(c0.getGi() != 2_000_000); // houve troca
    }
}

