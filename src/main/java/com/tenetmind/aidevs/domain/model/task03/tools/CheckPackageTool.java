package com.tenetmind.aidevs.domain.model.task03.tools;

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
public class CheckPackageTool implements Toolbox.Tool {

  private static final String URI = "/api/packages";

  private final ApiCaller apiCaller;
  private final ObjectMapper mapper = new ObjectMapper();

  public CheckPackageTool(ApiCaller apiCaller) {
    if (isNull(apiCaller)) {
      throw new IllegalArgumentException("apiCaller must not be null");
    }

    this.apiCaller = apiCaller;
  }

  @Override
  public String execute(Message.ToolCall toolCall) {
    log.info("Executing CheckPackageTool for toolCall {}", toolCall);

    Map<String, Object> args = parseArguments(toolCall.function().arguments());

    Object packageIdRaw = args.get("packageid");

    if (!(packageIdRaw instanceof String packageid) || packageid.isBlank()) {
      throw new IllegalArgumentException("Missing/invalid argument 'packageid'");
    }

    var requestBody = CheckPackageRequest.builder()
        .apikey(apiCaller.getApiKey())
        .action("check")
        .packageid(packageid)
        .build();

    try {
      Object response = apiCaller.callApi(URI, requestBody);
      return mapper.writeValueAsString(response);
    } catch (Exception e) {
      throw new RuntimeException("Error calling check_package API", e);
    }
  }

  public static ChatCompletionRequest.Tool checkPackageTool() {
    var function = ChatCompletionRequest.Function.builder()
        .name("check_package")
        .description("Checks the current status and location of a package by package ID.")
        .parameters(Map.of(
            "type", "object",
            "properties", Map.of(
                "packageid", Map.of(
                    "type", "string",
                    "description", "Package identifier, for example PKG12345678"
                )
            ),
            "required", List.of("packageid"),
            "additionalProperties", false
        ))
        .build();

    return new ChatCompletionRequest.Tool("function", function);
  }

  @Builder
  private record CheckPackageRequest(
      String apikey,
      String action,
      String packageid
  ) {
  }
}
