package com.digitalsanctum.idea.plugins.buildr.run;

/**
 * User: shane
 * Date: Feb 4, 2008
 * Time: 7:24:26 AM
 */
public class Output {
  private String stdout;
  private String stderr;
  private int exitCode;

  public Output( String stdout, String stderr, int exitCode ) {
    this.stdout = stdout;
    this.stderr = stderr;
    this.exitCode = exitCode;
  }

  public String getStdout() {
    return stdout;
  }

  public String getStderr() {
    return stderr;
  }

  public int getExitCode() {
    return exitCode;
  }
}
