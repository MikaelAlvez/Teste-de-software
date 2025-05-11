    package com.lucassf2k.main.jumpingthings;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Creature {
    private final int id;
    private float X = 0.0f;
    private int coins = 1_000_000;

    public Creature(final int id) {
        this.id = id;
    }

    public Creature(final int id, final float x) {
        this.id = id;
        setX(x);
    }

    public Creature(final int id, final int coins) {
        this.id = id;
        setCoins(coins);
    }

    public Creature(final int id, final float x, final int coins) {
        this.id = id;
        setX(x);
        setCoins(coins);
    }

    public void addCoins(final int value) {
        if(value >= 0) this.coins += value;
    }


    public int getHalfCoins() {
        final var halfCoins = this.coins / 2;
        loseCoins(halfCoins);
        return halfCoins;
    }

    private void loseCoins(final int value) {
        if (this.coins > 1) this.coins -= value;
    }

    private float toTwoDecimalPlaces(final float value) {
        return new BigDecimal(Float.toString(value))
                .setScale(2, RoundingMode.HALF_EVEN)
                .floatValue();
    }


    public int getId() {
        return id;
    }

    public float getX() {
        return X;
    }

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
