package com.digitalsanctum.idea.plugins.buildr;

import com.digitalsanctum.idea.plugins.buildr.execution.BuildrConfigurationType;
import com.digitalsanctum.idea.plugins.buildr.execution.BuildrSimpleRunConfiguration;
import com.digitalsanctum.idea.plugins.buildr.execution.BuildrTasksPane;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;
import com.digitalsanctum.idea.plugins.buildr.parser.AvailableTasksParser;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Output;
import com.intellij.execution.OutputListener;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.RunnerRegistry;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.digitalsanctum.idea.plugins.buildr.Buildr.*;

/**
 * User: sblundy
 * Date: Jan 3, 2010
 * Time: 9:13:34 PM
 */
public class BuildrComponent implements ProjectComponent {
  private static final Logger LOG = Logger.getInstance( BuildrComponent.class );

  private Project project;
  private ToolWindow buildrToolWindow;
  private BuildrTasksPane buildrTasksPane;
  private ContentFactory contentFactory;

  public BuildrComponent( Project project ) {
    this.project = project;
  }

  public void projectOpened() {
    ToolWindowManager toolWindowManager = ToolWindowManager.getInstance( this.project );
    initBuildrToolWindow( toolWindowManager );
  }

  public void projectClosed() {
    ToolWindowManager toolWindowManager = ToolWindowManager.getInstance( project );
    toolWindowManager.unregisterToolWindow( BUILDR_TOOL_WINDOW_ID );
  }

  @NotNull
  public String getComponentName() {
    return "BuildrTaskListComponent";
  }

  public void initComponent() {
  }

  public void disposeComponent() {
  }

  public void refreshTaskList() {
    this.buildrTasksPane.refreshTaskList();
  }

  public void runSelectedTask() {
    runTask( this.buildrTasksPane.getModule(), this.buildrTasksPane.getCommand() );
  }

  public boolean isTaskSelected() {
    return null != this.buildrTasksPane && this.buildrTasksPane.isTaskSelected();
  }

  private ContentFactory getContentFactory() {
    if ( contentFactory == null ) {
      contentFactory = ContentFactory.SERVICE.getInstance();
    }
    return contentFactory;
  }

  private void initBuildrToolWindow( ToolWindowManager toolWindowManager ) {
    if ( buildrToolWindow == null ) {
      buildrToolWindow = toolWindowManager.registerToolWindow( BUILDR_TOOL_WINDOW_ID, false,
                                                               ToolWindowAnchor.RIGHT );
      buildrToolWindow.setIcon( BUILDR_16 );

      if ( buildrTasksPane == null ) {
        buildrTasksPane = new BuildrTasksPane( this );
      }

      Content content = getContentFactory().createContent( buildrTasksPane.getTasksPanel(),
                                                           BUILDR_TASKS_DISPLAY_NAME, false );
      buildrToolWindow.getContentManager().addContent( content );
    }
  }

  public void runTask( @Nullable Module module, @NotNull List<String> tasks ) {
    String name = StringUtils.join( tasks, ',' );
    final BuildrConfigurationType type = ConfigurationTypeUtil.findConfigurationType( BuildrConfigurationType.class );

    final RunnerAndConfigurationSettings configSettings = RunManager.getInstance( project )
        .createRunConfiguration( name, type.getConfigurationFactory() );

    BuildrSimpleRunConfiguration configuration = ( BuildrSimpleRunConfiguration ) configSettings.getConfiguration();
    configuration.setModule( module );
    configuration.setTasks( tasks );
    final ProgramRunner runner = RunnerRegistry.getInstance().findRunnerById( DefaultRunExecutor.EXECUTOR_ID );

    assert runner != null;

    final ExecutionEnvironment env = new ExecutionEnvironment(
        configuration,
        project,
        configSettings.getRunnerSettings( runner ),
        configSettings.getConfigurationSettings( runner ),
        null
    );

    try {
      runner.execute( DefaultRunExecutor.getRunExecutorInstance(), env, null );
    } catch ( ExecutionException e ) {
      LOG.error( e );
    }
  }

  public Project getProject() {
    return project;
  }

  public List<BuildrTask> getAvailableTasks( @Nullable Module aModule ) {
    try {
      String workingDirectory = getWorkingDirectory( project, aModule );
      GeneralCommandLine commandLine = createCommandLine( workingDirectory, "help:tasks" );

      if ( workingDirectory != null && commandLine != null ) {
        OSProcessHandler process = new OSProcessHandler( commandLine.createProcess(), commandLine.getCommandLineString() );
        OutputListener outputListener = new OutputListener();
        process.addProcessListener( outputListener );
        process.startNotify();
        process.waitFor( 30 * 1000 );
        Output output = outputListener.getOutput();
        return AvailableTasksParser.parseTasks( output.getStdout() );
      }
    } catch ( ExecutionException e ) {
      // Ignored
    }
    return Collections.emptyList();
  }
}
