package com.digitalsanctum.idea.plugins.buildr.settings;

import com.digitalsanctum.idea.plugins.buildr.BuildrBundle;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

class SetExecutablePathPanel extends TextFieldWithBrowseButton {
  private final Set<ActionListener> myOkListeners = new HashSet<ActionListener>();

  SetExecutablePathPanel() {
    FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false) {
      public void validateSelectedFiles(VirtualFile[] files) throws Exception {
        String path = files[0].getPath();
        final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();

        if (!settings.isValidBuildrPath(path)) {
          throw new ConfigurationException(
            BuildrBundle.message("buildr.configuration.executable.error", path)
          );
        }
        for (ActionListener okListener : myOkListeners) {
          okListener.actionPerformed(null);
        }
      }
    };
    addBrowseFolderListener(BuildrBundle.message("buildr.configuration.title"), BuildrBundle.message("buildr.configuration.description"), null, descriptor);
  }

  /**
   * Adds a listener which will be called when file chooser dialog is closed successfully.
   */
  void addOKListener(ActionListener listener) {
    myOkListeners.add(listener);
  }
}
