package com.digitalsanctum.idea.plugins.buildr.execution;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

/**
 * User: steve
 * Date: Jan 4, 2010
 * Time: 10:25:35 PM
 */
public class BuildrConfigurationType implements ConfigurationType {
  private final ConfigurationFactory myConfigurationFactory = new ConfigurationFactory( this ) {
    @Override
    public RunConfiguration createTemplateConfiguration( Project project ) {
      return new BuildrRunConfiguration( "", project, this );
    }
  };

  public static BuildrConfigurationType getInstance() {
    return ConfigurationTypeUtil.findConfigurationType( BuildrConfigurationType.class );
  }

  public String getDisplayName() {
    return "Buildr";
  }

  public String getConfigurationTypeDescription() {
    return "Buildr";
  }

  public Icon getIcon() {
    return Buildr.BUILDR_16;
  }

  @NotNull
  public String getId() {
    return getDisplayName();
  }

  public ConfigurationFactory[] getConfigurationFactories() {
    return new ConfigurationFactory[]{
        myConfigurationFactory
    };
  }

  public ConfigurationFactory getConfigurationFactory() {
    return myConfigurationFactory;
  }
}
