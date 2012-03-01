package com.digitalsanctum.idea.plugins.buildr.settings;

import com.digitalsanctum.idea.plugins.buildr.BuildrBundle;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Component;

/**
 * User: shane
 * Date: Nov 22, 2008
 * Time: 11:03:42 AM
 */
public class GeneralSettingsTab implements UnnamedConfigurable {
  private JPanel myContentPane;
  private TextFieldWithBrowseButton buildrPath;
  private TextFieldWithBrowseButton bundlerPath;
  private JCheckBox bundlerEnabled;

  public GeneralSettingsTab() {
  }

  public Component getContentPanel() {
    return myContentPane;
  }

  public JComponent createComponent() {
    //N/A
    return null;
  }

  /**
   * Checks if the settings in the user interface component were modified by the user and
   * need to be saved.
   *
   * @return true if the settings were modified, false otherwise.
   */
  public boolean isModified() {
    final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();
    return !Comparing.equal( buildrPath.getText().trim(), settings.getBuildrPath() ) ||
           !Comparing.equal( bundlerPath.getText().trim(), settings.getBundlerPath() ) ||
           !Comparing.equal( bundlerEnabled.isSelected(), settings.getBundlerEnabled() );
  }

  /**
   * Store the settings from configurable to other components.
   */
  public void apply() throws ConfigurationException {
    final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();
    settings.setBuildrPath( buildrPath.getText().trim() );
    settings.setBundlerPath( bundlerPath.getText().trim() );
    settings.setBundlerEnabled( bundlerEnabled.isSelected() );
  }

  //

  /**
   * Load settings from other components to configurable.
   */
  public void reset() {
    final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();
    buildrPath.setText( settings.getBuildrPath() );
    bundlerPath.setText( settings.getBundlerPath() );
    bundlerEnabled.setSelected( settings.getBundlerEnabled() );
  }

  /**
   * Disposes the Swing components used for displaying the configuration.
   */
  public void disposeUIResources() {
    // do nothing
  }

  public void validate() throws ConfigurationException {
    final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();

    String buildrPathText = buildrPath.getText();
    if ( !settings.isValidBuildrPath( buildrPathText ) ) {
      throw new ConfigurationException(
          BuildrBundle.message( "buildr.configuration.executable.error", buildrPathText )
      );
    }

    String bundlerPathText = bundlerPath.getText();
    if ( settings.getBundlerEnabled() ) {
      if ( !settings.isValidBundlerPath( bundlerPathText ) ) {
        throw new ConfigurationException(
            BuildrBundle.message( "buildr.configuration.executable.error", bundlerPathText )
        );
      }
    }
  }

  private void createUIComponents() {
    buildrPath = new TextFieldWithBrowseButton();
    buildrPath.addBrowseFolderListener(
        BuildrBundle.message( "buildr.configuration.title" ),
        BuildrBundle.message( "buildr.configuration.buildrdescription" ),
        null,
        new FileChooserDescriptor( true, false, false, false, false, false ) {
          public void validateSelectedFiles( VirtualFile[] files ) throws Exception {
            String path = files[ 0 ].getPath();
            final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();

            if ( !settings.isValidBuildrPath( path ) ) {
              throw new ConfigurationException(
                  BuildrBundle.message( "buildr.configuration.executable.error", path )
              );
            }
          }
        }
    );

    bundlerPath = new TextFieldWithBrowseButton();
    bundlerPath.addBrowseFolderListener(
        BuildrBundle.message( "buildr.configuration.title" ),
        BuildrBundle.message( "buildr.configuration.bundlerdescription" ),
        null,
        new FileChooserDescriptor( true, false, false, false, false, false ) {
          public void validateSelectedFiles( VirtualFile[] files ) throws Exception {
            String path = files[ 0 ].getPath();
            final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();

            if ( !settings.isValidBundlerPath( path ) ) {
              throw new ConfigurationException(
                  BuildrBundle.message( "buildr.configuration.executable.error", path )
              );
            }
          }
        }
    );
  }
}