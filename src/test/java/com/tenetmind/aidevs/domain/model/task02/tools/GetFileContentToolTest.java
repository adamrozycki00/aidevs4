package com.tenetmind.aidevs.domain.model.task02.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GetFileContentToolTest {

  @TempDir
  Path tmp;

  @Test
  void readsFileFromBaseDir() throws Exception {
    Path base = tmp.resolve("base");
    Files.createDirectories(base);
    Files.writeString(base.resolve("file.json"), "{\"ok\":true}");

    String content = GetFileContentTool.getFileContentFromDir(base.toString(), "file.json");
    assertEquals("{\"ok\":true}", content);
  }

  @Test
  void blocksPathTraversal() throws Exception {
    Path base = tmp.resolve("base");
    Files.createDirectories(base);

    var ex = assertThrows(IllegalArgumentException.class,
        () -> GetFileContentTool.getFileContentFromDir(base.toString(), "../secret.txt"));
    assertTrue(ex.getMessage().toLowerCase().contains("traversal"));
  }

  @Test
  void missingFileThrows() throws Exception {
    Path base = tmp.resolve("base");
    Files.createDirectories(base);

    var ex = assertThrows(IllegalStateException.class,
        () -> GetFileContentTool.getFileContentFromDir(base.toString(), "missing.json"));
    assertTrue(ex.getMessage().toLowerCase().contains("not found"));
  }
}

