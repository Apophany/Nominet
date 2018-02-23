package com.nominet.rd.coverage.maps;

public final class Receiver implements Tower {
    private final int id;
    private final int x;
    private final int y;

    public Receiver(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
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

    @Override
    public String toString() {
        return "Receiver{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
