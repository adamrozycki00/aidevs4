package com.tenetmind.aidevs.domain.model.task02.tools;

import com.tenetmind.aidevs.domain.model.Toolbox;
import com.tenetmind.aidevs.domain.model.llmchat.ChatCompletionRequest;
import com.tenetmind.aidevs.domain.model.llmchat.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.tenetmind.aidevs.domain.model.Toolbox.parseArguments;

@Slf4j
public class CalculateDistanceTool implements Toolbox.Tool {

  private static final double EARTH_RADIUS_METERS = 6371000;

  @Override
  public String execute(Message.ToolCall toolCall) {
    log.info("Executing CalculateDistanceTool for toolCall {}", toolCall);
    Map<String, Object> args = parseArguments(toolCall.function().arguments());
    Object latitude1Raw = args.get("latitude_1");
    Object longitude1Raw = args.get("longitude_1");
    Object latitude2Raw = args.get("latitude_2");
    Object longitude2Raw = args.get("longitude_2");

    if (!(latitude1Raw instanceof Double lat1)) {
      throw new IllegalArgumentException("Missing/invalid argument 'latitude_1'");
    }

    if (!(longitude1Raw instanceof Double lon1)) {
      throw new IllegalArgumentException("Missing/invalid argument 'longitude_1'");
    }

    if (!(latitude2Raw instanceof Double lat2)) {
      throw new IllegalArgumentException("Missing/invalid argument 'latitude_2'");
    }

    if (!(longitude2Raw instanceof Double lon2)) {
      throw new IllegalArgumentException("Missing/invalid argument 'longitude_2'");
    }

    double distance = calculateDistanceTool(lat1, lon1, lat2, lon2);
    return String.valueOf(Math.round(distance));
  }

  public static ChatCompletionRequest.Tool calculateDistanceTool() {
    var function = ChatCompletionRequest.Function.builder()
        .name("calculate_distance")
        .description("Calculates distance in meters between two locations using Haversine formula.")
        .parameters(Map.of(
            "type", "object",
            "properties", Map.of(
                "latitude_1", Map.of(
                    "type", "number",
                    "description", "Geographical latitude of first location"
                ),
                "longitude_1", Map.of(
                    "type", "number",
                    "description", "Geographical longitude of first location"
                ),
                "latitude_2", Map.of(
                    "type", "number",
                    "description", "Geographical latitude of second location"
                ),
                "longitude_2", Map.of(
                    "type", "number",
                    "description", "Geographical longitude of second location"
                )
            ),
            "required", List.of("latitude_1", "longitude_1",  "latitude_2", "longitude_2"),
            "additionalProperties", false
        ))
        .build();
    return new ChatCompletionRequest.Tool("function", function);
  }

  /**
   * Calculates distance in meters between two locations using Haversine formula.
   */
  static double calculateDistanceTool(double lat1, double lon1, double lat2, double lon2) {
    double lat1Rad = Math.toRadians(lat1);
    double lat2Rad = Math.toRadians(lat2);
    double deltaLat = Math.toRadians(lat2 - lat1);
    double deltaLon = Math.toRadians(lon2 - lon1);

    double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
        + Math.cos(lat1Rad) * Math.cos(lat2Rad)
        * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS_METERS * c;
  }
}

