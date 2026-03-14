package com.tenetmind.ai.aidevs.sender;

import lombok.Builder;

@Builder
public record AnswerRequest(String apikey, String task, Object answer) {
}
