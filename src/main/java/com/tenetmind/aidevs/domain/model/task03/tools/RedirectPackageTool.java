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
public class RedirectPackageTool implements Toolbox.Tool {

  private static final String URI = "/api/packages";

  private final ApiCaller apiCaller;
  private final ObjectMapper mapper = new ObjectMapper();

  public RedirectPackageTool(ApiCaller apiCaller) {
    if (isNull(apiCaller)) {
      throw new IllegalArgumentException("apiCaller must not be null");
    }

    this.apiCaller = apiCaller;
  }

  @Override
  public String execute(Message.ToolCall toolCall) {
    log.info("Executing RedirectPackageTool for toolCall {}", toolCall);

    Map<String, Object> args = parseArguments(toolCall.function().arguments());

    Object packageIdRaw = args.get("packageid");
    Object destinationRaw = args.get("destination");
    Object codeRaw = args.get("code");

    if (!(packageIdRaw instanceof String packageid) || packageid.isBlank()) {
      throw new IllegalArgumentException("Missing/invalid argument 'packageid'");
    }

    if (!(destinationRaw instanceof String destination) || destination.isBlank()) {
      throw new IllegalArgumentException("Missing/invalid argument 'destination'");
    }

    if (!(codeRaw instanceof String code) || code.isBlank()) {
      throw new IllegalArgumentException("Missing/invalid argument 'code'");
    }

    var requestBody = RedirectPackageRequest.builder()
        .apikey(apiCaller.getApiKey())
        .action("redirect")
        .packageid(packageid)
        .destination(destination)
        .code(code)
        .build();

    try {
      Object response = apiCaller.callApi(URI, requestBody);
      return mapper.writeValueAsString(response);
    } catch (Exception e) {
      throw new RuntimeException("Error calling redirect_package API", e);
    }
  }

  public static ChatCompletionRequest.Tool redirectPackageTool() {
    var function = ChatCompletionRequest.Function.builder()
        .name("redirect_package")
        .description("Redirects a package to a given power plant destination using a security code.")
        .parameters(Map.of(
            "type", "object",
            "properties", Map.of(
                "packageid", Map.of(
                    "type", "string",
                    "description", "Package identifier, for example PKG12345678"
                ),
                "destination", Map.of(
                    "type", "string",
                    "description", "Destination power plant code, for example PWR3847PL"
                ),
                "code", Map.of(
                    "type", "string",
                    "description", "Security code required to redirect the package"
                )
            ),
            "required", List.of("packageid", "destination", "code"),
            "additionalProperties", false
        ))
        .build();

    return new ChatCompletionRequest.Tool("function", function);
  }

  @Builder
  private record RedirectPackageRequest(
      String apikey,
      String action,
      String packageid,
      String destination,
      String code
  ) {
  }
}
