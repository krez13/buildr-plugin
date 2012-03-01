package com.digitalsanctum.idea.plugins.buildr.parser;

import com.digitalsanctum.idea.plugins.buildr.model.BuildrTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: shane
 * Date: Feb 3, 2008
 * Time: 10:28:14 AM
 */
public class AvailableTasksParser {
  private static final String BUILDR_PREFIX = "[Bb]uildr ";
  private static final Pattern TASK_LINE = Pattern.compile( "^\\s*([a-zA-Z0-9:]+)\\s*# (.*)$" );

  public static List<BuildrTask> parseTasks( String tasksOutput ) {
    if ( tasksOutput == null || tasksOutput.length() == 0 ) {
      return Collections.emptyList();
    }

    List<BuildrTask> tasks = new ArrayList<BuildrTask>();
    String[] taskLine = tasksOutput.split( "[\r\n]+" );
    for ( String s : taskLine ) {
      Matcher matcher = TASK_LINE.matcher( s );
      if ( matcher.matches() ) {
        String name = matcher.group( 1 );
        String desc = matcher.group( 2 );
        BuildrTask task = new BuildrTask( name, desc );
        tasks.add( task );
      }
    }

    return tasks;
  }
}
