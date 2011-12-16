package com.digitalsanctum.idea.plugins.buildr.ui;

import com.digitalsanctum.idea.plugins.buildr.BuildrProjectComponent;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.components.JBList;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
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

        taskList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                BuildrTask task = (BuildrTask) value;
                Component renderer = super.getListCellRendererComponent(list, task.getName(), index, isSelected, cellHasFocus);
                ((JComponent) renderer).setToolTipText(task.getDescription());
                return renderer;
            }
        });

        this.taskList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                LOG.info("event=" + e);
                if (!e.getValueIsAdjusting()) {
                    if(StringUtils.isBlank(tasks.getText())) {
                        tasks.setText(((BuildrTask) taskList.getSelectedValue()).getName());
                    } else {
                        tasks.setText(tasks.getText() + ' ' + ((BuildrTask)taskList.getSelectedValue()).getName());
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
