package com.tenetmind.ai.aidevs.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
public class AnswerSender {

  private static final String API_KEY = "fddde980-fbe0-446f-98d6-1f2648c44e9b";
  private static final String BRAVE_URL = "https://hub.ag3nts.org/verify";

  private final RestClient restClient;

  public Object send(String task, List<Object> answer) {
    var request = AnswerRequest.builder()
        .apikey(API_KEY)
        .task(task)
        .answer(answer)
        .build();

    return restClient.post()
        .uri(BRAVE_URL)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .body(request)
        .retrieve()
        .body(Object.class);
  }
}
