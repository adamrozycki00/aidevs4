package com.tenetmind.aidevs.infra.client.brave;

import com.tenetmind.aidevs.domain.model.Task;
import com.tenetmind.aidevs.domain.ports.out.AnswerVerifier;
import com.tenetmind.aidevs.domain.ports.out.ApiCaller;
import com.tenetmind.aidevs.infra.config.Secrets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RequiredArgsConstructor
public class BraveClient implements AnswerVerifier, ApiCaller {

  private final RestClient restClient;
  private final Secrets.Brave secrets;

  @Override
  public AnswerVerifier.Response verify(Task task) {
    var request = AnswerRequest.builder()
        .apikey(secrets.getApiKey())
        .task(task.getVerifierName())
        .answer(task.getAnswer())
        .build();
    log.info("Calling Brave verify API at with request body: {}", request);
    var resp = restClient.post()
        .uri(secrets.getBaseUrl() + "/verify")
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .body(request)
        .retrieve()
        .body(Object.class);

    return new AnswerVerifier.Response(task.getName(), resp);
  }

  @Override
  public String getApiKey() {
    return secrets.getApiKey();
  }

  @Override
  public Object callApi(String uri, Object requestBody) {
    String fullUri = secrets.getBaseUrl() + uri;
    log.info("Calling Brave API at {} with request body: {}", fullUri, requestBody);
    return restClient.post()
        .uri(fullUri)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .body(requestBody)
        .retrieve()
        .body(Object.class);
  }
}
