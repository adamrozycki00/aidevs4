package com.tenetmind.aidevs.infra.client.brave;

import com.tenetmind.aidevs.domain.ports.AnswerVerifier;
import com.tenetmind.aidevs.domain.model.Task;
import com.tenetmind.aidevs.infra.config.Secrets;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
public class BraveClient implements AnswerVerifier {

  private final RestClient restClient;
  private final Secrets.Brave secrets;

  @Override
  public AnswerVerifier.Response verify(Task task) {
    var request = AnswerRequest.builder()
        .apikey(secrets.getApiKey())
        .task(task.getSenderName())
        .answer(task.getAnswer())
        .build();

    var resp = restClient.post()
        .uri(secrets.getUrl())
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .body(request)
        .retrieve()
        .body(Object.class);

    return new AnswerVerifier.Response(task.getName(), resp);
  }
}
