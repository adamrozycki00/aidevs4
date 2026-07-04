package com.tenetmind.aidevs.domain.model.task02;

import static com.tenetmind.aidevs.domain.model.Tasks.TaskName.FIND_HIM;
import static com.tenetmind.aidevs.domain.model.llmchat.util.LlmUtil.readJsonSchema;
import static com.tenetmind.aidevs.domain.model.task02.tools.CalculateDistanceTool.calculateDistanceTool;
import static com.tenetmind.aidevs.domain.model.task02.tools.GetFileContentTool.getFileContentTool;
import static com.tenetmind.aidevs.domain.model.task02.tools.GetPersonAccessLevelTool.getPersonAccessLevelTool;
import static com.tenetmind.aidevs.domain.model.task02.tools.GetPersonLocationsTool.getPersonLocationsTool;
import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.aidevs.domain.model.Task;
import com.tenetmind.aidevs.domain.model.llmchat.ChatCompletionRequest;
import com.tenetmind.aidevs.domain.model.llmchat.Message;
import com.tenetmind.aidevs.domain.model.task02.tools.Toolbox;
import com.tenetmind.aidevs.domain.ports.out.LlmClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Task02 extends Task {

  private final Toolbox toolbox;
  private final LlmClient client;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String model = "openai/gpt-5.4";
  private final double temperature = 0.2;
  private final int turnLimit;

  public Task02(Toolbox tools, LlmClient client, int turnLimit) {
    super(FIND_HIM);
    this.toolbox = tools;
    this.client = client;
    this.turnLimit = turnLimit;
  }

  @Override
  public void solve() {
    List<Message> conversation = new ArrayList<>();
    String prompt = """
           Below is a task for you to solve.
           To solve it, you can use tool calls as needed.
       
           <task>
              1. Fetch the list of power plants from `power_plants_with_coordinates.json`.
              2. Fetch the list of people from `people_selected.json`.
              3. For each person in people_selected, retrieve the list of the locations they were seen recently. For this, you must use get_person_locations tool. Each person may come from the same city, but they moved around and were seen in different locations, and the tool get_person_locations will return the list of these locations for that person.
              4. Select any person that were seen very close to any of the power plants. Very close means less than 100 m. Use calculate_distance tool to calculate the distance between the locations of a person and the coordinates of power plants. You don't need to calculate distance for every location and every power plant; instead, you should stop when you find a person's location in proximity of a power plant. If a location is very close to one of the plants, you have a candidate.
              5. For the person selected in step 4, get their access level to power plants using get_person_access_level tool.
              6. Finally, return a structured response containing: that person's name, surname, and access level together with that close power plant's code and the closest distance between the person and power plant.
           </task>
        
           <hints>
              Your responses should be either tool calls or the final answer.
              Do not describe in detail your reasoning.
           </hints>
        """;

    var message = Message.builder()
        .role("user")
        .content(prompt)
        .build();
    conversation.add(message);
    var tools = List.of(getFileContentTool(), getPersonLocationsTool(), calculateDistanceTool(), getPersonAccessLevelTool());
    var structuredResponse = new ChatCompletionRequest.ResponseFormat(
        new ChatCompletionRequest.JsonSchema(
            "structured_response",
            true,
            readJsonSchema("closestPersonSchema.json")
        )
    );
    var request = ChatCompletionRequest.builder()
        .model(model)
        .messages(conversation)
        .temperature(temperature)
        .tools(tools)
        .responseFormat(structuredResponse)
        .build();

    Map<String, Object> requestBody = mapper.convertValue(request, new TypeReference<>() {
    });

    var response = client.call(requestBody);
    var assistantMsg = response.getFirstMessage();

    if (assistantMsg == null) {
      log.warn("Assistant message was null");
      return;
    }

    conversation.add(assistantMsg);
    int agentTurns = 0;

    while (nonNull(assistantMsg.toolCalls()) && agentTurns < turnLimit) {
      agentTurns++;
      log.info("Agent turn {}. Processing tool calls...", agentTurns);
      var toolCalls = assistantMsg.toolCalls();
      log.info("Tool calls: {}", toolCalls);

      for (var toolCall : toolCalls) {
        log.info("Executing tool call: {}", toolCall);
        String toolCallResult = toolbox.execute(toolCall);
        log.info("Tool call result: {}", toolCallResult);
        conversation.add(Message.toolResult(toolCall.id(), toolCallResult));
      }

      request = ChatCompletionRequest.builder()
          .model(model)
          .messages(conversation)
          .tools(tools)
          .responseFormat(structuredResponse)
          .build();

      requestBody = mapper.convertValue(request, new TypeReference<>() {
      });
      response = client.call(requestBody);
      assistantMsg = response.getFirstMessage();

      if (assistantMsg == null) {
        log.warn("Assistant message was null");
        return;
      }

      conversation.add(assistantMsg);
    }

    String content = assistantMsg.content();
    log.info("agentTurns: {}, final assistant message content: {}", agentTurns, content);

    answer = toAnswerRequest(content);
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record AnswerRequest(String name, String surname, int accessLevel, String powerPlant) {
  }

  private AnswerRequest toAnswerRequest(String content) {
    try {
      return mapper.readValue(content, AnswerRequest.class);
    } catch (Exception e) {
      throw new IllegalStateException(
          "Model output did not contain a valid " + AnswerRequest.class.getSimpleName() + ": " + content, e);
    }
  }
}
