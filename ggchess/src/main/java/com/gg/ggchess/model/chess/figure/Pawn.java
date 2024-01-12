package com.gg.ggchess.model.chess.figure;


import com.gg.ggchess.model.chess.Board;
import com.gg.ggchess.model.chess.Player;
import com.gg.ggchess.model.chess.Position;

import java.util.LinkedHashSet;
import java.util.Set;

public class Pawn extends Figure {


    public Pawn(Player player) {
        super(PAWN, player);
    }

    // TODO: En passant
    @Override
    public Set<Position> possibleMoves(Board board, Position from) {
        if (!board.hasFigure(from)) {
            throw new IllegalArgumentException("No figure at position " + from);
        }
        if (board.getFigure(from).getPlayer() != getPlayer()) {
            throw new IllegalArgumentException("Figure at position " + from + " is not yours");
        }

        Set<Position> positions = new LinkedHashSet<>();

        // UP
        if (getPlayer() == Player.WHITE) {
            // Forward one/two
            if (from.getX() - 1 >= 0) {
                if (!board.hasFigure(from.getX() - 1, from.getY())) {
                    positions.add(new Position(from.getX() - 1, from.getY()));

                    if(from.getX() == 6 && !board.hasFigure(from.getX() - 2, from.getY())){
                        positions.add(new Position(from.getX() - 2, from.getY()));
                    }

                }
            }

            // Up left attack
            if (from.getX() - 1 >= 0 && from.getY() - 1 >= 0) {
                if (board.hasFigure(from.getX() - 1, from.getY() - 1)) {
                    Figure figure = board.getFigure(from.getX() - 1, from.getY() - 1);
                    if (figure.getPlayer() != getPlayer()) {
                        positions.add(new Position(from.getX() - 1, from.getY() - 1));
                    }
                }
            }

            // Up right attack
            if (from.getX() - 1 >= 0 && from.getY() + 1 < Board.SIZE) {
                if (board.hasFigure(from.getX() - 1, from.getY() + 1)) {
                    Figure figure = board.getFigure(from.getX() - 1, from.getY() + 1);
                    if (figure.getPlayer() != getPlayer()) {
                        positions.add(new Position(from.getX() - 1, from.getY() + 1));
                    }
                }
            }

        } else {
            // Forward one
            if (from.getX() + 1 < Board.SIZE) {
                if (!board.hasFigure(from.getX() + 1, from.getY())) {
                    positions.add(new Position(from.getX() + 1, from.getY()));
                }
            }

            // Forward two
            if (from.getX() == 1) {
                if (!board.hasFigure(3, from.getY())) {
                    positions.add(new Position(3, from.getY()));
                }
            }

            // Up left attack
            if (from.getX() + 1 < Board.SIZE && from.getY() - 1 >= 0) {
                if (board.hasFigure(from.getX() + 1, from.getY() - 1)) {
                    Figure figure = board.getFigure(from.getX() + 1, from.getY() - 1);
                    if (figure.getPlayer() != getPlayer()) {
                        positions.add(new Position(from.getX() + 1, from.getY() - 1));
                    }
                }
            }

            // Up right attack
            if (from.getX() + 1 < Board.SIZE && from.getY() + 1 < Board.SIZE) {
                if (board.hasFigure(from.getX() + 1, from.getY() + 1)) {
                    Figure figure = board.getFigure(from.getX() + 1, from.getY() + 1);
                    if (figure.getPlayer() != getPlayer()) {
                        positions.add(new Position(from.getX() + 1, from.getY() + 1));
                    }
                }
            }
        }

        return positions;
    }
}
