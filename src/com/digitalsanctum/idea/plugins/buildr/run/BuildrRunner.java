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

    final BuildrApplicationSettings settings = BuildrApplicationSettings.getInstance();
    BuildrCommand command = settings.getBuilderCommandUsingTasks(args);
    Output output;
    try {
      output = Runner.runInPathAndShowErrors(buildrProject.getBaseDirPath(), buildrProject.getProject(), mode, true, "Buildr Error", command);
    } catch (BuildrPluginException e) {
      LOG.error(BuildrBundle.message("error.buildr.exception"), e);
      output = new Output(null, BuildrBundle.message("error.buildr.exception"), -1 );
    }

    if (TextUtil.isEmpty(output.getStdout())) {
        if (!TextUtil.isEmpty(output.getStderr())) {
            output = new Output(null, output.getStderr(), output.getExitCode() );
        } else {
            output = new Output(null, BuildrBundle.message("error.null.output"), output.getExitCode() );
        }
    }

    return output;
  }
}
