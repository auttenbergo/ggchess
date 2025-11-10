package com.gg.ggchess.model.chess;

import com.gg.ggchess.model.chess.figure.Figure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FigureMove {
    private final Position from;
    private final Position to;
    private final Figure figure;
}
