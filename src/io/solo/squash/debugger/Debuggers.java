package io.solo.squash.debugger;

import com.intellij.openapi.components.ServiceManager;

import java.util.HashMap;

public class Debuggers {
    private HashMap<String,IDebugger> debuggers = new HashMap<>();

    public void register(IDebugger dbg) {
        if(dbg != null) {
            debuggers.put(dbg.getName(), dbg);
        }
    }

    public IDebugger get(String name) {
        return debuggers.get(name);
    }

    public String[] list() {
        return debuggers.keySet().toArray(new String[0]);
    }

    public int size() { return debuggers.size(); }

    public static Debuggers Init() {
        Debuggers dbgs = new Debuggers();

        dbgs.register(new JavaDebugger());

        return dbgs;
    }

}
