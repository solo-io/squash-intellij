package io.solo.squash.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import io.solo.squash.ui.ProjectConfigGUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProjectConfigController implements SearchableConfigurable {

    ProjectConfigGUI prjGui;
    Project project;
    ProjectConfig config;


    ProjectConfigController(Project prj) {
        project = prj;
        config = ProjectConfig.getInstance(project);
    }

    @NotNull
    @Override
    public String getId() {
        return "preferences.SquashDebuggerProject";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Squash Debugger Project";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        prjGui = new ProjectConfigGUI();
        prjGui.setTextProcessName(config.getProcessName());
        prjGui.setTextRemotePath(config.getRemotePath());
        prjGui.setTextRequestTimeout(config.getRequestTimeout());
        return prjGui.getPanelRoot();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        config.setProcessName(prjGui.getTextProcessName());
        config.setRemotePath(prjGui.getTextRemotePath());
        config.setRequestTimeout(prjGui.getTextRequestTimeout());
    }
}
