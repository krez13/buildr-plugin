package com.digitalsanctum.idea.plugins.buildr;

import com.digitalsanctum.idea.plugins.buildr.settings.BuildrApplicationSettings;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.Icon;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 8:38:54 AM
 */
public final class Buildr {

  public static final String VERSION_ARG = "-V";
  public static final String HELP_TASKS_ARG = "-T";
  public static final String[] BUILDFILES = { "buildfile", "Buildfile", "rakefile", "Rakefile", "rakefile.rb", "Rakefile.rb" };

  public static final Icon BUILDR_16 = IconLoader.getIcon( "/com/digitalsanctum/idea/plugins/buildr/buildr-16x16.png" );
  public static final Icon BUILDR_32 = IconLoader.getIcon( "/com/digitalsanctum/idea/plugins/buildr/buildr-32x32.png" );

  public static final String BUILDR_TOOL_WINDOW_ID = "Buildr";
  public static final String BUILDR_TASKS_DISPLAY_NAME = "Tasks";

  private Buildr() {
  }

  public static boolean buildfilePresent( VirtualFile aVf ) {
    for ( String buildfile : BUILDFILES ) {
      if ( aVf.findChild( buildfile ) != null ) {
        return true;
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

  public static String getWorkingDirectory( Project aProject, Module... aModules ) {
    for ( int i = 0; i < aModules.length; i++ ) {
      Module module = aModules[ i ];
      if ( module != null ) {
        VirtualFile[] roots = ModuleRootManager.getInstance( module ).getContentRoots();
        for ( int j = 0; j < roots.length; j++ ) {
          VirtualFile root = roots[ j ];
          if ( buildfilePresent( root ) ) {
            return root.getPath();
          }
        }
      }
    }

    VirtualFile baseDir = aProject.getBaseDir();
    if ( baseDir != null ) {
      if ( buildfilePresent( baseDir ) ) {
        return baseDir.getPath();
      }
    }

    return null;
  }
}
