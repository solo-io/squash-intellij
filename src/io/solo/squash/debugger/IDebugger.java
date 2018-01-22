package io.solo.squash.debugger;

import com.intellij.openapi.project.Project;

public interface IDebugger {
    public String getName();
    public void startDebugging(Project project, String host, String port) throws Exception;
}
