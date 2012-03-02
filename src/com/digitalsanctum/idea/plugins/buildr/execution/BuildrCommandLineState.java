package com.digitalsanctum.idea.plugins.buildr.execution;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;

/**
 * User: steve
 * Date: Jan 5, 2010
 * Time: 9:15:45 PM
 */
public class BuildrCommandLineState extends CommandLineState {
  public BuildrCommandLineState( ExecutionEnvironment environment ) {
    super( environment );
  }

  @Override
  protected OSProcessHandler startProcess() throws ExecutionException {
    BuildrRunSettings config = ( BuildrRunSettings ) getRunnerSettings().getRunProfile();
    GeneralCommandLine commandLine = Buildr.createCommandLine( config.getWorkingDirectory(), config.getTasks() );
    if ( commandLine != null ) {
      return new OSProcessHandler( commandLine.createProcess(), commandLine.getCommandLineString() );
    }
    else {
      throw new ExecutionException( "Could not create buildr command line" );
    }
  }
}
