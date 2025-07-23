package jumpingthings.main.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MeuFrame extends JFrame {

  public MeuFrame() {
    super("Exemplo");
    setName("frame0"); // Para debug e referência no log

    JTextField campoNome = new JTextField(20);
    campoNome.setName("campoNome");

    JButton botaoEnviar = new JButton("Enviar");
    botaoEnviar.setName("botaoEnviar");

    JLabel labelMensagem = new JLabel("Digite seu nome");
    labelMensagem.setName("labelMensagem");

    botaoEnviar.addActionListener((ActionEvent e) -> {
      String nome = campoNome.getText();
      labelMensagem.setText("Olá, " + nome + "!");
    });

    setLayout(new FlowLayout());
    add(campoNome);
    add(botaoEnviar);
    add(labelMensagem);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();

    this.setVisible(true);
  }
}
