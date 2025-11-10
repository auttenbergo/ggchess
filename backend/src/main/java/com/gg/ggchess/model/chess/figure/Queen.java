package com.gg.ggchess.model.chess.figure;


import com.gg.ggchess.model.chess.Board;
import com.gg.ggchess.model.chess.FigureMove;
import com.gg.ggchess.model.chess.Player;
import com.gg.ggchess.model.chess.Position;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Queen extends Figure {
    public Queen(Player player) {
        super(Figure.QUEEN, player);
    }

    @Override
    public Set<FigureMove> possibleMoves(Board board, Position from) {
        if (!board.hasFigure(from)) {
            throw new IllegalArgumentException("No figure at position " + from);
        }
        if (board.getFigure(from).getPlayer() != getPlayer()) {
            throw new IllegalArgumentException("Figure at position " + from + " is not yours");
        }

        Set<Position> positions = new LinkedHashSet<>();

        positions.addAll(getHorizontalPossibleMoves(board, from));
        positions.addAll(getVerticalPossibleMoves(board, from));
        positions.addAll(getDiagonalPossibleMoves(board, from));

        return positions.stream()
                .map(pos -> new FigureMove(from, pos, this))
                .collect(Collectors.toSet());
    }

    private Set<Position> getHorizontalPossibleMoves(Board board, Position from) {
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

    private Set<Position> getHorizontalAttackMoves(Board board, Position from) {
        Set<Position> positions = new LinkedHashSet<>();

        // Horizontally left
        for (int i = from.getY() - 1; i >= 0; i--) {
            positions.add(new Position(from.getX(), i));
            if (board.hasFigure(from.getX(), i)) {
                break;
            }
        }

        // Horizontally right
        for (int i = from.getY() + 1; i < Board.SIZE; i++) {
            positions.add(new Position(from.getX(), i));
            if (board.hasFigure(from.getX(), i)) {
                break;
            }
        }

        return positions;
    }

    private Set<Position> getVerticalPossibleMoves(Board board, Position from) {
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

    private Set<Position> getVerticalAttackMoves(Board board, Position from) {
        Set<Position> positions = new LinkedHashSet<>();

        // Vertically up
        for (int i = from.getX() - 1; i >= 0; i--) {
            positions.add(new Position(i, from.getY()));
            if (board.hasFigure(i, from.getY())) {
                break;
            }
        }

        // Vertically down
        for (int i = from.getX() + 1; i < Board.SIZE; i++) {
            positions.add(new Position(i, from.getY()));
            if (board.hasFigure(i, from.getY())) {
                break;
            }
        }

        return positions;
    }

    private Set<Position> getDiagonalPossibleMoves(Board board, Position from) {
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

    private Set<Position> getDiagonalAttackMoves(Board board, Position from) {
        Set<Position> positions = new LinkedHashSet<>();

        // Diagonal UP|LEFT
        for (int i = from.getX() - 1, j = from.getY() - 1; i >= 0 && j >= 0; i--, j--) {
            positions.add(new Position(i, j));
            if (board.hasFigure(i, j)) {
                break;
            }
        }

        // Diagonal UP|RIGHT
        for (int i = from.getX() - 1, j = from.getY() + 1; i >= 0 && j < Board.SIZE; i--, j++) {
            positions.add(new Position(i, j));
            if (board.hasFigure(i, j)) {
                break;
            }
        }

        // Diagonal DOWN|LEFT
        for (int i = from.getX() + 1, j = from.getY() - 1; i < Board.SIZE && j >= 0; i++, j--) {
            positions.add(new Position(i, j));
            if (board.hasFigure(i, j)) {
                break;
            }
        }

        // Diagonal DOWN|RIGHT
        for (int i = from.getX() + 1, j = from.getY() + 1; i < Board.SIZE && j < Board.SIZE; i++, j++) {
            positions.add(new Position(i, j));
            if (board.hasFigure(i, j)) {
                break;
            }
        }

        return positions;
    }

    @Override
    public Set<FigureMove> attackMoves(Board board, Position from) {
        if (!board.hasFigure(from)) {
            throw new IllegalArgumentException("No figure at position " + from);
        }
        if (board.getFigure(from).getPlayer() != getPlayer()) {
            throw new IllegalArgumentException("Figure at position " + from + " is not yours");
        }

        Set<Position> positions = new LinkedHashSet<>();

        positions.addAll(getHorizontalAttackMoves(board, from));
        positions.addAll(getVerticalAttackMoves(board, from));
        positions.addAll(getDiagonalAttackMoves(board, from));

        return positions.stream()
                .map(pos -> new FigureMove(from, pos, this))
                .collect(Collectors.toSet());
    }
}
