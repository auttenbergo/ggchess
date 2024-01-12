package com.gg.ggchess.model.chess.figure;

import com.gg.ggchess.model.chess.Board;
import com.gg.ggchess.model.chess.Player;
import com.gg.ggchess.model.chess.Position;

import java.util.LinkedHashSet;
import java.util.Set;

public class Bishop extends Figure {
    public Bishop(Player player) {
        super(BISHOP, player);
    }

    @Override
    public Set<Position> possibleMoves(Board board, Position from) {
        if (!board.hasFigure(from)) {
            throw new IllegalArgumentException("No figure at position " + from);
        }
        if (board.getFigure(from).getPlayer() != getPlayer()) {
            throw new IllegalArgumentException("Figure at position " + from + " is not yours");
        }

        Set<Position> positions = new LinkedHashSet<>();

        // Diagonal UP|LEFT
        for (int i = from.getX() - 1, j = from.getY() - 1; i >= 0 && j >= 0; i--, j--) {
            if (board.hasFigure(i, j)) {
                Figure figure = board.getFigure(i, j);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(i, j));
                }
                break;
            }
            positions.add(new Position(i, j));
        }

        // Diagonal UP|RIGHT
        for (int i = from.getX() - 1, j = from.getY() + 1; i >= 0 && j < Board.SIZE; i--, j++) {
            if (board.hasFigure(i, j)) {
                Figure figure = board.getFigure(i, j);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(i, j));
                }
                break;
            }
            positions.add(new Position(i, j));
        }

        // Diagonal DOWN|LEFT
        for (int i = from.getX() + 1, j = from.getY() - 1; i < Board.SIZE && j >= 0; i++, j--) {
            if (board.hasFigure(i, j)) {
                Figure figure = board.getFigure(i, j);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(i, j));
                }
                break;
            }
            positions.add(new Position(i, j));
        }

        // Diagonal DOWN|RIGHT
        for (int i = from.getX() + 1, j = from.getY() + 1; i < Board.SIZE && j < Board.SIZE; i++, j++) {
            if (board.hasFigure(i, j)) {
                Figure figure = board.getFigure(i, j);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(i, j));
                }
                break;
            }
            positions.add(new Position(i, j));
        }

        return positions;
    }
}
