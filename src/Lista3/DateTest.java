package Lista3;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class DateTest {

    // Casos válidos
    @Test
    void deveCriarDataValida() {
        Date date = new Date(1, 31, 2024);
        assertThat(date.toString()).isEqualTo("1/31/2024");
    }

    @Test
    void deveAceitarAnoBissextoEmFevereiro() {
        Date date = new Date(2, 29, 2020); // 2020 é bissexto
        assertThat(date.toString()).isEqualTo("2/29/2020");
    }

    @Test
    void deveAceitarMesCom30Dias() {
        Date date = new Date(4, 30, 2023); // Abril tem 30 dias
        assertThat(date.toString()).isEqualTo("4/30/2023");
    }

    @Test
    void deveAceitarMesCom31Dias() {
        Date date = new Date(7, 31, 2023); // Julho tem 31 dias
        assertThat(date.toString()).isEqualTo("7/31/2023");
    }

    // Meses inválidos
    @Test
    void deveLancarExcecaoParaMesZero() {
        assertThatThrownBy(() -> new Date(0, 15, 2022))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("month (0) must be 1-12");
    }

    @Test
    void deveLancarExcecaoParaMesMaiorQue12() {
        assertThatThrownBy(() -> new Date(13, 1, 2022))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("month (13) must be 1-12");
    }

    // Dias inválidos por mês
    @Test
    void deveLancarExcecaoParaDiaMaiorQue31() {
        assertThatThrownBy(() -> new Date(1, 32, 2022))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("day (32) out-of-range");
    }

    @Test
    void deveLancarExcecaoParaDiaZero() {
        assertThatThrownBy(() -> new Date(5, 0, 2022))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("day (0) out-of-range");
    }

    @Test
    void deveLancarExcecaoParaAbrilCom31Dias() {
        assertThatThrownBy(() -> new Date(4, 31, 2022)) // Abril tem 30
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("day (31) out-of-range");
    }

    // Casos com fevereiro
    @Test
    void deveLancarExcecaoParaFevereiro29AnoNaoBissexto() {
        assertThatThrownBy(() -> new Date(2, 29, 2023)) // 2023 não é bissexto
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("day (29) out-of-range");
    }

    @Test
    void deveAceitarAnoBissextoDivisivelPor400() {
        Date date = new Date(2, 29, 2000); // 2000 é bissexto
        assertThat(date.toString()).isEqualTo("2/29/2000");
    }

    @Test
    void deveLancarExcecaoParaAnoSecularNaoBissexto() {
        assertThatThrownBy(() -> new Date(2, 29, 1900)) // 1900 não é bissexto
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("day (29) out-of-range");
    }

    // toString básico
    @Test
    void toStringDeveRetornarFormatoCorreto() {
        Date date = new Date(12, 25, 2025);
        assertThat(date.toString()).isEqualTo("12/25/2025");
    }
}
