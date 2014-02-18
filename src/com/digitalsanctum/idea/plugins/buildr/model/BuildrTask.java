package com.digitalsanctum.idea.plugins.buildr.model;

public class BuildrTask {

  private String name;
  private String description;

  public BuildrTask( String name, String description ) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return String.format( "BuildrTask{name='%s', description = '%s'}", name, description );
  }
}
