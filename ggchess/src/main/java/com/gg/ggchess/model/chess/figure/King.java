package com.gg.ggchess.model.chess.figure;


import com.gg.ggchess.model.chess.Board;
import com.gg.ggchess.model.chess.FigureMove;
import com.gg.ggchess.model.chess.Player;
import com.gg.ggchess.model.chess.Position;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class King extends Figure {
    public King(Player player) {
        super(KING, player);
    }

    @Override
    public Set<FigureMove> possibleMoves(Board board, Position from) {
        if (!board.hasFigure(from)) {
            throw new IllegalArgumentException("No figure at position " + from);
        }
        if (board.getFigure(from).getPlayer() != getPlayer()) {
            throw new IllegalArgumentException("Figure at position " + from + " is not yours");
        }

        Set<Position> allEnemyMoves = board.getAllPlayerAttackMoves(getPlayer().getEnemy()).stream()
                .map(FigureMove::getTo)
                .collect(Collectors.toSet());

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

        if (!board.figureHasMoved(this)) {
            if (getPlayer() == Player.WHITE) {
                // Check that lower left corner is rook and it has not moved
                if (board.hasFigure(Board.SIZE - 1, 0) && board.getFigure(Board.SIZE - 1, 0) instanceof Rook rook) {
                    if (!board.figureHasMoved(rook)) {

                        boolean canRookMove = true;

                        for (int i = 1; i < from.getY(); i++) {
                            if (board.hasFigure(Board.SIZE - 1, i)) {
                                canRookMove = false;
                                break;
                            }
                            Position position = new Position(Board.SIZE - 1, i);
                            if (allEnemyMoves.contains(position)) {
                                canRookMove = false;
                                break;
                            }
                        }

                        if (canRookMove) {
                            positions.add(new Position(from.getX(), 1));
                        }
                    }
                }
                // Check that lower right corner is rook and it has not moved
                if (board.hasFigure(Board.SIZE - 1, Board.SIZE - 1) && board.getFigure(Board.SIZE - 1, Board.SIZE - 1) instanceof Rook rook) {
                    if (!board.figureHasMoved(rook)) {

                        boolean canRookMove = true;

                        for (int i = from.getY() + 1; i < Board.SIZE - 2; i++) {
                            if (board.hasFigure(Board.SIZE - 1, i)) {
                                canRookMove = false;
                                break;
                            }
                            Position position = new Position(Board.SIZE - 1, i);
                            if (allEnemyMoves.contains(position)) {
                                canRookMove = false;
                                break;
                            }
                        }

                        if (canRookMove) {
                            positions.add(new Position(from.getX(), 6));
                        }
                    }
                }
            }

            if (getPlayer() == Player.BLACK) {
                // Check that upper left corner is rook and it has not moved
                if (board.hasFigure(0, 0) && board.getFigure(0, 0) instanceof Rook rook) {
                    if (!board.figureHasMoved(rook)) {

                        boolean canRookMove = true;

                        for (int i = 1; i < from.getY(); i++) {
                            if (board.hasFigure(0, i)) {
                                canRookMove = false;
                                break;
                            }
                            Position position = new Position(0, i);
                            if (allEnemyMoves.contains(position)) {
                                canRookMove = false;
                                break;
                            }
                        }

                        if (canRookMove) {
                            positions.add(new Position(from.getX(), 1));
                        }
                    }
                }
                // Check that upper right corner is rook and it has not moved
                if (board.hasFigure(0, Board.SIZE - 1) && board.getFigure(0, Board.SIZE - 1) instanceof Rook rook) {
                    if (!board.figureHasMoved(rook)) {

                        boolean canRookMove = true;

                        for (int i = from.getY() + 1; i < Board.SIZE - 2; i++) {
                            if (board.hasFigure(0, i)) {
                                canRookMove = false;
                                break;
                            }
                            Position position = new Position(0, i);
                            if (allEnemyMoves.contains(position)) {
                                canRookMove = false;
                                break;
                            }
                        }

                        if (canRookMove) {
                            positions.add(new Position(from.getX(), 6));
                        }
                    }
                }
            }


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

        for (int i = from.getX() - 1; i <= from.getX() + 1; i++) {
            for (int j = from.getY() - 1; j <= from.getY() + 1; j++) {
                if (i == from.getX() && j == from.getY()) {
                    continue;
                }
                if (i < 0 || i >= Board.SIZE || j < 0 || j >= Board.SIZE) {
                    continue;
                }

                positions.add(new Position(i, j));
            }
        }
        return positions.stream()
                .map(pos -> new FigureMove(from, pos, this))
                .collect(Collectors.toSet());
    }
}
