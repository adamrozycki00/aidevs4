package com.tenetmind.aidevs.domain.model.task01.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.aidevs.domain.model.llmchat.ChatCompletionResponse;
import com.tenetmind.aidevs.domain.ports.out.LlmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.tenetmind.aidevs.domain.model.llmchat.util.LlmUtil.readJsonSchema;

@Slf4j
@RequiredArgsConstructor
public class LlmTagExtractor implements TagExtractor {

  private static final List<String> TAGS = List.of("IT", "transport", "edukacja", "medycyna", "praca z ludźmi", "praca z pojazdami", "praca fizyczna");
  private static final String JSON_SCHEMA_PATH = "tags.json";
  private static final String PROMPT = """
      Assign related tags to the following text:
      
      <query>
        %s
      </query>
      
      The tags should be related to the content of the text and should be selected from this list: %s.
      """;

  private final LlmClient client;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String model = "openai/gpt-4.1-mini";

  @Override
  public List<String> extract(String text) {
    String prompt = PROMPT.formatted(text, TAGS);
    var responseSchema = readJsonSchema(JSON_SCHEMA_PATH);
    var resp = callLlm(prompt, responseSchema);
    return resp.tags();
  }

  private TagsResponse callLlm(String prompt, Object jsonSchema) {
    var requestBody = client.buildRequest(model, prompt, jsonSchema, null, null, null);
    log.info("Calling OpenRouter with request body: {}", requestBody);

    ChatCompletionResponse response = client.call(requestBody);
    log.info("OpenRouter full response: {}", response);

    String content = response.getContent();
    log.info("Response content: {}", content);

    return toTagsResponse(content);
  }

  private TagsResponse toTagsResponse(String content) {
    try {
      return mapper.readValue(content, TagsResponse.class);
    } catch (Exception e) {
      throw new IllegalStateException(
          "Model output did not contain a valid " + TagsResponse.class.getSimpleName() + ": " + content, e);
    }
  }
}
