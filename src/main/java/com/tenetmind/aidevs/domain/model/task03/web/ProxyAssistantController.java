package com.tenetmind.aidevs.domain.model.task03.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(name = "task-mode", havingValue = "false", matchIfMissing = true)
public class ProxyAssistantController {

  @PostMapping("/proxyAssistant")
  public ProxyAssistantResponse handle(@RequestBody ProxyAssistantRequest request) {
    return new ProxyAssistantResponse(request.msg());
  }
}
