package io.solo.squash.debugger;

import com.intellij.debugger.impl.GenericDebuggerRunner;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RemoteConnection;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.remote.RemoteConfigurationType;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class JavaDebugger implements IDebugger {

    @Override
    public String getName() {
        return "java";
    }

    @Override
    public void startDebugging(Project project, String host, String port) throws Exception {
        JavaDebugRunner runner = new JavaDebugRunner(host, port);
        runner.execute(project);
    }
}

class JavaDebugRunner extends GenericDebuggerRunner {
    private String jdwpPort;
    private String jdwpHost;

    public JavaDebugRunner(String host, String port) {
        jdwpHost = host;
        jdwpPort = port;
    }

    @NotNull
    public String getRunnerId() {
        return "io.solo.squash.debugger.JavaDebugRunner";
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return true;
    }


    @Nullable
    @Override
    protected RunContentDescriptor createContentDescriptor(RunProfileState runProfileState, ExecutionEnvironment executionEnvironment) throws ExecutionException {
        RemoteConnection connection = new RemoteConnection(true, jdwpHost, jdwpPort, false);
        return attachVirtualMachine(runProfileState, executionEnvironment, connection, false);
    }


    public void execute(Project project) throws Exception {
        Executor exec = new DefaultDebugExecutor();

        RemoteConfigurationType rct = new RemoteConfigurationType();
        RunConfiguration ruc = rct.getFactory().createTemplateConfiguration(project);
        ExecutionEnvironment ev = ExecutionEnvironmentBuilder.create(exec, ruc).build();
        this.execute(ev);
    }
}
