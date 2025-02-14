package com.gg.ggchess.model.chess.figure;


import com.gg.ggchess.model.chess.Board;
import com.gg.ggchess.model.chess.FigureMove;
import com.gg.ggchess.model.chess.Player;
import com.gg.ggchess.model.chess.Position;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Rook extends Figure {
    public Rook(Player player) {
        super(Figure.ROOK, player);
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

        return positions.stream()
                .map(pos -> new FigureMove(from, pos, this))
                .collect(Collectors.toSet());
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

        return positions.stream()
                .map(pos -> new FigureMove(from, pos, this))
                .collect(Collectors.toSet());
    }

}
