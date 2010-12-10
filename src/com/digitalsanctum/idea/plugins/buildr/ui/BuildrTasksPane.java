package com.digitalsanctum.idea.plugins.buildr.ui;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.digitalsanctum.idea.plugins.buildr.BuildrProjectComponent;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.ui.components.JBList;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: shane
 * Date: Nov 26, 2008
 * Time: 7:09:45 AM
 */
public class BuildrTasksPane implements Buildr {
    private JPanel tasksPanel;
    private JTextField commandTextField;
    private JComponent commandToolbar;
    private JComponent taskListToolbar;
    private JList taskList;
    private JLabel tasksLabel;
    private JLabel commandLabel;

  private BuildrProjectComponent buildrProject;


  public BuildrTasksPane(BuildrProjectComponent buildrProject) {
        this.buildrProject = buildrProject;
        this.commandTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(KeyEvent.VK_ENTER == e.getKeyChar()) {
                    ActionManager.getInstance().tryToExecute(ActionManager.getInstance().getAction("runTask"), e,
                            commandTextField, null, true);
                }
            }
        });
    }

    public JPanel getTasksPanel() {
        return tasksPanel;
    }

    private JList createTaskList() {
        final JBList taskList = new JBList(new TaskListModel(Collections.<BuildrTask>emptyList()));
        taskList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                BuildrTask task = (BuildrTask) value;
                Component renderer = super.getListCellRendererComponent(list, task.getName(), index, isSelected, cellHasFocus);
                ((JComponent) renderer).setToolTipText(task.getDescription());
                return renderer;
            }
        });

        final MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton( e ) && e.getClickCount() == 2) {
                    buildrProject.runTask(
                            Arrays.asList(((BuildrTask) taskList.getSelectedValue()).getName()));
                }
            }
        };
        taskList.addMouseListener(mouseListener);
        return taskList;
    }

    private void createUIComponents() {
      this.taskList = createTaskList();

      tasksLabel = new JLabel( "Tasks:" );
      tasksLabel.setBorder( BorderFactory.createEmptyBorder( 0, 3, 0, 0 ));
      commandLabel = new JLabel( "Command:" );
      commandLabel.setBorder( BorderFactory.createEmptyBorder( 0, 3, 0, 0 ));

      ActionManager actionManager = ActionManager.getInstance();
      final ActionGroup commandToolbar = ( ( ActionGroup ) actionManager.getAction("commandToolbar"));
      this.commandToolbar = actionManager.createActionToolbar("Buildr", commandToolbar, true).getComponent();
      final ActionGroup taskListToolbar = ( ( ActionGroup ) actionManager.getAction("taskListToolbar"));
      this.taskListToolbar = actionManager.createActionToolbar("Buildr", taskListToolbar, true).getComponent();
    }

    public void refreshTaskList() {
        if (null != this.taskList) {
            final List<BuildrTask> bTasks = buildrProject.getBuildrProject().getAvailableTasks();
            if (null == bTasks) {
                this.taskList.setModel(new TaskListModel(Collections.<BuildrTask>emptyList()));
            } else {
                this.taskList.setModel(new TaskListModel(bTasks));
            }
        }
    }

    public boolean isTaskSelected() {
        return StringUtils.isNotBlank(this.commandTextField.getText());
    }

    public List<String> getCommand() {
        return Arrays.asList(StringUtils.split(this.commandTextField.getText(), " "));
    }
}
