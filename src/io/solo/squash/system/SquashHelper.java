package io.solo.squash.system;

import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import io.solo.squash.config.ApplicationConfig;
import io.solo.squash.config.ProjectConfig;
import io.solo.squash.model.SquashDebugAttachment;
import io.solo.squash.model.SquashDebugRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SquashHelper {

    static final int DefRequestTimeout = 60;

    public static String runSquash(Collection<String> args) throws Exception {
        ArrayList<String> cmds = new ArrayList<>(args.size() + 1);
        String url = ApplicationConfig.getInstance().getSquashUrl();
        String sq = ApplicationConfig.getInstance().getSquashPath();

        if(sq != null && !sq.isEmpty()) {
            cmds.add(sq);
        }
        else {
            cmds.add("squash");
        }

        if(url != null && !url.isEmpty()) {
            cmds.add("--url=" + url);
        }
        cmds.add("--json=true");

        cmds.addAll(args);

        return Helper.executeCommand(cmds.toArray(new String[0]));
    }

    public static String requestAttachment(Project project,
                                           String imageId,
                                           String podNamespace,
                                           String podName,
                                           String container,
                                           String debugger) throws Exception {

        String prcName = ProjectConfig.getInstance(project).getProcessName();

        String res;
        if(prcName != null && !prcName.isEmpty()) {
            res = runSquash(
                    Arrays.asList("debug-container", "--namespace=" + podNamespace, imageId, podName, container, debugger, "-p", prcName));
        }
        else {
            res = runSquash(
                    Arrays.asList("debug-container", "--namespace=" + podNamespace, imageId, podName, container, debugger));
        }
        Gson gson = new Gson();
        SquashDebugAttachment att =  gson.fromJson(res, SquashDebugAttachment.class);

        if(att == null) {
            throw new Exception("squash debug-container ... returned empty response");
        }
        if(att.metadata == null) {
            throw new Exception("squash debug-container ... returned invalid response: " + att);
        }
        return att.metadata.name;
    }

    public static SquashDebugAttachment waitForAttachment(String debugId) throws Exception {
        String res = runSquash(Arrays.asList("wait", debugId));
        Gson gson = new Gson();
        return gson.fromJson(res, SquashDebugAttachment.class);
    }

    public static String debugRequest(Project project,
                                      String imageId,
                                      String debugger) throws Exception {

        String prcName = ProjectConfig.getInstance(project).getProcessName();

        String res;
        if(prcName != null && !prcName.isEmpty()) {
            res = runSquash(
                    Arrays.asList("debug-request", imageId, debugger, "-p", prcName));
        }
        else {
            res = runSquash(
                    Arrays.asList("debug-request", imageId, debugger));
        }
        Gson gson = new Gson();
        SquashDebugRequest att =  gson.fromJson(res, SquashDebugRequest.class);
        if(att == null) {
            throw new Exception("squash debug-request ... returned empty response");
        }
        if(att.metadata == null) {
            throw new Exception("squash debug-request ... returned invalid response: " + att);
        }
        return att.metadata.name;
    }

    public static SquashDebugRequest listDebugRequests(String debugId) throws Exception {
        String res = runSquash(Arrays.asList("list", "debugrequests", debugId));
        Gson gson = new Gson();
        return gson.fromJson(res, SquashDebugRequest.class);
    }


    public static String waitForDebugRequest(Project project, String debugId) throws Exception {

        int timeout = ProjectConfig.getInstance(project).getRequestTimeout();

        if(timeout < 10) {
            timeout = DefRequestTimeout;
        }

        ProjectConfig.getInstance(project).setInWait(true);
        for(int i = 0; i < timeout && ProjectConfig.getInstance(project).isInWait(); i ++) {
            SquashDebugRequest dr = listDebugRequests(debugId);
            if(dr != null && dr.status.debug_attachment_ref != null) {
                return dr.status.debug_attachment_ref;
            }
            Thread.sleep(1000);
        }
        ProjectConfig.getInstance(project).setInWait(false);
        return null;
    }
}
