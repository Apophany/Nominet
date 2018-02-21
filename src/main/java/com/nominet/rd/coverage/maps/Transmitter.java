package com.nominet.rd.coverage.maps;

public final class Transmitter {
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

    public int getId() {
        return id;
    }

    public int getXCoordinate() {
        return x;
    }

    public int getYCoordinate() {
        return y;
    }

    public int getPower() {
        return power;
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
