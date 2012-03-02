package com.digitalsanctum.idea.plugins.buildr.execution;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;

import java.util.List;

/**
 *
 */
public interface BuildrRunSettings {
  String getWorkingDirectory();

  List<String> getTasks();
}
