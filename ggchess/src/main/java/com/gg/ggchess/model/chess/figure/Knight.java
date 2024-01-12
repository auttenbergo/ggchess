package com.gg.ggchess.model.chess.figure;


import com.gg.ggchess.model.chess.Board;
import com.gg.ggchess.model.chess.Player;
import com.gg.ggchess.model.chess.Position;

import java.util.LinkedHashSet;
import java.util.Set;

public class Knight extends Figure {
    public Knight(Player player) {
        super(Figure.KNIGHT, player);
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

        // UP|LEFT
        if (from.getX() - 2 >= 0 && from.getY() - 1 >= 0) {
            if (!board.hasFigure(from.getX() - 2, from.getY() - 1)) {
                positions.add(new Position(from.getX() - 2, from.getY() - 1));
            } else {
                Figure figure = board.getFigure(from.getX() - 2, from.getY() - 1);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(from.getX() - 2, from.getY() - 1));
                }
            }
        }

        // UP|RIGHT
        if (from.getX() - 2 >= 0 && from.getY() + 1 < Board.SIZE) {
            if (!board.hasFigure(from.getX() - 2, from.getY() + 1)) {
                positions.add(new Position(from.getX() - 2, from.getY() + 1));
            } else {
                Figure figure = board.getFigure(from.getX() - 2, from.getY() + 1);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(from.getX() - 2, from.getY() + 1));
                }
            }
        }

        // DOWN|LEFT
        if (from.getX() + 2 < Board.SIZE && from.getY() - 1 >= 0) {
            if (!board.hasFigure(from.getX() + 2, from.getY() - 1)) {
                positions.add(new Position(from.getX() + 2, from.getY() - 1));
            } else {
                Figure figure = board.getFigure(from.getX() + 2, from.getY() - 1);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(from.getX() + 2, from.getY() - 1));
                }
            }
        }

        // DOWN|RIGHT
        if (from.getX() + 2 < Board.SIZE && from.getY() + 1 < Board.SIZE) {
            if (!board.hasFigure(from.getX() + 2, from.getY() + 1)) {
                positions.add(new Position(from.getX() + 2, from.getY() + 1));
            } else {
                Figure figure = board.getFigure(from.getX() + 2, from.getY() + 1);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(from.getX() + 2, from.getY() + 1));
                }
            }
        }

        // LEFT|UP
        if (from.getX() - 1 >= 0 && from.getY() - 2 >= 0) {
            if (!board.hasFigure(from.getX() - 1, from.getY() - 2)) {
                positions.add(new Position(from.getX() - 1, from.getY() - 2));
            } else {
                Figure figure = board.getFigure(from.getX() - 1, from.getY() - 2);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(from.getX() - 1, from.getY() - 2));
                }
            }
        }

        // LEFT|DOWN
        if (from.getX() + 1 < Board.SIZE && from.getY() - 2 >= 0) {
            if (!board.hasFigure(from.getX() + 1, from.getY() - 2)) {
                positions.add(new Position(from.getX() + 1, from.getY() - 2));
            } else {
                Figure figure = board.getFigure(from.getX() + 1, from.getY() - 2);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(from.getX() + 1, from.getY() - 2));
                }
            }
        }

        // RIGHT|UP
        if (from.getX() - 1 >= 0 && from.getY() + 2 < Board.SIZE) {
            if (!board.hasFigure(from.getX() - 1, from.getY() + 2)) {
                positions.add(new Position(from.getX() - 1, from.getY() + 2));
            } else {
                Figure figure = board.getFigure(from.getX() - 1, from.getY() + 2);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(from.getX() - 1, from.getY() + 2));
                }
            }
        }

        // RIGHT|DOWN
        if (from.getX() + 1 < Board.SIZE && from.getY() + 2 < Board.SIZE) {
            if (!board.hasFigure(from.getX() + 1, from.getY() + 2)) {
                positions.add(new Position(from.getX() + 1, from.getY() + 2));
            } else {
                Figure figure = board.getFigure(from.getX() + 1, from.getY() + 2);
                if (figure.getPlayer() != getPlayer()) {
                    positions.add(new Position(from.getX() + 1, from.getY() + 2));
                }
            }
        }

        return positions;
    }
}
