# 🐾 Simulação de Criaturas Saltitantes – Avaliação 1 (Teste de Software)

Este projeto consiste na implementação de um simulador em Java para a disciplina **Teste de Software** (Unidade 1), com foco em práticas de **desenvolvimento orientado a testes (TDD)**, **análise estrutural** e **cobertura de código com 100% de MC/DC (Modified Condition/Decision Coverage)**.

## 🧪 Objetivo

Desenvolver uma simulação gráfica de **criaturas saltitantes**, obedecendo aos requisitos definidos, bem como projetar uma **suíte de testes sistemática e eficiente** com cobertura total. A avaliação considerará especificação, originalidade, documentação, qualidade técnica e rastreabilidade.

## 📝 Descrição da Simulação

A simulação consiste em:

- Um conjunto de `n` criaturas, numeradas de 1 a `n`.
- Cada criatura possui:
    - Uma **posição no horizonte** representada por um número de ponto flutuante (`xi`).
    - Uma **quantidade de moedas de ouro (`gi`)**, inicialmente igual a **1.000.000**.
- A cada **iteração da simulação**:
    1. A criatura atualiza sua posição de acordo com a fórmula:  
       `xi ← xi + r * gi`  
       onde `r` é um número aleatório entre -1 e 1.
    2. Em seguida, a criatura **rouba metade das moedas de ouro** da criatura mais próxima em um dos lados e adiciona esse valor ao seu próprio `gi`.

## 🎯 Requisitos

- Simulação visual iterativa para um número definido de criaturas.
- Suporte a testes de:
    - Domínio
    - Fronteiras
    - Estruturais com **100% de cobertura MC/DC**
- Código com **documentação interna clara**.
- Entrega do projeto em formato `.zip`, com o padrão de nome:  
  `Nome1Nome2-Avaliacao1-TS.zip`

## Pacotes
- xerial.sqlite.jdbc2
- net.jqwik
- mockito-core
- junit-jupiter-api
- assertj-core
- assertj-swing
- assertj-swing-junit

## 🧪 Testes e Cobertura

O projeto inclui uma **suíte de testes unitários em Java**, baseada em JUnit, com foco em:

- Validação de regras de movimentação das criaturas.
- Detecção correta da criatura mais próxima.
- Manipulação segura das moedas de ouro.
- Comprovação da cobertura lógica MC/DC.

## 📅 Cronograma

- **Início:** 02/05/2025 às 17h42
- **Entrega Final:** 22/05/2025 às 23h59

## 🗂 Estrutura do Projeto

O projeto será composto por diversas classes, organizadas de forma clara para garantir modularidade e rastreabilidade entre os requisitos e as implementações.

### Principais componentes (a serem detalhados):

# Classe `Creature` – Resumo de Funcionamento 
## Sua respectiva classe de testes `CreatureTest`

A classe `Creature` representa uma criatura com um identificador (`id`), uma posição no eixo X (`X`) e uma quantidade de moedas (`coins`). Ela é usada para simular movimentações e interações em um ambiente controlado.

---

## Atributos

- `int id`: identificador único da criatura.
- `float X`: posição no eixo X, varia de -1.0 a 1.0.
- `int coins`: quantidade de moedas, inicia com 1.000.000 por padrão.

---

## Construtores

- `Creature(int id)`: cria uma criatura com ID e valores padrão.
- `Creature(int id, float x)`: define ID e posição inicial.
- `Creature(int id, int coins)`: define ID e quantidade de moedas.
- `Creature(int id, float x, int coins)`: define todos os atributos.

---

## Métodos principais

### `addCoins(int value)`
Adiciona moedas ao total **somente se o valor for maior ou igual a zero**.

### `getHalfCoins()`
- Retorna metade das moedas atuais (divisão inteira).
- Subtrai esse valor do total de moedas.

### `updatePosition()`
- Atualiza a posição `X` com base em:
    - Um número aleatório `r` entre -1 e 1.
    - A quantidade de moedas.
- A fórmula usada é: `X += r * coins / 1_000_000`
- A nova posição é arredondada para duas casas decimais e limitada entre -1 e 1.

### `setX(float x)`
- Arredonda `x` para duas casas decimais.
- Só define a nova posição se `x` estiver entre -1 e 1.

---

## Métodos auxiliares

### `loseCoins(int value)`
- Subtrai `value` do total de moedas **se houver mais de 1 moeda**.

### `toTwoDecimalPlaces(float value)`
- Arredonda um valor `float` para duas casas decimais.

---

## Getters e Setters

- `getId()`: retorna o `id`.
- `getX()`: retorna a posição `X`.
- `getCoins()`: retorna o número atual de moedas.
- `setCoins(int coins)`: define o número de moedas (apenas se for >= 0).
- `setId(int id)`: define o ID (lança exceção se for negativo).

---

# Classe `Match` – Resumo de Funcionamento
## Sua respectiva classe de teste `MatchTest`

A classe `Match` gerencia uma simulação com múltiplas criaturas (`Creature`), permitindo interações entre elas com base na proximidade.

---

## Atributos

- `List<Creature> creatures`: lista de criaturas participantes da simulação.
- `float maxDistanceStealCoins`: distância máxima para permitir que uma criatura roube moedas de outra. Valor padrão: 0.3.

---

## Construtor

### `Match(int n)`
- Cria até 30 criaturas com IDs únicos (de 1 até `n`).
- Lança exceção se `n <= 1`.

---

## Método principal

### `iterate()`
Executa uma iteração da simulação:
1. Atualiza a posição de todas as criaturas (`updatePosition()`).
2. Para cada criatura:
    - Procura a criatura mais próxima (dentro da distância `maxDistanceStealCoins`).
    - Se for encontrada e tiver um ID maior, a criatura atual rouba metade das moedas da outra.

---

## Métodos auxiliares

### `findClosestWithinDistance(int idx, double maxDistance)`
- Retorna a criatura mais próxima da criatura no índice `idx`, desde que esteja dentro da distância máxima.
- Retorna `null` se nenhuma criatura estiver dentro do alcance.

### `hasHalfElementsReachedOneCoin()`
- Retorna `true` se **pelo menos metade** das criaturas possuem exatamente 1 moeda.

---

## Configuração da distância

### `setMaxDistanceStealCoins(float value)`
- Atualiza a distância máxima para roubo de moedas.
- Aceita apenas valores entre `0.01f` e `2.0f`, com precisão de duas casas decimais.

### `getMaxDistanceStealCoins()`
- Retorna o valor atual da distância máxima para roubo de moedas.

---

## Outros métodos

### `getCreatures()`
- Retorna a lista de criaturas da simulação.

---

# Classe `VisualizationPanel`
## É necessário o uso de `Mock` para prosseguir com os testes

A classe `VisualizationPanel` é responsável por exibir visualmente a simulação gráfica das criaturas em um painel do tipo `JPanel`. Ela usa a biblioteca Swing do Java para renderizar elementos visuais.

## Principais Responsabilidades

- **Renderizar as criaturas**: Desenha cada criatura como um círculo azul em uma posição horizontal normalizada.
- **Animação de salto**: Cada criatura "salta" verticalmente com base em uma função seno, simulando movimento dinâmico.
- **Linha de base**: Uma linha preta é desenhada na parte inferior como base de referência para os saltos.
- **Painel de informações**: Um painel no canto superior direito exibe a quantidade de moedas de cada criatura, ordenadas de forma decrescente.

## Métodos Importantes

### `paintComponent(Graphics graphics)`
- Redefinido para desenhar os elementos gráficos:
    - Posiciona as criaturas com base em suas posições `x`, convertidas do intervalo `[-1, 1]` para coordenadas de tela.
    - Aplica um deslocamento vertical animado (salto) com base no tempo atual.
    - Exibe um painel lateral com a quantidade de moedas de cada criatura, formatada no padrão brasileiro.

### `normalizesCreaturePositionScreen(float positionX)`
- Converte a posição `x` de uma criatura do intervalo `[-1, 1]` para `[0.0, 1.0]` (normalização para coordenadas de tela).

### `formatterCoins(int coins)`
- Formata o número de moedas de uma criatura conforme o padrão brasileiro (`pt-BR`), sem casas decimais (ex: `2500` → `"2.500"`).

## Campos
- `Match match`: Objeto que representa a simulação atual e fornece acesso às criaturas.

# Classe `App`
## Também acho que será necessária o uso de `Mock` para testar

A classe `App` representa o ponto de entrada da aplicação de simulação gráfica de criaturas saltitantes.

## Objetivo

Iniciar a simulação com uma interface gráfica (`JFrame`) e atualizar a lógica da simulação em intervalos regulares usando um `Timer`.

## Funcionamento

### Constantes
- `TIMER_MS = 1000`: Intervalo de tempo entre atualizações (1 segundo).
- `MAXIMUM_MATCH_DURATION = 100`: Duração máxima da simulação em iterações.

### `main(String[] args)`
1. **Criação da simulação**:
    - Instancia um objeto `Match` com 30 criaturas.
    - Cria um `VisualizationPanel` para exibir as criaturas.
    - Adiciona o painel a uma `JFrame` com título e configurações básicas.

2. **Timer**:
    - Usa `javax.swing.Timer` para executar ações a cada 1 segundo.
    - Em cada execução:
        - Chama `match.iterate()` para atualizar o estado da simulação.
        - Rechama `panel.repaint()` para redesenhar a tela.
        - Aumenta o contador de iterações.
        - Interrompe a simulação se:
            - Atingir o número máximo de iterações (`100`), ou
            - Metade das criaturas tiver pelo menos uma moeda (`match.hasHalfElementsReachedOneCoin()`).

3. **Encerramento**:
    - Ao final da simulação, exibe uma mensagem de encerramento (`JOptionPane`).

## Componentes Utilizados
- `Match`: Representa o estado da simulação.
- `VisualizationPanel`: Painel gráfico para exibir as criaturas.
- `JFrame`: Janela principal da interface gráfica.
- `Timer`: Atualiza a simulação periodicamente.
