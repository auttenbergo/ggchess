package com.gg.ggchess.model.chess;

public enum Player {
    WHITE("w"),
    BLACK("b");

    private String color;

    Player(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public Player getEnemy() {
        return this == WHITE ? BLACK : WHITE;
    }
}
