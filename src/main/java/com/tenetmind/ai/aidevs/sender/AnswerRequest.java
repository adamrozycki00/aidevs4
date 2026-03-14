package com.tenetmind.ai.aidevs.sender;

import lombok.Builder;

import java.util.List;

@Builder
public record AnswerRequest(String apikey, String task, List<Object> answer) {}
