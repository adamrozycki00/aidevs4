package com.tenetmind.aidevs.domain.model.task02.tools;

import com.tenetmind.aidevs.domain.ports.out.ApiCaller;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ToolFactory {

  private final ApiCaller apiCaller;

  public Toolbox.Tool createGetFileContentTool() {
    return new GetFileContentTool();
  }

  public Toolbox.Tool createWriteToFileTool() {
    return new WriteToFileTool();
  }

  public Toolbox.Tool createGetPersonLocationsTool() {
    return new GetPersonLocationsTool(apiCaller);
  }

  public Toolbox.Tool createCalculateDistanceTool() {
    return new CalculateDistanceTool();
  }

  public Toolbox.Tool createGetPersonAccessLevel() {
    return new GetPersonAccessLevelTool(apiCaller);
  }
}
