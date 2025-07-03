package jumpingthings.main.views;

import javax.swing.*;
import java.awt.*;

public class AboutView extends JPanel {

    public AboutView() {
        startUp();
    }

    private void startUp() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Sobre o Projeto - Simulação de Criaturas Saltitantes", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Serif", Font.PLAIN, 14));

        textArea.setText("""
                📌 Versão 1 - Simulação básica:
                
                Implementem, em Java, uma simulação de criaturas saltitantes conforme os seguintes requisitos:

                - A simulação compreende n criaturas, numeradas de 1 a n.
                - Cada criatura i (1 ≤ i ≤ n) possui uma quantidade de moedas gi, iniciando com um milhão.
                - Cada criatura também possui uma posição no horizonte, representada por um número de ponto flutuante xi.
                - Em cada iteração:
                    - A nova posição é calculada por: xi ← xi + rgi, onde r ∈ [−1, 1].
                    - A criatura rouba metade das moedas da criatura mais próxima de um dos lados.
                - A simulação deve permitir visualização gráfica das iterações para um número dado de criaturas.
                
                📌 Versão 2 - Clusters e Guardião do Horizonte:

                - Se duas ou mais criaturas colidem (mesma posição), elas formam um cluster, somando suas moedas.
                - O cluster age como uma criatura, podendo roubar metade das moedas da criatura mais próxima.
                - Um Guardião do Horizonte (índice n+1) é introduzido:
                    - Começa com 0 moedas.
                    - Possui posição xn+1 e se move com mesma fórmula xi ← xi + rgi.
                    - Se colidir com um cluster, o cluster é eliminado e suas moedas são absorvidas pelo guardião.
                - A simulação é considerada bem-sucedida se restar apenas:
                    - O guardião e uma criatura i, com gn+1 > gi, ou
                    - Apenas o guardião.

                📊 Funcionalidades Adicionais:

                - Inclusão e exclusão de usuários (com login, senha, avatar e pontuação).
                - Estatísticas da simulação disponíveis para todos:
                    - Pontuação de cada usuário.
                    - Simulações executadas por usuário.
                    - Total de simulações.
                    - Média de simulações bem-sucedidas por usuário.
                    - Média total de simulações bem-sucedidas.
                """);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        JButton backButton = new JButton("Voltar");
        backButton.addActionListener(e -> RouterView.getInstance().navigateTo("/"));
        JPanel footer = new JPanel();
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        footer.add(backButton);
        add(footer, BorderLayout.SOUTH);
    }
}
