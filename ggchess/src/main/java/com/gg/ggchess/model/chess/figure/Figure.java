package com.gg.ggchess.model.chess.figure;


import com.gg.ggchess.model.chess.Board;
import com.gg.ggchess.model.chess.Player;
import com.gg.ggchess.model.chess.Position;

import java.util.Set;

public abstract class Figure {
    public static final String PAWN = "P";
    public static final String ROOK = "R";
    public static final String KNIGHT = "N";
    public static final String BISHOP = "B";
    public static final String QUEEN = "Q";
    public static final String KING = "K";

    private final String name;
    private final Player player;

    public Figure(String figureName, Player player) {
        this.player = player;
        this.name = player.getColor() + figureName;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean canMove(Board board, Position from, Position to) {
        Set<Position> possibleMoves = possibleMoves(board, from);
        return possibleMoves.contains(to);
    }

    public abstract Set<Position> possibleMoves(Board board, Position from);

}

