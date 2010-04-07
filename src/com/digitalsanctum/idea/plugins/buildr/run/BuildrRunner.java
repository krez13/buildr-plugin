package com.digitalsanctum.idea.plugins.buildr.run;

import com.digitalsanctum.idea.plugins.buildr.Buildr;
import com.digitalsanctum.idea.plugins.buildr.BuildrBundle;
import com.digitalsanctum.idea.plugins.buildr.exception.BuildrPluginException;
import com.digitalsanctum.idea.plugins.buildr.lang.TextUtil;
import com.digitalsanctum.idea.plugins.buildr.model.BuildrProject;
import com.digitalsanctum.idea.plugins.buildr.settings.BuildrApplicationSettings;
import com.digitalsanctum.idea.plugins.buildr.utils.OSUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 9:18:42 AM
 */
public class BuildrRunner extends Runner implements Buildr {

  private static final Logger LOG = Logger.getInstance(BuildrRunner.class.getName());


  public static final String BUILDR_SCRIPT_NAME = "buildr";
  private static final String[] BUILDR_CMD_ARGS = new String[]{"buildr", "help"};
  private static final String BAT_SUFFIX = ".bat";

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

  public String getBuildrCommandPath() {
    return OSUtil.findExecutableByName(SystemInfo.isWindows ? BUILDR_SCRIPT_NAME + BAT_SUFFIX : BUILDR_SCRIPT_NAME);
  }

  public boolean isBuildrCommandAvailable() {

// todo: figure out how best to get buildr path


//    return isBuildrInExtendedLoadPath(null);
    return true;
  }

  /**
   * Checks if Buildr is in extended LOAD path
   *
   * @param buildrPath path to buildr
   * @return true if Buildr is in load path
   */
  @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
  public static boolean isBuildrInExtendedLoadPath(final @Nullable String buildrPath) {
    final Output output = BuildrRunner.runSystemCommand(buildrPath, null, BUILDR_CMD_ARGS[0], BUILDR_CMD_ARGS[1]);
    return TextUtil.isEmpty(output.getStderr());
  }

  public static Output runSystemCommand(@Nullable final String additionalPath,
                                        @Nullable final String workingDirectory,
                                        @NotNull final String... arguments) {
    try {

      @NonNls
      final String argument = "-e exec '" + TextUtil.concat(arguments) + "'";
      return Runner.execute(Runner.createAndSetupCmdLine(additionalPath, workingDirectory, null, argument));
    } catch (ExecutionException e) {
      return new Output(TextUtil.EMPTY_STRING, e.getMessage(), -1 );
    }
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
    commands[0] = settings.buildrPath;

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
