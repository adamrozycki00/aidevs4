package com.tenetmind.aidevs.domain.model.task02.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.aidevs.domain.model.llmchat.ChatCompletionRequest;
import com.tenetmind.aidevs.domain.model.llmchat.Message;
import com.tenetmind.aidevs.domain.ports.out.ApiCaller;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.tenetmind.aidevs.domain.model.task02.tools.Toolbox.parseArguments;
import static java.util.Objects.isNull;

@Slf4j
public class GetPersonAccessLevelTool implements Toolbox.Tool {

  private static final String URI = "/api/accesslevel";

  private final ApiCaller apiCaller;
  private final ObjectMapper mapper = new ObjectMapper();

  public GetPersonAccessLevelTool(ApiCaller apiCaller) {
    if (isNull(apiCaller)) {
      throw new IllegalArgumentException("apiCaller must not be null");
    }

    this.apiCaller = apiCaller;
  }

  @Override
  public String execute(Message.ToolCall toolCall) {
    log.info("Executing GetPersonAccessLevelTool for toolCall {}", toolCall);
    Map<String, Object> args = parseArguments(toolCall.function().arguments());

    Object nameRaw = args.get("name");
    Object surnameRaw = args.get("surname");
    Object birthYearRaw = args.get("birthYear");

    if (!(nameRaw instanceof String name) || name.isBlank()) {
      throw new IllegalArgumentException("Missing/invalid argument 'name'");
    }

    if (!(surnameRaw instanceof String surname) || surname.isBlank()) {
      throw new IllegalArgumentException("Missing/invalid argument 'surname'");
    }

    if (!(birthYearRaw instanceof Integer birthYear)) {
      throw new IllegalArgumentException("Missing/invalid argument 'birthYear'");
    }

    var requestBody = GetPersonAccessLevelRequest.builder()
        .apikey(apiCaller.getApiKey())
        .name(name)
        .surname(surname)
        .birthYear(birthYear)
        .build();

    try {
      Object responseObj = apiCaller.callApi(URI, requestBody);
      var response = mapper.convertValue(responseObj, GetPersonAccessLevelResponse.class);
      return response.accessLevel().toString();
    } catch (Exception e) {
      throw new RuntimeException("Error calling get_person_access_level API", e);
    }
  }

  public static ChatCompletionRequest.Tool getPersonAccessLevelTool() {
    var function = ChatCompletionRequest.Function.builder()
        .name("get_person_access_level")
        .description("Returns person's access level to power plants: integer 1-10")
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
                ),
                "birthYear", Map.of(
                    "type", "number",
                    "description", "Year of birth of the person"
                )
            ),
            "required", List.of("name", "surname", "birthYear"),
            "additionalProperties", false
        ))
        .build();
    return new ChatCompletionRequest.Tool("function", function);
  }

  @Builder
  private record GetPersonAccessLevelRequest(String apikey, String name, String surname, Integer birthYear) {}

  private record GetPersonAccessLevelResponse(String name, String surname, Integer accessLevel) {}
}

