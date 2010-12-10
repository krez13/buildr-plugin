package com.digitalsanctum.idea.plugins.buildr.settings;

import com.digitalsanctum.idea.plugins.buildr.lang.TextUtil;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * User: shane
 * Date: Nov 22, 2008
 * Time: 11:32:18 AM
 */

@State(
        name = "BuildrApplicationSettings",
        storages = {
                @Storage(
                        id = "main",
                        file = "$APP_CONFIG$/buildrsettings.xml"
                )
        }
)
public class BuildrApplicationSettings implements PersistentStateComponent<BuildrApplicationSettings> {

    private String buildrPath = TextUtil.EMPTY_STRING;


    public static BuildrApplicationSettings getInstance() {
        return ServiceManager.getService(BuildrApplicationSettings.class);
    }


    /**
     * @return a component state. All properties and public fields are serialized. Only values, which differ
     *         from default (i.e. the value of newly instantiated class) are serialized.
     * @see com.intellij.util.xmlb.XmlSerializer
     */
    public BuildrApplicationSettings getState() {
        return this;
    }

    /**
     * This method is called when new component state is loaded. A component should expect this method
     * to be called at any moment of its lifecycle. The method can and will be called several times, if
     * config files were externally changed while IDEA running.
     *
     * @param settings loaded component state
     */
    public void loadState(@NotNull final BuildrApplicationSettings settings) {
        setBuildrPath(settings.getBuildrPath());
    }

    public String getBuildrPath() {
        return buildrPath;
    }

    public void setBuildrPath(String buildrPath) {
        this.buildrPath = buildrPath;
    }

    public boolean isValidBuildrPath(String buildrPath) {
        File executable = new File(buildrPath);
        return executable.exists() && executable.isFile() && executable.getName().startsWith("buildr");
    }
}
