package com.gg.ggchess.service;

import com.gg.ggchess.exception.ChessException;
import com.gg.ggchess.model.chess.Board;
import com.gg.ggchess.model.request.MoveRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
@Slf4j
public class ChessService {

    private Board board = null;

    public Map<String, String> initialize() {
        board = new Board();
        return board.initialize();
    }

    public Map<String, String> move(MoveRequest request) {
        try {
            board.moveFigure(request.from(), request.to());
        } catch (ChessException e) {
            log.error("ChessException thrown: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return board.getBoard();
    }
}
