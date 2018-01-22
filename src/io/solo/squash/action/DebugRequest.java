package io.solo.squash.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import io.solo.squash.DebugTarget;
import io.solo.squash.ui.Helper;

public class DebugRequest extends AnAction {
    public DebugRequest() {
        super("Squash: Debug Request");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        DebugTarget target = new DebugTarget(project);
        target.debugRequest();
    }
}
