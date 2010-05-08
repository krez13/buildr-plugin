package com.digitalsanctum.idea.plugins.buildr;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 8:38:54 AM
 */
public interface Buildr {

  public static final String VERSION_ARG = "-V";
  public static final String HELP_TASKS_ARG = "-T";
  public static final String[] BUILDFILES = {"buildfile", "Buildfile", "rakefile", "Rakefile", "rakefile.rb", "Rakefile.rb"};
  
  public static final Icon BUILDR_16 = IconLoader.getIcon("/com/digitalsanctum/idea/plugins/buildr/buildr-16x16.png");
  public static final Icon BUILDR_32 = IconLoader.getIcon("/com/digitalsanctum/idea/plugins/buildr/buildr-32x32.png");

  public static final String BUILDR_TOOL_WINDOW_ID = "Buildr";
  public static final String BUILDR_TASKS_DISPLAY_NAME = "Tasks";
}
