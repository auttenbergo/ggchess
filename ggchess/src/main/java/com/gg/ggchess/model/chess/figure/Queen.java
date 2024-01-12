package com.gg.ggchess.model.chess.figure;


import com.gg.ggchess.model.chess.Board;
import com.gg.ggchess.model.chess.Player;
import com.gg.ggchess.model.chess.Position;

import java.util.LinkedHashSet;
import java.util.Set;

public class Queen extends Figure {
    public Queen(Player player) {
        super(Figure.QUEEN, player);
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

        positions.addAll(getHorizontalMoves(board, from));
        positions.addAll(getVerticalMoves(board, from));
        positions.addAll(getDiagonalMoves(board, from));

        return positions;
    }

    private Set<Position> getHorizontalMoves(Board board, Position from) {
        Set<Position> positions = new LinkedHashSet<>();

        // Horizontally left
        for (int i = from.getY() - 1; i >= 0; i--) {
            if (board.hasFigure(from.getX(), i)) {
                Figure figure = board.getFigure(from.getX(), i);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(from.getX(), i));
                }
                break;
            }
            positions.add(new Position(from.getX(), i));
        }

        // Horizontally right
        for (int i = from.getY() + 1; i < Board.SIZE; i++) {
            if (board.hasFigure(from.getX(), i)) {
                Figure figure = board.getFigure(from.getX(), i);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(from.getX(), i));
                }
                break;
            }
            positions.add(new Position(from.getX(), i));
        }

        return positions;
    }

    private Set<Position> getVerticalMoves(Board board, Position from) {
        Set<Position> positions = new LinkedHashSet<>();

        // Vertically up
        for (int i = from.getX() - 1; i >= 0; i--) {
            if (board.hasFigure(i, from.getY())) {
                Figure figure = board.getFigure(i, from.getY());
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(i, from.getY()));
                }
                break;
            }
            positions.add(new Position(i, from.getY()));
        }

        // Vertically down
        for (int i = from.getX() + 1; i < Board.SIZE; i++) {
            if (board.hasFigure(i, from.getY())) {
                Figure figure = board.getFigure(i, from.getY());
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(i, from.getY()));
                }
                break;
            }
            positions.add(new Position(i, from.getY()));
        }

        return positions;
    }

    private Set<Position> getDiagonalMoves(Board board, Position from) {
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
