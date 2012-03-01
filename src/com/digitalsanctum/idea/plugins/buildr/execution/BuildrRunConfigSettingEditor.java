package com.digitalsanctum.idea.plugins.buildr.execution;

import com.digitalsanctum.idea.plugins.buildr.BuildrComponent;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.Arrays;

/**
 * User: steve
 * Date: Jan 4, 2010
 * Time: 11:36:31 PM
 */
public class BuildrRunConfigSettingEditor extends SettingsEditor<BuildrSimpleRunConfiguration> {
  private final ModuleSelector moduleSelector;

  private JPanel panel;
  private JComboBox modulesComboBox;
  private JTextField tasks;

  public BuildrRunConfigSettingEditor( BuildrComponent buildrProject ) {
    moduleSelector = new ModuleSelector(
        buildrProject.getProject(),
        modulesComboBox
    );
  }

  @Override
  protected void resetEditorFrom( BuildrSimpleRunConfiguration config ) {
    moduleSelector.reset( config );
    tasks.setText( StringUtils.join( config.getTasks(), " " ) );
  }

  @Override
  protected void applyEditorTo( BuildrSimpleRunConfiguration config ) throws ConfigurationException {
    moduleSelector.applyTo( config );
    config.setTasks( Arrays.asList( StringUtils.split( tasks.getText(), " " ) ) );
  }

  @NotNull
  @Override
  protected JComponent createEditor() {
    return panel;
  }

  @Override
  protected void disposeEditor() {
  }
}
