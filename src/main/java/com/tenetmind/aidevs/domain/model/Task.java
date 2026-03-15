package com.tenetmind.aidevs.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Task {

  protected final Tasks.TaskName name;
  protected Object answer;

  public abstract void solve();

  public String getSenderName() {
    return name.name().toLowerCase();
  }
}
