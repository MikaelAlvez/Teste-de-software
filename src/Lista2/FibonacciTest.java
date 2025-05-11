package Lista2;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class FibonacciTest {

    @Test
    void deveLancarExcecaoParaValorNegativo() {
        assertThat(Fibonacci.fibo(-1)).isEqualTo(-1);
    }

    @Test
    void deveRetornarZeroParaEntradaZero() {
        assertThat(Fibonacci.fibo(0)).isEqualTo(0);
    }

    @Test
    void deveRetornarUmParaEntradaUm() {
        assertThat(Fibonacci.fibo(1)).isEqualTo(1);
    }

    @Test
    void deveRetornarUmParaEntradaDois() {
        assertThat(Fibonacci.fibo(2)).isEqualTo(1);
    }

    @Test
    void deveRetornarValoresCorretosParaEntradasMenoresQue46() {
        assertThat(Fibonacci.fibo(5)).isEqualTo(5);
        assertThat(Fibonacci.fibo(10)).isEqualTo(55);
        assertThat(Fibonacci.fibo(20)).isEqualTo(6765);
        assertThat(Fibonacci.fibo(30)).isEqualTo(832040);
        assertThat(Fibonacci.fibo(45)).isEqualTo(1134903170);
    }
}

