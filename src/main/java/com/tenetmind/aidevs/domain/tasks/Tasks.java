package com.tenetmind.aidevs.domain.tasks;

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

  public enum TaskName {
    PEOPLE
  }
}
