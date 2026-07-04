package com.tenetmind.aidevs.domain.model.task02.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.aidevs.domain.model.Toolbox;
import com.tenetmind.aidevs.domain.model.llmchat.ChatCompletionRequest;
import com.tenetmind.aidevs.domain.model.llmchat.Message;
import com.tenetmind.aidevs.domain.ports.out.ApiCaller;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.tenetmind.aidevs.domain.model.Toolbox.parseArguments;
import static java.util.Objects.isNull;

@Slf4j
public class GetPersonLocationsTool implements Toolbox.Tool {

  private static final String URI = "/api/location";

  private final ApiCaller apiCaller;
  private final ObjectMapper mapper = new ObjectMapper();

  public GetPersonLocationsTool(ApiCaller apiCaller) {
    if (isNull(apiCaller)) {
      throw new IllegalArgumentException("apiCaller must not be null");
    }

    this.apiCaller = apiCaller;
  }

  @Override
  public String execute(Message.ToolCall toolCall) {
    log.info("Executing GetLocationTool for toolCall {}", toolCall);
    Map<String, Object> args = parseArguments(toolCall.function().arguments());

    Object nameRaw = args.get("name");
    Object surnameRaw = args.get("surname");

    if (!(nameRaw instanceof String name) || name.isBlank()) {
      throw new IllegalArgumentException("Missing/invalid argument 'name'");
    }

    if (!(surnameRaw instanceof String surname) || surname.isBlank()) {
      throw new IllegalArgumentException("Missing/invalid argument 'surname'");
    }

    var requestBody = GetPersonLocationsRequest.builder()
        .apikey(apiCaller.getApiKey())
        .name(name)
        .surname(surname)
        .build();

    try {
      Object response = apiCaller.callApi(URI, requestBody);
      return mapper.writeValueAsString(response);
    } catch (Exception e) {
      throw new RuntimeException("Error calling get_person_locations API", e);
    }
  }

  public static ChatCompletionRequest.Tool getPersonLocationsTool() {
    var function = ChatCompletionRequest.Function.builder()
        .name("get_person_locations")
        .description("Returns a JSON array of historical locations for a single person (name + surname) as geographical coordinates: longitude and latitude")
        .parameters(Map.of(
            "type", "object",
            "properties", Map.of(
                "name", Map.of(
                    "type", "string",
                    "description", "First name of the person"
                ),
                "surname", Map.of(
                    "type", "string",
                    "description", "Last name of the person"
                )
            ),
            "required", List.of("name", "surname"),
            "additionalProperties", false
        ))
        .build();
    return new ChatCompletionRequest.Tool("function", function);
  }

  @Builder
  private record GetPersonLocationsRequest(String apikey, String name, String surname) {}
}

