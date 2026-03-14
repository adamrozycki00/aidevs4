package com.tenetmind.ai.aidevs.sender;

import com.tenetmind.ai.aidevs.config.Secrets;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
public class AnswerSender {

  private final RestClient restClient;
  private final Secrets.Brave secrets;

  public Object send(String task, List<Object> answer) {
    var request = AnswerRequest.builder()
        .apikey(secrets.getApiKey())
        .task(task)
        .answer(answer)
        .build();

    return restClient.post()
        .uri(secrets.getUrl())
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .body(request)
        .retrieve()
        .body(Object.class);
  }
}
