package com.digitalsanctum.idea.plugins.buildr.execution;

import java.util.List;

/**
 *
 */
public interface BuildrRunSettings {
  String getWorkingDirectory();

  List<String> getTasks();
}
