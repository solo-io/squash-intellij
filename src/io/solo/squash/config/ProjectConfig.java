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
    int requestTimeout;

    @com.intellij.util.xmlb.annotations.Transient
    boolean inWait = false;

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

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public void setInWait(boolean w) {
        this.inWait = w;
    }

    public boolean isInWait() {
        return this.inWait;
    }
}
