package com.littleinferno.flowchart.node;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class JSNode {

    public static NodeManager nodeManager;


//    public static String createNode(String title) {
//        return nodeManager.createNode(N.class, new Node.NodeHandle(title, true)).getId();
//    }

    public static void addInExPin(String id) {
        nodeManager.getNode(id).addExecutionInputPin();
    }

    public static void addOutExPin(String id) {
        nodeManager.getNode(id).addExecutionOutputPin();
    }

    public static void v(ScriptObjectMirror mirror) {
        System.out.print(mirror.toString());

    }

    public static void registerNode(ScriptObjectMirror object) {

        String nodeName = (String) object.get("name");
        String nodeTitle = (String) object.get("title");


        ScriptObjectMirror pins = (ScriptObjectMirror) object.get("pins");

        Pin[] nodePins = Stream.of(pins.getOwnKeys(true)).map(name -> {
            ScriptObjectMirror nested = (ScriptObjectMirror) pins.get(name);
            DataType type = (DataType) nested.get("title");
            Connection connection = (Connection) nested.get("connection");

            return new Pin(null, name, connection, type);
        }).toArray(Pin[]::new);

        if (handles == null) handles = new HashMap<>();
        handles.put(nodeName, new JSNodeHandle(nodeName, nodeTitle, nodePins));
    }

    static Set<String> getNodeList() {
        return handles.keySet();
    }

    public static Node createNode(String type) {
        return new N(handles.get(type));
    }

    static Map<String, JSNodeHandle> handles;

    public static class N extends Node {

        public N(JSNodeHandle nodeHandle) {
            super(new NodeHandle(nodeHandle.name, true));

            addPins(nodeHandle.pins);
        }

        public N(NodeHandle nodeHandle) {
            super(nodeHandle);
        }

        @Override
        public String gen(BaseCodeGenerator builder, Pin with) {
            return String.format("com.littleinferno.flowchart.jsutil.IO.print(%s)", "lkj");
        }

    }

    public static class JSNodeHandle {
        public String name;
        public String title;
        public Pin[] pins;

        public JSNodeHandle(String name, String title, Pin[] pins) {
            this.name = name;
            this.title = title;
            this.pins = pins;
        }
    }

}
