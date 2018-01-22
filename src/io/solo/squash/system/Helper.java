package io.solo.squash.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Helper {

    public static String executeCommand(String[] cmds, boolean wait) throws Exception {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(cmds);

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        if(wait) {
            return stdIn.lines().collect(Collectors.joining());
        }
        else {
            return stdIn.readLine();
        }
    }
    public static String executeCommand(String[] cmds) throws Exception {
        return executeCommand(cmds, true);
    }
}
