package com.digitalsanctum.idea.plugins.buildr.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractProjectAction extends AnAction {
    @Nullable
    protected <T> T findProjectComponent(DataContext ctxt, Class<T> type) {
        final Project project = PlatformDataKeys.PROJECT.getData(ctxt);
        if (null != project) {
            return project.getComponent(type);
        } else {
            return null;
        }
    }
}
