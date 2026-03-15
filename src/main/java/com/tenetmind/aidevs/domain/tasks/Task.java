package com.tenetmind.aidevs.domain.tasks;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Task {

  @Getter
  protected final Tasks.TaskName name;

  public abstract Object solve();

  public String getSenderName() {
    return name.name().toLowerCase();
  }
}
