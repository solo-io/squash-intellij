package io.solo.squash;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SelectFromListDialog;
import io.solo.squash.debugger.JavaDebugRunner;
import io.solo.squash.model.*;
import io.solo.squash.system.KubectlHelper;
import io.solo.squash.system.SquashHelper;
import io.solo.squash.ui.Helper;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class DebugTarget {
    private static Logger Log = Logger.getInstance(DebugTarget.class);
    private Project project;

    public DebugTarget(Project p) {
        project = p;
    }


    public void debugContainer() {
        try {
            KubePodList pods = KubectlHelper.getPods();
            KubePod pod = (KubePod) Helper.showSelection(project, "Select a pod", pods.items, new SelectFromListDialog.ToStringAspect() {
                @Override
                public String getToStirng(Object o) {
                    KubePod p = (KubePod) o;
                    return String.format("%s (%s)", p.metadata.name, p.spec.nodeName);
                }
            });

            if (pod == null) {
                return;
            }

            KubeContainer ctr = (KubeContainer) Helper.showSelection(project, "Select a container", pod.spec.containers, new SelectFromListDialog.ToStringAspect() {
                @Override
                public String getToStirng(Object o) {
                    KubeContainer k = (KubeContainer) o;
                    return String.format("%s - %s", k.name, k.image);
                }
            });

            if (ctr == null) {
                return;
            }

            ProgressManager.getInstance().run(new Task.Backgroundable(project, "Squash Debug Container") {
                public void run(@NotNull ProgressIndicator progressIndicator) {
                    try {
                        progressIndicator.setText("Squash - Debug Container");
                        progressIndicator.setFraction(0.20);

                        String dbgId = SquashHelper.requestAttachment(project, ctr.image, pod.metadata.namespace, pod.metadata.name, ctr.name, "java");
                        if (dbgId == null) {
                            Log.error("Can't attach to container");
                            //Helper.showErrorMessage(project, "Cannot debug remote system", "Can't attach");
                            throw new Exception("Can't attach to container");
                        }
                        progressIndicator.setFraction(0.40);
                        SquashDebugAttachment att = SquashHelper.waitForAttachment(dbgId);

                        if (att == null || att.status == null || !"attached".equals(att.status.state)) {
                            Log.error("Wait for attach failed");
//                            Helper.showErrorMessage(project, "Cannot attach to remote system", "Wait for attach failed");
                            throw new Exception("Wait for attach failed");
                            //return;
                        }
                        progressIndicator.setFraction(0.60);
                        String remote = att.status.debug_server_address;
                        String port = KubectlHelper.portForward(remote);
                        if (port == null || port.isEmpty()) {
                            Log.error("Cannot setup port forwarding");
                            //Helper.showErrorMessage(project, "Cannot setup port forwarding to remote system", remote);
                            //return;
                            throw new Exception("Cannot setup port forwarding");
                        }
                        progressIndicator.setFraction(0.80);
                        ApplicationManager.getApplication().invokeLater(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    Helper.showInfoMessage(project, "Debugging Remote", remote);

                                    JavaDebugRunner runner = new JavaDebugRunner("127.0.0.1", port);
                                    runner.execute(project);
                                }
                                catch(Exception ex) {
                                    Log.error(ex);
                                    Helper.showErrorMessage(project, "Debug Request Failed to Start Debugger", ex.toString());
                                }
                            }
                        });

                    } catch (Exception ex) {
                        ApplicationManager.getApplication().invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                Helper.showErrorMessage(project, "Debug Container Failed", ex.toString());
                            }
                        });
                    }
                }
            });
        }
        catch (Exception ex) {
            Log.error(ex);
            Helper.showErrorMessage(project,"Debug Container Exception", ex.toString());
        }
    }

    public void debugRequest() {
        final String customImageName = "-- Custom --";
        try {
            KubeServiceList svcList = KubectlHelper.getServices();
            KubeService svc = (KubeService) Helper.showSelection(project, "Select a pod", svcList.items, new SelectFromListDialog.ToStringAspect() {
                @Override
                public String getToStirng(Object o) {
                    KubeService p = (KubeService) o;
                    return p.metadata.name;
                }
            });

            if (svc == null) {
                return;
            }

            Collection<String> images = KubectlHelper.getImagesPerService(svc);
            images.add(customImageName);

            String image = (String)Helper.showSelection(project, "Select Image", images.toArray(), new SelectFromListDialog.ToStringAspect() {
                @Override
                public String getToStirng(Object o) {
                    return (String)o;
                }
            });

            if(image == null) {
                return;
            }

            if(customImageName.equals(image)) {
                image = Helper.getInput(project, "Image Name", "Image name as in appears in the pod spec");
                if(image == null) {
                    return;
                }
            }

            String startdbgId = SquashHelper.debugRequest(project, image, "java");

            ProgressManager.getInstance().run(new Task.Backgroundable(project, "Squash Debug Request"){
                public void run(@NotNull ProgressIndicator progressIndicator) {
                    try {
                        progressIndicator.setText("Squash - Debug Request");
                        progressIndicator.setFraction(0.20);
                        String dbgId = SquashHelper.waitForDebugRequest(startdbgId);
                        if(dbgId == null) {
                            Log.error("Timeout waiting for debug request");
                            throw new Exception("Timeout waiting for debug request");
                        }

                        progressIndicator.setFraction(0.40);
                        SquashDebugAttachment att = SquashHelper.waitForAttachment(dbgId);

                        if (att == null || att.status == null || !"attached".equals(att.status.state)) {
                            Log.error("Wait for attach failed");
                            throw new Exception("Wait for attach failed");
                        }

                        progressIndicator.setFraction(0.60);
                        String remote = att.status.debug_server_address;
                        String port = KubectlHelper.portForward(remote);
                        if (port == null || port.isEmpty()) {
                            Log.error("Cannot setup port forwarding");
                            throw new Exception("Cannot setup port forwarding");
                        }

                        progressIndicator.setFraction(0.80);

                        ApplicationManager.getApplication().invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Helper.showInfoMessage(project, "Debugging Remote", remote);

                                    JavaDebugRunner runner = new JavaDebugRunner("127.0.0.1", port);
                                    runner.execute(project);
                                }
                                catch (Exception ex) {
                                    Log.error(ex);
                                    Helper.showErrorMessage(project, "Debug Request Failed to Start Debugger", ex.toString());
                                }
                            }
                        });
                    }
                    catch (Exception ex) {
                        ApplicationManager.getApplication().invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                Helper.showErrorMessage(project, "Debug Request Failed", ex.toString());
                            }
                        });
                    }
                }
            });
        }
        catch (Exception ex) {
            Log.error(ex);
            Helper.showErrorMessage(project,"Debug Request Exception", ex.toString());
        }
    }

    public void detachDebugger() {
        if(Helper.askConfirmation(project, "Stop Debugging", "Stop Debugging session?")) {

        }
    }
}
