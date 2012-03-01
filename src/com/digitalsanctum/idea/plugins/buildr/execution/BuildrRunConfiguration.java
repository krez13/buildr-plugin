package com.digitalsanctum.idea.plugins.buildr.execution;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.LocatableConfiguration;
import com.intellij.execution.configurations.ModuleRunConfiguration;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class BuildrRunConfiguration extends BuildrSimpleRunConfiguration implements LocatableConfiguration, Cloneable, ModuleRunConfiguration {
  public BuildrRunConfiguration( String name, Project project, ConfigurationFactory configurationFactory ) {
    super( name, project, configurationFactory );
  }

  @NotNull
  public Module[] getModules() {
    return new Module[0];
  }

  public boolean isGeneratedName() {
    return false;
  }

  public String suggestedName() {
    return null;
  }
}
