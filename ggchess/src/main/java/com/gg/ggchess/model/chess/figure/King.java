package com.gg.ggchess.model.chess.figure;


import com.gg.ggchess.model.chess.Board;
import com.gg.ggchess.model.chess.Player;
import com.gg.ggchess.model.chess.Position;

import java.util.LinkedHashSet;
import java.util.Set;

public class King extends Figure {
    public King(Player player) {
        super(KING, player);
    }

    // TODO: Add castling
    @Override
    public Set<Position> possibleMoves(Board board, Position from) {
        if (!board.hasFigure(from)) {
            throw new IllegalArgumentException("No figure at position " + from);
        }
        if (board.getFigure(from).getPlayer() != getPlayer()) {
            throw new IllegalArgumentException("Figure at position " + from + " is not yours");
        }

        Set<Position> allEnemyMoves = board.getAllPlayerMoves(getPlayer().getEnemy());

        Set<Position> positions = new LinkedHashSet<>();


        for (int i = from.getX() - 1; i <= from.getX() + 1; i++) {
            for (int j = from.getY() - 1; j <= from.getY() + 1; j++) {
                if (i == from.getX() && j == from.getY()) {
                    continue;
                }
                if (i < 0 || i >= Board.SIZE || j < 0 || j >= Board.SIZE) {
                    continue;
                }

                if (allEnemyMoves.contains(new Position(i, j))) {
                    continue;
                }

                if (!board.hasFigure(i, j)) {
                    positions.add(new Position(i, j));
                } else {
                    Figure figure = board.getFigure(i, j);
                    if (figure.getPlayer() != getPlayer()) {
                        positions.add(new Position(i, j));
                    }
                }
            }
        }

        return positions;
    }
}
