package io.solo.squash.config;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(
        name="SquashApplicationConfig",
        storages = {@Storage("squash.solo.io.xml")}
)
public class ApplicationConfig implements PersistentStateComponent<ApplicationConfig> {

    String kubectlPath;
    String kubectlProxy;
    String squashPath;
    String squashUrl;

    @Nullable
    @Override
    public ApplicationConfig getState() {
        return this;
    }

    @Override
    public void loadState(ApplicationConfig applicationConfig) {
        XmlSerializerUtil.copyBean(applicationConfig, this);
    }

    @Nullable
    public static ApplicationConfig getInstance() {
        return ServiceManager.getService(ApplicationConfig.class);
    }

    public String getKubectlProxy() {
        return kubectlProxy;
    }

    public String getKubectlPath() {
        return kubectlPath;
    }

    public String getSquashPath() {
        return squashPath;
    }

    public String getSquashUrl() {
        return squashUrl;
    }

    public void setKubectlPath(String kubectlPath) {
        this.kubectlPath = kubectlPath;
    }

    public void setKubectlProxy(String kubectlProxy) {
        this.kubectlProxy = kubectlProxy;
    }

    public void setSquashPath(String squashPath) {
        this.squashPath = squashPath;
    }

    public void setSquashUrl(String squashUrl) {
        this.squashUrl = squashUrl;
    }
}
