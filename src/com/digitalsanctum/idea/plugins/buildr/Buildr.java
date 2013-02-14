package com.digitalsanctum.idea.plugins.buildr;

import com.digitalsanctum.idea.plugins.buildr.settings.BuildrApplicationSettings;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 8:38:54 AM
 */
public final class Buildr {
  public static final String[] BUILDFILES = { "buildfile", "Buildfile", "rakefile", "Rakefile", "rakefile.rb", "Rakefile.rb" };

  public static final Icon BUILDR_13 = IconLoader.getIcon( "/com/digitalsanctum/idea/plugins/buildr/buildr-13x13.png" );
  public static final Icon BUILDR_16 = IconLoader.getIcon( "/com/digitalsanctum/idea/plugins/buildr/buildr-16x16.png" );

  public static final String BUILDR_TOOL_WINDOW_ID = "Buildr";
  public static final String BUILDR_TASKS_DISPLAY_NAME = "Tasks";

  private Buildr() {
  }

  public static boolean buildfilePresent( VirtualFile aVf ) {
    for ( String buildfile : BUILDFILES ) {
      VirtualFile f = aVf;
      while ( f != null ) {
        if ( f.findChild( buildfile ) != null ) {
          return true;
        }
        else {
          f = f.getParent();
        }
      }

    }
    return false;
  }

  public static GeneralCommandLine createCommandLine( String aDirectory, String... aArguments ) {
    return createCommandLine( aDirectory, Arrays.asList( aArguments ) );
  }

  public static GeneralCommandLine createCommandLine( String aDirectory, List<String> aArguments ) {
    if ( aDirectory == null ) {
      return null;
    }

    final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();

    GeneralCommandLine commandLine = new GeneralCommandLine();

    if ( settings.getBundlerEnabled() ) {
      commandLine.setExePath( settings.getBundlerPath() );
      commandLine.addParameters( "exec", "buildr" );
    }
    else {
      commandLine.setExePath( settings.getBuildrPath() );
    }

    if ( !new File( commandLine.getExePath() ).canExecute() ) {
      return null;
    }

    commandLine.setWorkDirectory( aDirectory );
    commandLine.addParameters( aArguments );
    return commandLine;
  }

  public static String getWorkingDirectory( Project aProject ) {
    if ( aProject != null ) {
      VirtualFile baseDir = aProject.getBaseDir();
      return baseDir != null && buildfilePresent( baseDir ) ? baseDir.getPath() : null;
    }

    return null;
  }

  public static String getWorkingDirectory( Module aModule ) {
    if ( aModule != null ) {
      VirtualFile[] roots = ModuleRootManager.getInstance( aModule ).getContentRoots();
      for ( int j = 0; j < roots.length; j++ ) {
        VirtualFile root = roots[ j ];
        if ( buildfilePresent( root ) ) {
          return root.getPath();
        }
      }
    }

    return null;
  }
}
