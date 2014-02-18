package com.digitalsanctum.idea.plugins.buildr.settings;

import com.digitalsanctum.idea.plugins.buildr.BuildrBundle;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.IdeBorderFactory;

import javax.swing.*;

public class GeneralSettingsTab {
  private JPanel myContentPane;
  private TextFieldWithBrowseButton buildrPath;

  public GeneralSettingsTab() {
  }

  public JComponent getContentPanel() {
    return myContentPane;
  }

  public boolean isModified() {
    final BuildrSettings settings = BuildrSettings.getInstance();
    return !Comparing.equal( buildrPath.getText().trim(), settings.getBuildrPath() );
  }

  public void apply() throws ConfigurationException {
    final BuildrSettings settings = BuildrSettings.getInstance();
    settings.setBuildrPath( buildrPath.getText().trim() );
  }

  public void reset() {
    final BuildrSettings settings = BuildrSettings.getInstance();
    buildrPath.setText( settings.getBuildrPath() );
  }

  public void validate() throws ConfigurationException {
    final BuildrSettings settings = BuildrSettings.getInstance();

    String buildrPathText = buildrPath.getText();
    if ( !settings.isValidBuildrPath( buildrPathText ) ) {
      throw new ConfigurationException(
          BuildrBundle.message( "buildr.configuration.executable.error", buildrPathText )
      );
    }
  }

  private void createUIComponents() {
    myContentPane = new JPanel();
    myContentPane.setBorder(
            IdeBorderFactory.createTitledBorder( BuildrBundle.message( "buildr.configuration.environment" ), true )
    );

    buildrPath = new TextFieldWithBrowseButton();
    buildrPath.addBrowseFolderListener(
        BuildrBundle.message( "buildr.configuration.title" ),
        BuildrBundle.message( "buildr.configuration.buildrdescription" ),
        null,
        new FileChooserDescriptor( true, false, false, false, false, false ) {
          public void validateSelectedFiles( VirtualFile[] files ) throws Exception {
            String path = files[ 0 ].getPath();
            final BuildrSettings settings = BuildrSettings.getInstance();

            if ( !settings.isValidBuildrPath( path ) ) {
              throw new ConfigurationException(
                  BuildrBundle.message( "buildr.configuration.executable.error", path )
              );
            }
          }
        }
    );
  }
}