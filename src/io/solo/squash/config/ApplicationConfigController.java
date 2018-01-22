package io.solo.squash.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import io.solo.squash.ui.ApplicationConfigGUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ApplicationConfigController implements SearchableConfigurable {

    ApplicationConfigGUI appGui;
    ApplicationConfig config;


    public ApplicationConfigController() {
        config = ApplicationConfig.getInstance();
    }

    @NotNull
    @Override
    public String getId() {
        return "preferences.SquashDebuggerApplication";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Squash Debugger Plugin";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        appGui = new ApplicationConfigGUI();
        appGui.setTextKubectlPath(config.getKubectlPath());
        appGui.setTextKubectlProxy(config.getKubectlProxy());
        appGui.setTextSquashPath(config.getSquashPath());
        appGui.setTextSquashURL(config.getSquashUrl());
        return appGui.getPanelRoot();
    }

    @Override
    public void disposeUIResources() {
        appGui = null;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        config.setKubectlPath(appGui.getTextKubectlPath());
        config.setKubectlProxy(appGui.getTextKubectlProxy());
        config.setSquashPath(appGui.getTextSquashPath());
        config.setSquashUrl(appGui.getTextSquashURL());
    }
}
