package com.digitalsanctum.idea.plugins.buildr.execution;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

/**
 *
 */
public class BuildrRunProfile implements RunProfile, BuildrRunSettings {
  private final String workingDirectory;
  private final List<String> tasks;

  public BuildrRunProfile( String aWorkingDirectory, List<String> aTasks ) {
    workingDirectory = aWorkingDirectory;
    tasks = aTasks;
  }

  public void checkConfiguration() throws RuntimeConfigurationException {
  }

  public RunProfileState getState( @NotNull Executor aExecutor, @NotNull ExecutionEnvironment environment ) throws ExecutionException {
    final BuildrCommandLineState state = new BuildrCommandLineState( environment );
    Project project = environment.getProject();
    if ( project != null ) {
      state.setConsoleBuilder( TextConsoleBuilderFactory.getInstance().createBuilder( project ) );
    } else {
      throw new ExecutionException( "Project not available" );
    }
    return state;
  }

  public String getName() {
    return StringUtil.join( tasks, ", " );
  }

  public Icon getIcon() {
    return Buildr.BUILDR_16;
  }

  public String getWorkingDirectory() {
    return workingDirectory;
  }

  public List<String> getTasks() {
    return tasks;
  }
}
