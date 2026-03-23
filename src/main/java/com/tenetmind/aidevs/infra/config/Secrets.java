package com.tenetmind.aidevs.infra.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Secrets {

  private OpenRouter openRouter;
  private Brave brave;

  @Data
  public static class OpenRouter {
    private String apiKey;
  }

  @Data
  public static class Brave {
    private  String apiKey;
    private String baseUrl;
  }
}
