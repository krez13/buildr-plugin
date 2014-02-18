package com.digitalsanctum.idea.plugins.buildr.execution;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import org.jetbrains.annotations.NotNull;

public class BuildrCommandLineState extends CommandLineState {
  public BuildrCommandLineState( ExecutionEnvironment environment ) {
    super( environment );
  }

  @NotNull
  @Override
  protected OSProcessHandler startProcess() throws ExecutionException {
    BuildrRunProfile config = ( BuildrRunProfile ) getEnvironment().getRunProfile();
    GeneralCommandLine commandLine = Buildr.createCommandLine( config.getWorkingDirectory(), config.getTasks() );
    if ( commandLine != null ) {
      return new OSProcessHandler( commandLine.createProcess(), commandLine.getCommandLineString() );
    }
    else {
      throw new ExecutionException( "Could not create buildr command line" );
    }
  }
}
