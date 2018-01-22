package io.solo.squash.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import io.solo.squash.DebugTarget;

public class DebugContainer extends AnAction {

    public DebugContainer() {
        super("Squash: Debug Container");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        DebugTarget target = new DebugTarget(project);
        target.debugContainer();
    }

}
