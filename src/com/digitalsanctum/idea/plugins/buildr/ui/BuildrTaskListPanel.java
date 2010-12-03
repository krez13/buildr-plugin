package com.digitalsanctum.idea.plugins.buildr.ui;

import com.digitalsanctum.idea.plugins.buildr.BuildrProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.components.JBList;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Arrays;
import java.util.List;

/**
 * User: sblundy
 * Date: Jan 8, 2010
 * Time: 9:11:35 PM
 */
public class BuildrTaskListPanel {
    public static final Logger LOG = Logger.getInstance(BuildrTaskListPanel.class.getName());
    private JTextField tasks;
    private JPanel panel;
    private JBList taskList;

    public BuildrTaskListPanel(BuildrProjectComponent buildrProject) {
        this.taskList.setModel(new TaskListModel(buildrProject.getBuildrProject().getAvailableTasks()));
        this.taskList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                LOG.info("event=" + e);
                if (!e.getValueIsAdjusting()) {
                    if(StringUtils.isBlank(tasks.getText())) {
                        tasks.setText((String) taskList.getSelectedValue());
                    } else {
                        tasks.setText(tasks.getText() + ' ' + taskList.getSelectedValue());
                    }
                }
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    public List<String> getTasks() {
        return Arrays.asList(StringUtils.split(tasks.getText(), " "));
    }

    public void setTasks(List<String> tasks) {
        this.tasks.setText(StringUtils.join(tasks, " "));
    }
}
