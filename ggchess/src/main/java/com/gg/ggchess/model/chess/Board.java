package com.gg.ggchess.model.chess;


import com.gg.ggchess.exception.ChessException;
import com.gg.ggchess.model.chess.figure.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Board {
    public static final int SIZE = 8;
    private Figure[][] board;
    private Player player;
    private King whiteKing;
    private King blackKing;

    public Map<String, String> initialize() {
        initializeBlacks();
        initializeWhites();
        player = Player.WHITE;
        return getBoard();
    }

    private void initializeBlacks() {
        for (int i = 0; i < SIZE; i++) {
            board[1][i] = new Pawn(Player.BLACK);
        }
        board[0][0] = new Rook(Player.BLACK);
        board[0][1] = new Knight(Player.BLACK);
        board[0][2] = new Bishop(Player.BLACK);
        board[0][3] = new Queen(Player.BLACK);

        blackKing = new King(Player.BLACK);
        board[0][4] = blackKing;

        board[0][5] = new Bishop(Player.BLACK);
        board[0][6] = new Knight(Player.BLACK);
        board[0][7] = new Rook(Player.BLACK);
    }

    private void initializeWhites() {
        for (int i = 0; i < SIZE; i++) {
            board[6][i] = new Pawn(Player.WHITE);
        }
        board[7][0] = new Rook(Player.WHITE);
        board[7][1] = new Knight(Player.WHITE);
        board[7][2] = new Bishop(Player.WHITE);
        board[7][3] = new Queen(Player.WHITE);

        whiteKing = new King(Player.WHITE);
        board[7][4] = whiteKing;
        board[7][5] = new Bishop(Player.WHITE);
        board[7][6] = new Knight(Player.WHITE);
        board[7][7] = new Rook(Player.WHITE);
    }

    public Board() {
        board = new Figure[SIZE][SIZE];
    }

    public boolean hasFigure(Position position) {
        return board[position.getX()][position.getY()] != null;
    }

    public Figure getFigure(Position position) {
        return board[position.getX()][position.getY()];
    }

    public Figure getFigure(int x, int y) {
        return board[x][y];
    }

    public boolean hasFigure(int x, int y) {
        return board[x][y] != null;
    }

    public void moveFigure(String from, String to) throws ChessException {
        Position fromPosition = new Position(from);
        Position toPosition = new Position(to);
        moveFigure(fromPosition, toPosition);
    }

    // TODO: King -> Add that must move if targeted by enemy (if no moves, game over)

    public void moveFigure(Position from, Position to) throws ChessException {
        Figure target = getFigure(from);
        if (target == null) {
            throw new ChessException("No figure at position " + from);
        }
        if (target.getPlayer() != player) {
            throw new ChessException("Figure at position " + from + " is not yours");
        }

        if (!target.canMove(this, from, to)) {
            throw new ChessException("Figure at position " + from + " can't move to " + to);
        }

        // Check if current King is targeted and other Piece wants to move
        // If piece moves to kill the enemy, check if King is still targeted
        King king = getCurrentKing();
        if (target != king && isCurrentKingTargeted()) {
            if (isCurrentKingTargetedAfterMove(from, to)) {
                throw new ChessException("King is targeted");
            }

            board[to.getX()][to.getY()] = target;
            board[from.getX()][from.getY()] = null;
            player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
            return;
        }

        // Check if after the move King is targeted
        if (isCurrentKingTargetedAfterMove(from, to)) {
            throw new ChessException("King will be targeted after the move");
        }

        if (board[to.getX()][to.getY()] != null) {
            // TODO: Add check for castling

            Figure conflictFigure = getFigure(to);
            Player enemyPlayer = player == Player.WHITE ? Player.BLACK : Player.WHITE;
            if (conflictFigure.getPlayer() == enemyPlayer) {
                board[to.getX()][to.getY()] = target;
                board[from.getX()][from.getY()] = null;
                player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
                return;
            }
        }

        // TODO: Add promotion (when pawn goes to the end)

        board[to.getX()][to.getY()] = target;
        board[from.getX()][from.getY()] = null;
        player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
    }

    private Position getKingPosition(Figure figure) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == figure) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    public Set<FigureMove> getAllPlayerAttackMoves(Player player) {
        Set<FigureMove> allPositions = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null || board[i][j].getPlayer() != player)
                    continue;
                Figure figure = getFigure(i, j);

                Position position = new Position(i, j);

                allPositions.addAll(figure.attackMoves(this, position));

            }
        }
        return allPositions;
    }

    private boolean isCurrentKingTargeted() {
        King king = getCurrentKing();
        Position kingPosition = getKingPosition(king);

        Set<FigureMove> allEnemyMoves = getAllPlayerAttackMoves(player.getEnemy());
        return allEnemyMoves.stream()
                .map(FigureMove::getTo)
                .collect(Collectors.toSet())
                .contains(kingPosition);
    }

    private boolean isCurrentKingTargetedAfterMove(Position from, Position to) {
        Figure previousFrom = getFigure(from);
        Figure previousTo = getFigure(to);

        try {
            board[from.getX()][from.getY()] = null;
            board[to.getX()][to.getY()] = previousFrom;


            King king = getCurrentKing();
            Position kingPosition = getKingPosition(king);

            Set<FigureMove> allEnemyMoves = getAllPlayerAttackMoves(player.getEnemy());
            return allEnemyMoves.stream()
                    .map(FigureMove::getTo)
                    .collect(Collectors.toSet())
                    .contains(kingPosition);
        } finally {
            board[from.getX()][from.getY()] = previousFrom;
            board[to.getX()][to.getY()] = previousTo;
        }
    }

    private King getCurrentKing() {
        return player == Player.WHITE ? whiteKing : blackKing;
    }

    public Map<String, String> getBoard() {
        Map<String, String> boardMap = new HashMap<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null)
                    continue;

                String position = (char) ('a' + j) + "" + (SIZE - i);
                String figure = board[i][j].getName();
                boardMap.put(position, figure);
            }
        }
        return boardMap;
    }

}
