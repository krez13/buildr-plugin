package com.digitalsanctum.idea.plugins.buildr.model;

/**
 * User: shane
 * Date: Feb 2, 2008
 * Time: 11:39:54 PM
 */
public class BuildrTask {

  private String name;
  private String description;

  public BuildrTask(String name, String description) {
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
    return "BuildrTask{" +
        "name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
