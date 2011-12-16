package com.digitalsanctum.idea.plugins.buildr.run;

import com.digitalsanctum.idea.plugins.buildr.settings.BuildrApplicationSettings;

import java.util.ArrayList;
import java.util.List;

public class BuildrCommand {
    private String path;
    private List<String> arguments;

    public BuildrCommand(BuildrApplicationSettings settings, List<String> tasks) {
        arguments = new ArrayList<String>();
        if(settings.getBundlerEnabled()) {
            path = settings.getBundlerPath();
            arguments.add("exec");
            arguments.add("buildr");
        }
        else {
            path = settings.getBuildrPath();
        }

        arguments.addAll(tasks);
    }

    public String getCommandLine() {
        StringBuilder commandLine = new StringBuilder(path);
        for(String arg : arguments) {
            commandLine.append(arg);
        }
        return commandLine.toString();
    }

    public String getExecutablePath() {
        return path;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
