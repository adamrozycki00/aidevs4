package com.tenetmind.aidevs.infra.client.brave;

import lombok.Builder;

@Builder
public record AnswerRequest(String apikey, String task, Object answer) {
}
