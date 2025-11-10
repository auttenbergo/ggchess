package com.gg.ggchess.model.request;

import java.util.Map;

public record MoveRequest(String from, String to, Map<String, String> additionalProperties) {
}
