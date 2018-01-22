package io.solo.squash.config;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;


@State(
        name="SquashProjectConfig"
)
public class ProjectConfig implements PersistentStateComponent<ProjectConfig> {

    String remotePath;
    String processName;

    @Nullable
    @Override
    public ProjectConfig getState() {
        return this;
    }

    @Override
    public void loadState(ProjectConfig projectConfig) {
        XmlSerializerUtil.copyBean(projectConfig, this);
    }

    @Nullable
    public static ProjectConfig getInstance(Project project) {
        return ServiceManager.getService(project, ProjectConfig.class);
    }

    public String getProcessName() {
        return processName;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }
}
