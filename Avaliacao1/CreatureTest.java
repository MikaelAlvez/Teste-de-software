package com.lucassf2k.test.jumpingthings;

import com.lucassf2k.main.jumpingthings.Creature;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CreatureTest {
    private final int ID = 1;
    private final int COINS = 1_000_000;
    private final float INITIAL_POSITION_X = 0.0F;

    @Test
    void shouldCreateCreatureByPassingOnlyID() {
        final var creature = new Creature(ID);
        assertThat(creature.getId()).isEqualTo(ID);
        assertThat(creature.getCoins()).isEqualTo(COINS);
        assertThat(creature.getX()).isEqualTo(INITIAL_POSITION_X);
    }

    @Test
    void shouldCreateCreatureByPassingIDPositionXCoins() {
        final var creature = new Creature(ID, INITIAL_POSITION_X, COINS);
        assertThat(creature.getId()).isEqualTo(ID);
        assertThat(creature.getCoins()).isEqualTo(COINS);
        assertThat(creature.getX()).isEqualTo(INITIAL_POSITION_X);
    }

    @Test
    void shouldCreateCreatureByPassingIDAndPositionX() {
        final var creature = new Creature(ID, INITIAL_POSITION_X);
        assertThat(creature.getId()).isEqualTo(ID);
        assertThat(creature.getX()).isEqualTo(INITIAL_POSITION_X);
        assertThat(creature.getCoins()).isEqualTo(COINS);
    }

    @Test
    void shouldRestrictedTwoDecimalPlacesWhenCreatingOrModifyingXProperty() {
        final var positionX1 = 0.3256F;
        final var creature = new Creature(ID, positionX1, COINS);
        assertThat(creature.getX()).isNotEqualTo(positionX1);
        final var positionX2 = 0.45F;
        creature.setX(positionX2);
        assertThat(creature.getX()).isEqualTo(positionX2);
    }

    @Test
    void shouldRestrictPositionXRangeFromNegativeOneToPositiveOne() {
        final var xLimitNegativeOne = -1.00F;
        final var xLimitPositiveOne = 1.00F;
        final var x1NotValid = -2.20F;
        final var x2NotValid = 3.20F;
        final var xValidPositive = 0.55F;
        final var xValidNegative = -0.80F;
        final var creature = new Creature(ID);
        creature.setX(xLimitNegativeOne);
        assertThat(creature.getX()).isEqualTo(xLimitNegativeOne);
        creature.setX(xLimitPositiveOne);
        assertThat(creature.getX()).isEqualTo(xLimitPositiveOne);
        creature.setX(x1NotValid);
        assertThat(creature.getX()).isNotEqualTo(x1NotValid);
        creature.setX(x2NotValid);
        assertThat(creature.getX()).isNotEqualTo(x2NotValid);
        creature.setX(xValidPositive);
        assertThat(creature.getX()).isEqualTo(xValidPositive);
        creature.setX(xValidNegative);
        assertThat(creature.getX()).isEqualTo(xValidNegative);
    }

    @Test
    void shouldAddValuesToCoins() {
        final var value = 100_000;
        final var creature = new Creature(ID);
        creature.addCoins(value);
        assertThat(creature.getCoins()).isEqualTo(COINS + value);
    }

    @Test
    void  shouldNotAllowCoinsHaveNegativeValues() {
        final var creature = new Creature(ID, -100);
        assertThat(creature.getCoins()).isNotEqualTo(-100);
        assertThat(creature.getCoins()).isEqualTo(COINS);
        creature.addCoins(-200);
        assertThat(creature.getCoins()).isNotEqualTo(-200);
        assertThat(creature.getCoins()).isEqualTo(COINS);
    }

    @Test
    void shouldReturnAndHalveValueOfCoins() {
        final var creature = new Creature(ID);
        final var VALUE = COINS / 2;
        assertThat(creature.getHalfCoins()).isEqualTo(VALUE);
        assertThat(creature.getCoins()).isEqualTo(VALUE);
        creature.getHalfCoins();
        assertThat(creature.getCoins()).isEqualTo(VALUE / 2);
    }

    @Test
    void shouldNotDecreaseCoinsWhenHaveReachedOneCoin() {
        final var creature = new Creature(ID, 1);
        creature.getHalfCoins();
        assertThat(creature.getCoins()).isEqualTo(1);
        final var creature2 = new Creature(ID, 0);
        creature2.getHalfCoins();
        assertThat(creature2.getCoins()).isEqualTo(0);
    }
}
