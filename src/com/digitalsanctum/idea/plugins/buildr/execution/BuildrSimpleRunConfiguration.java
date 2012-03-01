package com.digitalsanctum.idea.plugins.buildr.execution;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.digitalsanctum.idea.plugins.buildr.BuildrComponent;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationInfoProvider;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: steve
 * Date: Jan 4, 2010
 * Time: 11:35:29 PM
 */
public class BuildrSimpleRunConfiguration extends RunConfigurationBase implements RunProfile {
  private List<String> tasks = new java.util.ArrayList<String>();
  private static final String TASKS_PARENT_ELEMENT_NAME = "tasks";
  private static final String TASKS_TASK_ELEMENT_NAME = "value";

  private final RunConfigurationModule myModule;

  public BuildrSimpleRunConfiguration( String name, Project project, ConfigurationFactory configurationFactory ) {
    super( project, configurationFactory, name );
    myModule = new RunConfigurationModule( project );
  }

  public BuildrRunConfigSettingEditor getConfigurationEditor() {
    return new BuildrRunConfigSettingEditor( getProject().getComponent( BuildrComponent.class ) );
  }

  public JDOMExternalizable createRunnerSettings( ConfigurationInfoProvider provider ) {
    return null;
  }

  @SuppressWarnings( "deprecated" )
  public SettingsEditor<JDOMExternalizable> getRunnerSettingsEditor( ProgramRunner runner ) {
    return null;
  }

  public RunProfileState getState( @NotNull Executor executor, @NotNull ExecutionEnvironment environment )
      throws ExecutionException {
    final BuildrCommandLineState state = new BuildrCommandLineState( environment );
    state.setConsoleBuilder( TextConsoleBuilderFactory.getInstance().createBuilder( getProject() ) );
    return state;
  }

  public void checkConfiguration() throws RuntimeConfigurationException {
    String workingDirectory = Buildr.getWorkingDirectory( getProject(), getModule() );
    if ( workingDirectory == null ) {
      throw new RuntimeConfigurationException( "Cannot find buildfile" );
    }

    GeneralCommandLine commandLine = Buildr.createCommandLine( workingDirectory );
    if (commandLine == null) {
      throw new RuntimeConfigurationException( "Buildr not configure correctly" );
    }
  }

  public List<String> getTasks() {
    return tasks;
  }

  public void setTasks( List<String> tasks ) {
    this.tasks = tasks;
  }

  @Override
  public void writeExternal( Element element ) throws WriteExternalException {
    super.writeExternal( element );

    Element tasks = element.getChild( TASKS_PARENT_ELEMENT_NAME );
    if ( null == tasks ) {
      tasks = new Element( TASKS_PARENT_ELEMENT_NAME );
      element.addContent( tasks );
    }
    else {
      tasks.removeContent();
    }

    for ( String task : this.tasks ) {
      final Element value = new Element( TASKS_TASK_ELEMENT_NAME );
      value.setText( task );
      tasks.addContent( value );
    }

    myModule.writeExternal( element );
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public void readExternal( Element element ) throws InvalidDataException {
    super.readExternal( element );
    this.tasks = new java.util.ArrayList<String>();

    final Element tasks = element.getChild( TASKS_PARENT_ELEMENT_NAME );
    if ( null != tasks ) {
      for ( Element value : ( List<Element> ) tasks.getChildren( TASKS_TASK_ELEMENT_NAME ) ) {
        this.tasks.add( value.getText() );
      }
    }

    myModule.readExternal( element );
  }

  public void setModule( Module aModule ) {
    myModule.setModule( aModule );
  }

  public Module getModule() {
    return myModule.getModule();
  }
}
