package io.solo.squash.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import io.solo.squash.DebugTarget;
import io.solo.squash.ui.Helper;

public class DebugRelease extends AnAction {

    public DebugRelease() {
        super("Squash: Release Debug Session");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        DebugTarget target = new DebugTarget(project);
        target.detachDebugger();
    }
}
