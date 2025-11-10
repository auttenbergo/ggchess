package com.gg.ggchess.model.chess;


import com.gg.ggchess.exception.ChessException;
import com.gg.ggchess.model.chess.figure.Bishop;
import com.gg.ggchess.model.chess.figure.Figure;
import com.gg.ggchess.model.chess.figure.King;
import com.gg.ggchess.model.chess.figure.Knight;
import com.gg.ggchess.model.chess.figure.Pawn;
import com.gg.ggchess.model.chess.figure.Queen;
import com.gg.ggchess.model.chess.figure.Rook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Board {

    private List<FigureMove> history;
    public static final int SIZE = 8;
    private Figure[][] board;
    private Player player;
    private King whiteKing;
    private King blackKing;

    public Board() {
        board = new Figure[SIZE][SIZE];
        history = new ArrayList<>();
    }

    public void initializeCustom(Map<String, String> boardMap) throws ChessException {
        if (!boardMap.containsValue("wK") || !boardMap.containsValue("bK"))
            throw new ChessException("Board must contain white and black king");

        history = new ArrayList<>();

        board = new Figure[SIZE][SIZE];
        for (Map.Entry<String, String> entry : boardMap.entrySet()) {
            String position = entry.getKey();
            String figure = entry.getValue();

            int x = SIZE - (position.charAt(1) - '0');
            int y = position.charAt(0) - 'a';

            Player player = Character.toLowerCase(figure.charAt(0)) == 'w' ? Player.WHITE : Player.BLACK;
            Character figureC = Character.toUpperCase(figure.charAt(1));

            Figure figureParsed = Figure.parseFigure(figureC, player);

            if (figureParsed instanceof King) {
                if (player == Player.WHITE) {
                    whiteKing = (King) figureParsed;
                } else {
                    blackKing = (King) figureParsed;
                }
            }

            board[x][y] = figureParsed;
        }
        player = Player.WHITE;
    }

    public Map<String, String> initialize() {
        board = new Figure[SIZE][SIZE];
        history = new ArrayList<>();
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

    public void moveFigure(String from, String to, Map<String, String> additionalProperties) throws ChessException {
        Position fromPosition = new Position(from);
        Position toPosition = new Position(to);
        moveFigure(fromPosition, toPosition, additionalProperties);
    }

    public void moveFigure(Position from, Position to, Map<String, String> additionalProperties) throws ChessException {
        Figure target = getFigure(from);
        if (target == null) {
            throw new ChessException("No figure at position " + from);
        }
        if (target.getPlayer() != player) {
            throw new ChessException("Figure at position " + from + " is not yours");
        }

        Player winner = getWinner();
        if (winner != null) {
            throw new ChessException("Game is over, winner is " + winner);
        }

        if (isDraw()) {
            throw new ChessException("Game is draw");
        }

        if (!target.canMove(this, from, to)) {
            throw new ChessException("Figure at position " + from + " can't move to " + to);
        }

        // Check if current King is targeted and other Piece wants to move
        // If piece moves to kill the enemy, check if King is still targeted
        King king = getCurrentKing();
        if (target != king && isKingTargeted(player)) {
            if (isKingTargetedAfterMove(player, from, to)) {
                throw new ChessException("King is targeted");
            }

            board[to.getX()][to.getY()] = target;
            board[from.getX()][from.getY()] = null;
            player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
            history.add(new FigureMove(from, to, target));
            return;
        }

        // Check if after the move King is targeted
        if (isKingTargetedAfterMove(player, from, to)) {
            throw new ChessException("King will be targeted after the move");
        }

        if (board[to.getX()][to.getY()] != null) {

            Figure conflictFigure = getFigure(to);
            Player enemyPlayer = player == Player.WHITE ? Player.BLACK : Player.WHITE;
            if (conflictFigure.getPlayer() == enemyPlayer) {
                board[to.getX()][to.getY()] = target;
                board[from.getX()][from.getY()] = null;
                player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
                history.add(new FigureMove(from, to, target));
                return;
            }
        }

        if (target instanceof Pawn && (to.getX() == 0 || to.getX() == SIZE - 1)) {
            if (additionalProperties == null || !additionalProperties.containsKey("promoteTo")) {
                throw new ChessException("Promotion must be passed for pawn at position " + from);
            }
            Character promoteTo = Objects.requireNonNull(additionalProperties.get("promoteTo")).charAt(0);
            Figure promotedFigure = Figure.parseFigure(promoteTo, player);

            if (!Figure.isValidPromotionFigure(promotedFigure)) {
                throw new ChessException("Invalid promotion figure: " + promoteTo);
            }

            board[to.getX()][to.getY()] = promotedFigure;
            board[from.getX()][from.getY()] = null;
            player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
            history.add(new FigureMove(from, to, target));
            return;
        }

        if (target instanceof Pawn) {
            if (player == Player.WHITE) {
                if (from.getX() == 3) {
                    // En passant left
                    if (to.getY() == from.getY() - 1 && from.getY() >= 1) {
                        if (board[from.getX()][from.getY() - 1] instanceof Pawn) {
                            Pawn enemyPawn = (Pawn) board[from.getX()][from.getY() - 1];
                            if (enemyPawn.getPlayer() == Player.BLACK && lastMoveWas(enemyPawn, new Position(1, from.getY() - 1), new Position(3, from.getY() - 1))) {
                                board[from.getX()][from.getY() - 1] = null;
                                board[to.getX()][to.getY()] = target;
                                board[from.getX()][from.getY()] = null;
                                player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
                                history.add(new FigureMove(from, to, target));
                                return;
                            }
                        }
                    }
                    // En passant right
                    if (to.getY() == from.getY() + 1 && from.getY() < SIZE - 1) {
                        if (board[from.getX()][from.getY() + 1] instanceof Pawn) {
                            Pawn enemyPawn = (Pawn) board[from.getX()][from.getY() + 1];
                            if (enemyPawn.getPlayer() == Player.BLACK && lastMoveWas(enemyPawn, new Position(1, from.getY() + 1), new Position(3, from.getY() + 1))) {
                                board[from.getX()][from.getY() + 1] = null;
                                board[to.getX()][to.getY()] = target;
                                board[from.getX()][from.getY()] = null;
                                player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
                                history.add(new FigureMove(from, to, target));
                                return;
                            }
                        }
                    }
                }
            }

            if (player == Player.BLACK) {
                if (from.getX() == 4) {
                    // En passant left
                    if (to.getY() == from.getY() - 1 && from.getY() >= 1) {
                        if (board[from.getX()][from.getY() - 1] instanceof Pawn) {
                            Pawn enemyPawn = (Pawn) board[from.getX()][from.getY() - 1];
                            if (enemyPawn.getPlayer() == Player.WHITE && lastMoveWas(enemyPawn, new Position(6, from.getY() - 1), new Position(4, from.getY() - 1))) {
                                board[from.getX()][from.getY() - 1] = null;
                                board[to.getX()][to.getY()] = target;
                                board[from.getX()][from.getY()] = null;
                                player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
                                history.add(new FigureMove(from, to, target));
                                return;
                            }
                        }
                    }
                    // En passant right
                    if (to.getY() == from.getY() + 1 && from.getY() < SIZE - 1) {
                        if (board[from.getX()][from.getY() + 1] instanceof Pawn) {
                            Pawn enemyPawn = (Pawn) board[from.getX()][from.getY() + 1];
                            if (enemyPawn.getPlayer() == Player.WHITE && lastMoveWas(enemyPawn, new Position(6, from.getY() + 1), new Position(4, from.getY() + 1))) {
                                board[from.getX()][from.getY() + 1] = null;
                                board[to.getX()][to.getY()] = target;
                                board[from.getX()][from.getY()] = null;
                                player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
                                history.add(new FigureMove(from, to, target));
                                return;
                            }
                        }
                    }
                }
            }
        }

        if (target instanceof King) {
            if (player == Player.WHITE) {
                if (from.getX() == SIZE - 1 && from.getY() == 4) {
                    if (to.getX() == SIZE - 1 && to.getY() == 1) {
                        board[from.getX()][from.getY()] = null;
                        board[to.getX()][to.getY()] = target;
                        board[SIZE - 1][2] = board[SIZE - 1][0];
                        board[SIZE - 1][0] = null;
                        player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
                        history.add(new FigureMove(from, to, target));
                        return;
                    } else if (to.getX() == SIZE - 1 && to.getY() == SIZE - 2) {
                        board[from.getX()][from.getY()] = null;
                        board[to.getX()][to.getY()] = target;
                        board[SIZE - 1][5] = board[SIZE - 1][7];
                        board[SIZE - 1][7] = null;
                        player = Player.BLACK;
                        history.add(new FigureMove(from, to, target));
                        return;
                    }
                }
            }
            if (player == Player.BLACK) {
                if (from.getX() == 0 && from.getY() == 4) {
                    if (to.getX() == 0 && to.getY() == 1) {
                        board[from.getX()][from.getY()] = null;
                        board[to.getX()][to.getY()] = target;
                        board[0][2] = board[0][0];
                        board[0][0] = null;
                        player = Player.WHITE;
                        history.add(new FigureMove(from, to, target));
                        return;
                    } else if (to.getX() == 0 && to.getY() == SIZE - 2) {
                        board[from.getX()][from.getY()] = null;
                        board[to.getX()][to.getY()] = target;
                        board[0][5] = board[0][7];
                        board[0][7] = null;
                        player = Player.WHITE;
                        history.add(new FigureMove(from, to, target));
                        return;
                    }
                }
            }
        }

        board[to.getX()][to.getY()] = target;
        board[from.getX()][from.getY()] = null;
        player = (player == Player.WHITE) ? Player.BLACK : Player.WHITE;
        history.add(new FigureMove(from, to, target));
    }

    public boolean figureHasMoved(Figure figure) {
        return history.stream().anyMatch(figureMove -> figureMove.getFigure() == figure);
    }

    public boolean lastMoveWas(Figure figure, Position from, Position to) {
        if (history.isEmpty())
            return false;
        FigureMove figureMove = history.get(history.size() - 1);
        return figureMove.getFrom().equals(from)
                && figureMove.getTo().equals(to) &&
                figureMove.getFigure().equals(figure);
    }

    public boolean canPromote(String from, String to, Map<String, String> stringStringMap) {
        Position fromPosition = new Position(from);
        Position toPosition = new Position(to);

        Figure target = getFigure(fromPosition);
        if (target == null) {
            return false;
        }
        if (!(target instanceof Pawn)) {
            return false;
        }

        Pawn pawn = (Pawn) target;
        if (pawn.getPlayer() != player) {
            return false;
        }
        return toPosition.getX() == 0 || toPosition.getX() == SIZE - 1;
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

    public Set<FigureMove> getAllPlayerPossibleMoves(Player player) {
        Set<FigureMove> allPositions = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null || board[i][j].getPlayer() != player)
                    continue;
                Figure figure = getFigure(i, j);

                Position position = new Position(i, j);

                allPositions.addAll(figure.possibleMoves(this, position));
            }
        }
        return allPositions;
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

    private boolean isKingTargeted(Player player) {
        King king = player == Player.WHITE ? whiteKing : blackKing;
        Position kingPosition = getKingPosition(king);

        Set<FigureMove> allEnemyMoves = getAllPlayerAttackMoves(player.getEnemy());
        return allEnemyMoves.stream()
                .map(FigureMove::getTo)
                .collect(Collectors.toSet())
                .contains(kingPosition);
    }

    private boolean isKingTargetedAfterMove(Player player, Position from, Position to) {
        Figure previousFrom = getFigure(from);
        Figure previousTo = getFigure(to);

        try {
            board[from.getX()][from.getY()] = null;
            board[to.getX()][to.getY()] = previousFrom;


            King king = player == Player.WHITE ? whiteKing : blackKing;
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


    public boolean isWinner(Player player) {
        King king = player == Player.WHITE ? blackKing : whiteKing;
        Position kingPosition = getKingPosition(king);

        if (!isKingTargeted(player.getEnemy())) {
            return false;
        }

        Set<FigureMove> kingPossibleMoves = king.possibleMoves(this, kingPosition);

        if (!kingPossibleMoves.isEmpty()) {
            return false;
        }

        Set<FigureMove> allKillerMoves = getAllPlayerAttackMoves(player).stream()
                .filter(figureMove -> figureMove.getTo().equals(kingPosition))
                .collect(Collectors.toSet());

        Set<Position> allKillerMovePositions = allKillerMoves.stream()
                .map(FigureMove::getFrom)
                .collect(Collectors.toSet());

        Set<FigureMove> allDefensiveMoves = getAllPlayerAttackMoves(player.getEnemy()).stream()
                .filter(figureMove -> allKillerMovePositions.contains(figureMove.getTo()))
                .collect(Collectors.toSet());

        if (allDefensiveMoves.isEmpty()) {
            return true;
        }

        for (FigureMove defensiveMove : allDefensiveMoves) {
            if (!isKingTargetedAfterMove(player, defensiveMove.getFrom(), defensiveMove.getTo())) {
                return false;
            }
        }

        return true;
    }

    public Player getWinner() {
        if (isWinner(Player.WHITE)) {
            return Player.WHITE;
        }
        if (isWinner(Player.BLACK)) {
            return Player.BLACK;
        }
        return null;
    }

    public boolean isDraw() {
        return !isKingTargeted(player) && getAllPlayerPossibleMoves(player).isEmpty();
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
