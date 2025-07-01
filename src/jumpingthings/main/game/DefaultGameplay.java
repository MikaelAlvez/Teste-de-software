package jumpingthings.main.game;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DefaultGameplay {
    private float X = 0.0f;
    private int coins = 1_000_000;

    public DefaultGameplay(final float x, final int coins) {
        setX(x);
        setCoins(coins);
    }


    /**
     * Adiciona uma quantidade positiva de moedas ao total atual da criatura.
     * <p>
     * Este método incrementa o saldo de moedas apenas se o valor informado for
     * maior ou igual a zero. Valores negativos são ignorados, garantindo que
     * o número total de moedas nunca diminua com esta operação.
     * </p>
     *
     * @param value a quantidade de moedas a ser adicionada; deve ser zero ou positiva.
     */
    public void addCoins(final int value) {
        if(value >= 0) this.coins += value;
    }


    /**
     * Retorna a metade das moedas da criatura, arredondando para baixo se necessário,
     * e remove essa quantidade do total atual de moedas da criatura.
     * <p>
     * Este método simula uma perda parcial de moedas, por exemplo, em uma situação
     * de penalidade ou transferência. A quantidade removida é calculada com
     * divisão inteira ({@code coins / 2}), ou seja:
     * <ul>
     *     <li>Se {@code coins = 5}, retorna {@code 2}</li>
     *     <li>Se {@code coins = 10}, retorna {@code 5}</li>
     * </ul>
     * Após a chamada, o valor retornado também é subtraído do saldo de moedas da criatura.
     * </p>
     *
     * @return A quantidade de moedas retirada (metade do valor atual).
     */
    public int getHalfCoins() {
        final var halfCoins = this.coins / 2;
        loseCoins(halfCoins);
        return halfCoins;
    }

    /**
     * Atualiza a posição da criatura no eixo X com base em um valor aleatório e na quantidade de moedas.
     * <p>
     * A nova posição é calculada somando um deslocamento aleatório à posição atual.
     * O deslocamento é proporcional ao número de moedas da criatura, mas escalado
     * por um fator de 1.000.000 para manter o movimento suave e controlado.
     * </p>
     * <p>
     * A posição resultante é então limitada ao intervalo [-1, 1], garantindo que
     * a criatura permaneça dentro dos limites da simulação.
     * </p>
     *
     * <p><b>Exemplo de cálculo:</b><br>
     * Se {@code coins = 500_000} e {@code r = 0.5}, então o deslocamento será
     * {@code 0.5 * 500_000 / 1_000_000 = 0.25}.
     * </p>
     *
     * @see #setX(float) Garante que a posição final esteja dentro dos limites.
     */
    public void updatePosition() {
//        double r = -1 + 2 * Math.random(); // r ∈ [-1, 1]
//        this.X += (float) (r * this.coins);double r = -1 + 2 * Math.random(); // r ∈ [-1, 1]
        double r = -1 + 2 * Math.random(); // r ∈ [-1, 1]
        this.X += (float) (r * this.coins / 1_000_000); // escala coins para até 1.0
        setX(Math.max(-1f, Math.min(1f, this.X))); // mantém X em [-1, 1]
    }

    private void loseCoins(final int value) {
        if (this.coins > 1) this.coins -= value;
    }

    private float toTwoDecimalPlaces(final float value) {
        return new BigDecimal(Float.toString(value))
                .setScale(2, RoundingMode.HALF_EVEN)
                .floatValue();
    }

    public float getX() {
        return X;
    }

    /**
     * Define a nova posição da criatura no eixo X, garantindo que o valor esteja dentro do intervalo válido [-1, 1].
     * <p>
     * Antes de aplicar o valor, ele é arredondado para duas casas decimais usando o método {@code toTwoDecimalPlaces}.
     * Isso ajuda a evitar problemas de precisão numérica em simulações contínuas.
     * </p>
     * <p>
     * Se o valor após o arredondamento estiver fora do intervalo permitido, a posição não é alterada.
     * </p>
     *
     * @param x A nova posição proposta no eixo X. Deve estar entre -1 e 1 (inclusive) após o arredondamento.
     *
     * @see #toTwoDecimalPlaces(float) Método utilizado para limitar a precisão da posição.
     */
    public void setX(final float x) {
        final var tmpX = toTwoDecimalPlaces(x);
        if (tmpX >= -1 && tmpX <= 1) this.X = tmpX;
    }

    public int getCoins() {
        return coins;
    }

    private void setCoins(final int coins) {
        if (coins >= 0) this.coins = coins;
    }
}
