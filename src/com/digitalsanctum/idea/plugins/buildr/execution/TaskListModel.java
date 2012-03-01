package com.digitalsanctum.idea.plugins.buildr.execution;

import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;

import javax.swing.*;
import java.util.List;

/**
* User: sblundy
* Date: Jan 8, 2010
* Time: 9:42:08 PM
*/
class TaskListModel extends AbstractListModel {
    final List<BuildrTask> tasks = new java.util.ArrayList<BuildrTask>();

    public TaskListModel(List<BuildrTask> tasks) {
        setTasks( tasks );
    }

    public void setTasks(List<BuildrTask> tasks) {
        this.tasks.clear();
        this.tasks.addAll( tasks );
        fireContentsChanged( this, 0, this.tasks.size() + 1 );
    }
  
    public int getSize() {
        return tasks.size();
    }

    public Object getElementAt(int index) {
        return tasks.get(index);
    }
}
