package com.nominet.rd.coverage.maps;

public final class Transmitter implements Tower {
    private final int id;
    private final int x;
    private final int y;
    private final int power;

    public Transmitter(int id, int x, int y, int power) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.power = power;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getXCoordinate() {
        return x;
    }

    @Override
    public int getYCoordinate() {
        return y;
    }

    public int getPower() {
        return power;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transmitter that = (Transmitter) o;
        return id == that.id &&
                x == that.x &&
                y == that.y &&
                power == that.power;
    }

    @Override
    public String toString() {
        return "Transmitter{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", power=" + power +
                '}';
    }
}
