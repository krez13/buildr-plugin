package com.digitalsanctum.idea.plugins.buildr.execution;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.digitalsanctum.idea.plugins.buildr.BuildrComponent;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.vcs.changes.BackgroundFromStartOption;
import com.intellij.ui.components.JBList;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: shane
 * Date: Nov 26, 2008
 * Time: 7:09:45 AM
 */
public class BuildrTasksPane {
  private JPanel tasksPanel;
  private JTextField commandTextField;
  private JComponent commandToolbar;
  private JComponent taskListToolbar;
  private JList taskList;
  private JLabel tasksLabel;
  private JLabel commandLabel;
  private JComboBox moduleComboBox;
  private JLabel moduleLabel;
  private final BuildrComponent component;
  private final ModuleSelector moduleSelector;

  public BuildrTasksPane( BuildrComponent component ) {
    moduleSelector = new ModuleSelector( component.getProject(), moduleComboBox );
    moduleSelector.updateModules();

    this.component = component;
    this.commandTextField.addKeyListener( new KeyAdapter() {
      @Override
      public void keyTyped( KeyEvent e ) {
        if ( KeyEvent.VK_ENTER == e.getKeyChar() ) {
          ActionManager.getInstance().tryToExecute( ActionManager.getInstance().getAction( "runTask" ), e,
                                                    commandTextField, null, true );
        }
      }
    } );
  }

  public JPanel getTasksPanel() {
    return tasksPanel;
  }

  private JList createTaskList() {
    final JBList taskList = new JBList( new TaskListModel( Collections.<BuildrTask>emptyList() ) );
    taskList.setCellRenderer( new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
        BuildrTask task = ( BuildrTask ) value;
        Component renderer = super.getListCellRendererComponent( list, task.getName(), index, isSelected, cellHasFocus );
        ( ( JComponent ) renderer ).setToolTipText( task.getDescription() );
        return renderer;
      }
    } );

    final MouseListener mouseListener = new MouseAdapter() {
      public void mouseClicked( MouseEvent e ) {
        if ( SwingUtilities.isLeftMouseButton( e ) && e.getClickCount() == 2 ) {
          component.runTask(
              getWorkingDirectory(),
              Arrays.asList( ( ( BuildrTask ) taskList.getSelectedValue() ).getName() ) );
        }
      }
    };
    taskList.addMouseListener( mouseListener );
    return taskList;
  }

  private void createUIComponents() {
    this.taskList = createTaskList();


    moduleLabel = new JLabel();
    moduleLabel.setBorder( BorderFactory.createEmptyBorder( 0, 3, 0, 0 ) );
    tasksLabel = new JLabel();
    tasksLabel.setBorder( BorderFactory.createEmptyBorder( 0, 3, 0, 0 ) );
    commandLabel = new JLabel();
    commandLabel.setBorder( BorderFactory.createEmptyBorder( 0, 3, 0, 0 ) );

    ActionManager actionManager = ActionManager.getInstance();
    final ActionGroup commandToolbar = ( ( ActionGroup ) actionManager.getAction( "commandToolbar" ) );
    this.commandToolbar = actionManager.createActionToolbar( "Buildr", commandToolbar, true ).getComponent();
    final ActionGroup taskListToolbar = ( ( ActionGroup ) actionManager.getAction( "taskListToolbar" ) );
    this.taskListToolbar = actionManager.createActionToolbar( "Buildr", taskListToolbar, true ).getComponent();
  }

  public void refreshTaskList() {
    new Task.Backgroundable( component.getProject(), "Retrieving available tasks", false, BackgroundFromStartOption.getInstance() ) {
      public void run( @NotNull ProgressIndicator aProgressIndicator ) {
        final List<BuildrTask> bTasks = component.getAvailableTasks( moduleSelector.getModule() );
        SwingUtilities.invokeLater( new Runnable() {
          public void run() {
            if ( null == bTasks ) {
              taskList.setModel( new TaskListModel( Collections.<BuildrTask>emptyList() ) );
            }
            else {
              taskList.setModel( new TaskListModel( bTasks ) );
            }
          }
        } );

      }
    }.queue();
  }

  public boolean isTaskSelected() {
    return StringUtils.isNotBlank( this.commandTextField.getText() );
  }

  public List<String> getCommand() {
    return Arrays.asList( StringUtils.split( this.commandTextField.getText(), " " ) );
  }

  public void refresh() {
    moduleSelector.updateModules();
  }

  public String getWorkingDirectory() {
    Module module = moduleSelector.getModule();
    return module != null ? Buildr.getWorkingDirectory( module ) : Buildr.getWorkingDirectory( component.getProject() );
  }
}
