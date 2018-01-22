package io.solo.squash.debugger;

import java.util.ArrayList;
import java.util.HashMap;

public class Debuggers {
    private HashMap<String,IDebugger> debuggers = new HashMap<>();

    public void register(String name, IDebugger dbg) {
        debuggers.put(name, dbg);
    }

    public IDebugger get(String name) {
        return debuggers.get(name);
    }

    public String[] list() {
        return (String[])debuggers.keySet().toArray();
    }
}
