package com.tenetmind.aidevs.domain.model.llmchat.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.aidevs.domain.model.task01.Task01;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class LlmUtil {

  private static final ObjectMapper mapper = new ObjectMapper();


  public static Object readJsonSchema(String path) {
    URL resource = Task01.class.getClassLoader().getResource(path);
    if (resource == null) {
      throw new IllegalStateException("search.json resource not found on classpath");
    }

    try {
      String readString = Files.readString(Path.of(resource.toURI()));

      try {
        return mapper.readValue(readString, Object.class);
      } catch (Exception e) {
        throw new IllegalStateException("Invalid JSON schema: " + readString, e);
      }

    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("Unable to read search.json", e);
    }
  }
}
