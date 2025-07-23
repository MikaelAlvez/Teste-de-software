package jumpingthings.tests.views;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jumpingthings.main.views.MeuFrame; // Importe sua classe

class MeuFrameTest {

  private FrameFixture window;

  @BeforeEach
  public void setUp() {
    // Cria a janela dentro da thread correta do Swing (EDT)
    final var frame = GuiActionRunner.execute(MeuFrame::new);

    // O FrameFixture é o "controle remoto" para interagir com sua janela
    window = new FrameFixture(frame);
    window.show(); // Garante que a janela seja exibida
  }

  @Test
  void deveExibirMensagemCorretaAoClicar() {
    // Agora, os componentes serão encontrados sem problemas
    window.textBox("campoNome").enterText("AssertJ");
    window.button("botaoEnviar").click();
    window.label("labelMensagem").requireText("Olá, AssertJ!");
  }

  @AfterEach
  public void tearDown() {
    // Limpa a janela após cada teste
    window.cleanUp();
  }
}