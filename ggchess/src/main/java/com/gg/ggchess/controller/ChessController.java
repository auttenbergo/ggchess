package com.gg.ggchess.controller;

import com.gg.ggchess.model.request.MoveRequest;
import com.gg.ggchess.service.ChessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chess")
@RequiredArgsConstructor
public class ChessController {

    private final ChessService chessService;

    @PostMapping("/initialize")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> initialize() {
        return chessService.initialize();
    }

    @PostMapping("/move")
    @ResponseStatus(HttpStatus.OK)
    public Map<String,String> move(@Valid @RequestBody MoveRequest request){
        return chessService.move(request);
    }

}
