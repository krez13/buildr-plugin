package com.digitalsanctum.idea.plugins.buildr;

import com.digitalsanctum.idea.plugins.buildr.execution.BuildrRunProfile;
import com.digitalsanctum.idea.plugins.buildr.execution.BuildrTasksPane;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;
import com.intellij.ProjectTopics;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Output;
import com.intellij.execution.OutputListener;
import com.intellij.execution.RunnerRegistry;
import com.intellij.execution.configurations.ConfigurationPerRunnerSettings;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.roots.ModuleRootListener;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.digitalsanctum.idea.plugins.buildr.Buildr.*;

/**
 * User: sblundy
 * Date: Jan 3, 2010
 * Time: 9:13:34 PM
 */
public class BuildrComponent implements ProjectComponent {
  private static final Logger LOG = Logger.getInstance( BuildrComponent.class );

  private static final Pattern TASK_LINE = Pattern.compile( "^\\s*([a-zA-Z0-9:]+)\\s*# (.*)$" );

  private Project project;
  private ToolWindow buildrToolWindow;
  private BuildrTasksPane buildrTasksPane;
  private ContentFactory contentFactory;
  private MessageBusConnection messageBusConnection;

  public BuildrComponent( Project project ) {
    this.project = project;
  }

  public void projectOpened() {
    ToolWindowManager toolWindowManager = ToolWindowManager.getInstance( this.project );
    initBuildrToolWindow( toolWindowManager );

    messageBusConnection = this.project.getMessageBus().connect();
    messageBusConnection.subscribe( ProjectTopics.PROJECT_ROOTS, new ModuleRootListener() {
      public void beforeRootsChange( ModuleRootEvent aModuleRootEvent ) {
      }

      public void rootsChanged( ModuleRootEvent aModuleRootEvent ) {
        SwingUtilities.invokeLater( new Runnable() {
          public void run() {
            buildrTasksPane.refresh();
          }
        } );
      }
    } );
  }

  public void projectClosed() {
    messageBusConnection.dispose();
    messageBusConnection = null;

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

  public boolean canRefreshTaskList() {
    return null != buildrTasksPane && buildrTasksPane.getWorkingDirectory() != null;
  }

  public void runSelectedTask() {
    runTask( this.buildrTasksPane.getWorkingDirectory(), this.buildrTasksPane.getCommand() );
  }

  public boolean canRunSelectedTask() {
    return null != buildrTasksPane && buildrTasksPane.getWorkingDirectory() != null && buildrTasksPane.isTaskSelected();
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

  public void runTask( @Nullable String workindDirectory, @NotNull List<String> tasks ) {
    BuildrRunProfile configuration = new BuildrRunProfile( workindDirectory, tasks );
    final ProgramRunner runner = RunnerRegistry.getInstance().findRunnerById( DefaultRunExecutor.EXECUTOR_ID );

    assert runner != null;

    final ExecutionEnvironment env = new ExecutionEnvironment(
        configuration,
        project,
        new RunnerSettings( null, configuration ),
        new ConfigurationPerRunnerSettings( runner.getRunnerId(), null ),
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
      String workingDirectory = aModule != null ? getWorkingDirectory( aModule ) : getWorkingDirectory( project );
      GeneralCommandLine commandLine = createCommandLine( workingDirectory, "help:tasks" );

      if ( workingDirectory != null && commandLine != null ) {
        OSProcessHandler process = new OSProcessHandler( commandLine.createProcess(), commandLine.getCommandLineString() );
        OutputListener outputListener = new OutputListener();
        process.addProcessListener( outputListener );
        process.startNotify();
        process.waitFor( 30 * 1000 );
        Output output = outputListener.getOutput();
        return parseTasks( output.getStdout() );
      }
    } catch ( ExecutionException e ) {
      // Ignored
    }
    return Collections.emptyList();
  }

  private static List<BuildrTask> parseTasks( String tasksOutput ) {
    if ( tasksOutput == null || tasksOutput.length() == 0 ) {
      return Collections.emptyList();
    }

    List<BuildrTask> tasks = new ArrayList<BuildrTask>();
    String[] taskLine = tasksOutput.split( "[\r\n]+" );
    for ( String s : taskLine ) {
      Matcher matcher = TASK_LINE.matcher( s );
      if ( matcher.matches() ) {
        String name = matcher.group( 1 );
        String desc = matcher.group( 2 );
        BuildrTask task = new BuildrTask( name, desc );
        tasks.add( task );
      }
    }

    return tasks;
  }
}
