package io.solo.squash.system;

import com.google.gson.Gson;
import io.solo.squash.config.ApplicationConfig;
import io.solo.squash.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KubectlHelper {

    public static String runKubectl(Collection<String> args, boolean wait) throws Exception {
        ArrayList<String> cmds = new ArrayList<>(args.size() + 1);
        String kc = ApplicationConfig.getInstance().getKubectlPath();
        if(kc != null && !kc.isEmpty()) {
            cmds.add(kc);
        }
        else {
            cmds.add("kubectl");
        }
        cmds.addAll(args);
        return Helper.executeCommand(cmds.toArray(new String[0]), wait);
    }

    public static KubePodList getPods() throws Exception {
        String res = runKubectl(Arrays.asList("get","-o", "json", "pods", "--all-namespaces"), true);
        Gson gson = new Gson();
        return gson.fromJson(res, KubePodList.class);
    }

    public static KubeServiceList getServices() throws Exception {
        String res = runKubectl(Arrays.asList("get","-o", "json", "services"), true);
        Gson gson = new Gson();
        return gson.fromJson(res, KubeServiceList.class);
    }

    public static Collection<String> getImagesPerService(KubeService svc) throws Exception {
        ArrayList<String> images = new ArrayList<>();
        String lbl = "";
        boolean first = true;
        for(String k : svc.spec.selector.keySet()) {
            if(first)
                first = false;
            else
                lbl += ",";
            lbl += (k + "=" + svc.spec.selector.get(k));
        }
        String res = runKubectl(Arrays.asList("get","-o", "json", "pods", "-l", lbl), true);
        Gson gson = new Gson();
        KubePodList pods =  gson.fromJson(res, KubePodList.class);

        if(pods == null) {
            throw new Exception("Can't get pods: " + res);
        }

        for(KubePod p : pods.items) {
            for(KubeContainer c : p.spec.containers) {
                images.add(c.image);
            }
        }

        return images;
    }

    public static String portForward(String remote) throws Exception {
        String[] args = remote.split(":");
        if(args.length < 2) {
            return null;
        }
        String remotePort = args[1];

        String[] args1 = args[0].split("\\.");
        String remoteAddr = args1[0];
        String namespace = "squash";
        if(args1.length > 1) {
            namespace = args1[1];
        }

        String res = runKubectl(Arrays.asList("--namespace=" + namespace, "port-forward", remoteAddr, ":" + remotePort), false);
        Pattern pattern = Pattern.compile("from\\s+.+:(\\d+)\\s+->");
        Matcher match = pattern.matcher(res);
        if(match.find()) {
            return match.group(1);
        }
        else {
            throw new Exception("Failed to setup port forwarding: " + res);
        }
    }

}
