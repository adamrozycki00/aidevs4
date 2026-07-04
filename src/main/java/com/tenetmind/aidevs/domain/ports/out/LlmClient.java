package com.tenetmind.aidevs.domain.ports.out;

import com.tenetmind.aidevs.domain.model.llmchat.ChatCompletionResponse;
import java.util.Map;
import org.jspecify.annotations.NonNull;

public interface LlmClient {

  @NonNull ChatCompletionResponse call(Map<String, Object> requestBody);
}
