package com.digitalsanctum.idea.plugins.buildr.settings;

import com.digitalsanctum.idea.plugins.buildr.BuildrBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

@State(
        name = "BuildrSettings",
        storages = {
                @Storage(
                        id = "main",
                        file = "$APP_CONFIG$/buildrsettings.xml"
                )
        }
)
public class BuildrSettings implements ApplicationComponent, Configurable, PersistentStateComponent<BuildrSettings.State> {
  public static class State {
    public String buildrPath = "";

    public State() {
    }
  }

  private State state;
  private GeneralSettingsTab form;

  public static BuildrSettings getInstance() {
    return ApplicationManager.getApplication().getComponent( BuildrSettings.class );
  }

  public BuildrSettings() {
    state = new State();
  }

  /**
   * Unique name of this component. If there is another component with the same name or
   * name is null internal assertion will occur.
   *
   * @return the name of this component
   */
  @NotNull
  public String getComponentName() {
    return "BuildrSettings";
  }

  /**
   * Component should do initialization and communication with another components in this method.
   */
  public void initComponent() {
  }

  /**
   * Component should dispose system resources or perform another cleanup in this method.
   */
  public void disposeComponent() {
  }

  /**
   * Returns the user-visible name of the settings component.
   *
   * @return the visible name of the component.
   */
  @Nls
  public String getDisplayName() {
    return BuildrBundle.message("settings.title");
  }

  /**
   * Returns the topic in the help file which is shown when help for the configurable
   * is requested.
   *
   * @return the help topic, or null if no help is available.
   */
  public String getHelpTopic() {
    return null;
  }

  /**
   * Returns the user interface component for editing the configuration.
   *
   * @return the component instance.
   */
  public JComponent createComponent() {
    if (form == null) {
      form = new GeneralSettingsTab();
    }
    return form.getContentPanel();
  }

  /**
   * Checks if the settings in the user interface component were modified by the user and
   * need to be saved.
   *
   * @return true if the settings were modified, false otherwise.
   */
  public boolean isModified() {
    return form == null || form.isModified();
  }

  /**
   * Store the settings from configurable to other components.
   */
  public void apply() throws ConfigurationException {
    if (form != null) {
      form.validate();
      // Get data from form to component
      form.apply();
    }
  }

  /**
   * Load settings from other components to configurable.
   */
  public void reset() {
    if (form != null) {
      // Reset form data from component
      form.reset();
    }
  }

  /**
   * Disposes the Swing components used for displaying the configuration.
   */
  public void disposeUIResources() {
    form = null;
  }

  @Nullable
  @Override
  public State getState() {
    return state;
  }

  @Override
  public void loadState( State state ) {
    this.state = state;
  }

  public String getBuildrPath() {
    return state.buildrPath;
  }

  public void setBuildrPath(String buildrPath) {
    state.buildrPath = buildrPath;
  }

  public boolean isValidBuildrPath(String buildrPath) {
    File executable = new File(buildrPath);
    return executable.exists() && executable.isFile() && executable.getName().startsWith("buildr");
  }
}
