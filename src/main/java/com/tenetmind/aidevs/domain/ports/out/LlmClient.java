package com.tenetmind.aidevs.domain.ports.out;

import com.tenetmind.aidevs.domain.model.llmchat.ChatCompletionRequest;
import com.tenetmind.aidevs.domain.model.llmchat.ChatCompletionResponse;
import com.tenetmind.aidevs.domain.model.llmchat.Message;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;

public interface LlmClient {

  @NonNull Map<String, Object> buildRequest(String model, String prompt, Object jsonSchema, List<Message.ToolCall> toolCalls,
                                            String toolCallId, List<ChatCompletionRequest.Tool> tools);

  @NonNull ChatCompletionResponse call(Map<String, Object> requestBody);
}
