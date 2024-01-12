package com.gg.ggchess.model.chess;


import com.gg.ggchess.exception.ChessException;
import com.gg.ggchess.model.chess.figure.Bishop;
import com.gg.ggchess.model.chess.figure.Figure;
import com.gg.ggchess.model.chess.figure.King;
import com.gg.ggchess.model.chess.figure.Knight;
import com.gg.ggchess.model.chess.figure.Pawn;
import com.gg.ggchess.model.chess.figure.Queen;
import com.gg.ggchess.model.chess.figure.Rook;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

        // Check if current King is targeted and other Piece wants to move
        King king = getCurrentKing();
        if (target != king && isCurrentKingTargeted()) {
            throw new ChessException("King is targeted");
        }

        if (!target.canMove(this, from, to)) {
            throw new ChessException("Figure at position " + from + " can't move to " + to);
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

    public Set<Position> getAllPlayerMoves(Player player) {
        Set<Position> allPositions = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].getPlayer() != player)
                    continue;
                Figure figure = getFigure(i, j);
                allPositions.addAll(figure.possibleMoves(this, new Position(i, j)));
            }
        }
        return allPositions;
    }

    private boolean isCurrentKingTargeted() {
        King king = getCurrentKing();
        Position kingPosition = getKingPosition(king);

        Set<Position> allEnemyMoves = getAllPlayerMoves(player.getEnemy());
        return allEnemyMoves.contains(kingPosition);
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
