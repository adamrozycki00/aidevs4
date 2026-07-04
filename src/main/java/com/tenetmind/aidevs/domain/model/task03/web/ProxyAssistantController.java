package com.tenetmind.aidevs.domain.model.task03.web;

import com.tenetmind.aidevs.domain.model.task03.Task03;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(name = "task-mode", havingValue = "false", matchIfMissing = true)
public class ProxyAssistantController {

  private final Task03 task03;

  public ProxyAssistantController(Task03 task03) {
    this.task03 = task03;
  }

  @GetMapping("/proxyAssistant")
  public ResponseEntity<Void> healthCheck() {
    return ResponseEntity.ok().build();
  }

  @PostMapping("/proxyAssistant")
  public ProxyAssistantResponse handle(@RequestBody ProxyAssistantRequest request) {
    String reply = task03.respond(request.sessionID(), request.msg());
    return new ProxyAssistantResponse(reply);
  }
}
