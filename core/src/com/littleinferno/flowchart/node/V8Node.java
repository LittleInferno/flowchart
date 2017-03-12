package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.codegen.BaseJSEngine;
import com.littleinferno.flowchart.pin.Pin;

public class V8Node extends Node {


    public V8Node(NodeHandle nodeHandle) {
        super(nodeHandle);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return null;
    }

    static void regInJs(BaseJSEngine baseJSEngine) {



    }

}
