package com.digitalsanctum.idea.plugins.buildr.parser;

import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;

import java.util.ArrayList;
import java.util.List;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 10:28:14 AM
 */
public class AvailableTasksParser {
  public static List<BuildrTask> parseTasks(String tasksOutput) {
    if (tasksOutput == null || tasksOutput.length() == 0) throw new IllegalArgumentException("tasksOutput was null!");

    List<BuildrTask> tasks = new ArrayList<BuildrTask>();
    String[] taskLine = tasksOutput.split("[\r\n]+");
    for (String s : taskLine) {
      String[] nameDesc = s.split("#");
      if (nameDesc.length == 2) {
        BuildrTask task = new BuildrTask(nameDesc[0].trim(), nameDesc[1].trim());
        tasks.add(task);
      }
    }

    return tasks;
  }
}
