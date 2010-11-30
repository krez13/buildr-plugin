package com.digitalsanctum.idea.plugins.buildr.run;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.digitalsanctum.idea.plugins.buildr.BuildrBundle;
import com.digitalsanctum.idea.plugins.buildr.exception.BuildrPluginException;
import com.digitalsanctum.idea.plugins.buildr.lang.TextUtil;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrProject;
import com.digitalsanctum.idea.plugins.buildr.settings.BuildrApplicationSettings;
import com.intellij.openapi.diagnostic.Logger;

import java.util.LinkedList;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 9:18:42 AM
 */
public class BuildrRunner extends Runner implements Buildr {

  private static final Logger LOG = Logger.getInstance(BuildrRunner.class.getName());

  private BuildrProject buildrProject;
  private LinkedList<String> args;

  public BuildrRunner(BuildrProject buildrProject) {
    this.buildrProject = buildrProject;
  }

  public BuildrRunner(BuildrProject buildrProject, String arg) {
    this.buildrProject = buildrProject;
    addArgument(arg);
  }

  public boolean addArgument(String arg) {
    if (args == null) {
      args = new LinkedList<String>();
    }
    return args.add(arg);
  }

  public Output runBuildrCommand() throws BuildrPluginException {

    final String progressTitle = BuildrBundle.message("progress.please.wait");
    final Runner.ModalProgressMode mode = new Runner.ModalProgressMode(progressTitle);

    String[] commands = new String[args.size() + 1];


    /*if (OSUtil.isWindows()) {
      commands[0] = BUILDR_WINDOWS;
    } else {
      commands[0] = BUILDR;
    }*/
    final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();
    commands[0] = settings.getBuildrPath();

    System.arraycopy(args.toArray(new String[args.size()]), 0, commands, 1, args.size());

    Output output;
    try {
      output = Runner.runInPathAndShowErrors(buildrProject.getBaseDirPath(), buildrProject.getProject(), mode, true, "Buildr Error", commands);
    } catch (BuildrPluginException e) {
      LOG.error(BuildrBundle.message("error.buildr.exception"), e);
      output = new Output(null, BuildrBundle.message("error.buildr.exception"), -1 );
    }

    if (TextUtil.isEmpty(output.getStdout())) {
      output = new Output(null, BuildrBundle.message("error.null.output"), output.getExitCode() );
    }

    return output;
  }
}
