package com.gg.ggchess.model.chess;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Position {
    private final int x;
    private final int y;

    @Override
    public String toString() {
        return "Position{" + "x=" + x + ", y=" + y + '}';
    }

    public Position(String position) {
        if (position.length() != 2) {
            throw new IllegalArgumentException("Invalid position");
        }
        this.y = position.charAt(0) - 'a';
        this.x = Board.SIZE - (position.charAt(1) - '0');
        System.out.println();
    }

    public Position(int x, int y) {
        if (x < 0 || x > Board.SIZE || y < 0 || y > Board.SIZE) {
            throw new IllegalArgumentException("Invalid position");
        }
        this.x = x;
        this.y = y;
    }
}
