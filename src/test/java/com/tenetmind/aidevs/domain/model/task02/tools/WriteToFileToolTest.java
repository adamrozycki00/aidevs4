package com.tenetmind.aidevs.domain.model.task02.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WriteToFileToolTest {

  @TempDir
  Path tmp;

  @Test
  void writesFileToBaseDir() throws Exception {
    Path base = tmp.resolve("base");
    Files.createDirectories(base);

    WriteToFileTool.writeToFileInDir(base.toString(), "out/result.txt", "hello");

    assertEquals("hello", Files.readString(base.resolve("out/result.txt")));
  }

  @Test
  void blocksPathTraversal() throws Exception {
    Path base = tmp.resolve("base");
    Files.createDirectories(base);

    var ex = assertThrows(IllegalArgumentException.class,
        () -> WriteToFileTool.writeToFileInDir(base.toString(), "../secret.txt", "x"));
    assertTrue(ex.getMessage().toLowerCase().contains("traversal"));
  }
}

