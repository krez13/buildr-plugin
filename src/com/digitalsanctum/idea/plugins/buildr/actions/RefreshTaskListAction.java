package com.digitalsanctum.idea.plugins.buildr.actions;

import com.digitalsanctum.idea.plugins.buildr.BuildrComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class RefreshTaskListAction extends AbstractProjectAction {
  @Override
  public void actionPerformed( AnActionEvent event ) {
    BuildrComponent bpc = findProjectComponent( event.getDataContext(), BuildrComponent.class );
    if ( null != bpc ) {
      bpc.refreshTaskList();
    }
  }

  @Override
  public void update( AnActionEvent event ) {
    final BuildrComponent bpc = findProjectComponent( event.getDataContext(), BuildrComponent.class );
    event.getPresentation().setEnabled( null != bpc && bpc.canRefreshTaskList() );
  }
}
