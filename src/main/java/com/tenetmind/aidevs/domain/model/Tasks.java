package com.tenetmind.aidevs.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class Tasks {

  private final List<Task> tasks;

  public Task getByName(TaskName name) {
    return tasks.stream()
        .filter(hasName(name))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Task with name " + name + " not found"));
  }

  private static @NonNull Predicate<Task> hasName(TaskName name) {
    return task -> task.getName() == name;
  }

  @Getter
  @RequiredArgsConstructor
  public enum TaskName {
    PEOPLE("people"),
    FIND_HIM("findhim");

    private final String verifierName;
  }
}
