package com.digitalsanctum.idea.plugins.buildr.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * User: shane
 * Date: Feb 4, 2008
 * Time: 7:29:39 AM
 */
public class VirtualFileUtil {
  @NonNls
  public static final char VFS_PATH_SEPARATOR = '/';

  public static String convertToOSDependentPath(@NotNull final String path) {
    return path.replace(VFS_PATH_SEPARATOR, File.separatorChar);
  }
}
