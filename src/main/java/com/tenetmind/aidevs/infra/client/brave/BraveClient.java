package com.tenetmind.aidevs.infra.client.brave;

import com.tenetmind.aidevs.domain.ports.AnswerSender;
import com.tenetmind.aidevs.infra.config.Secrets;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
public class BraveClient implements AnswerSender {

  private final RestClient restClient;
  private final Secrets.Brave secrets;

  @Override
  public Object send(String task, Object answer) {
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
