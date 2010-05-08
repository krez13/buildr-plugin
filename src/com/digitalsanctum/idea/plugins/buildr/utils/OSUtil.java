package com.digitalsanctum.idea.plugins.buildr.utils;

import com.digitalsanctum.idea.plugins.buildr.BuildrBundle;
import com.digitalsanctum.idea.plugins.buildr.lang.TextUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * User: witbsha
 * Date: Feb 4, 2008
 * Time: 10:55:41 AM
 */
public class OSUtil {

  private static final Logger LOG = Logger.getInstance(OSUtil.class.getName());

  private static final String UNIX_PATH_NAME = "PATH";
  private static final String WINDOWS_PATH_NAME = "Path";

  @Nullable
  public static String getPATHenvVariableName() {
    if (SystemInfo.isWindows) {
      return WINDOWS_PATH_NAME;
    }
    if (SystemInfo.isUnix) {
      return UNIX_PATH_NAME;
    }
    LOG.error(BuildrBundle.message("os.not.supported"));
    return null;
  }

  public static String appendToPATHenvVariable(@Nullable final String path,
                                               @NotNull final String additionalPath) {
    final String pathValue;
    if (TextUtil.isEmpty(path)) {
      pathValue = additionalPath;
    } else {
      pathValue = path + File.pathSeparatorChar + additionalPath;
    }
    return VirtualFileUtil.convertToOSDependentPath(pathValue);
  }
}
