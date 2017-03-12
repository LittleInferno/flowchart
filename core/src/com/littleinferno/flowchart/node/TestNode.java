package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class TestNode extends Node {

//    private  Pin init;

    public TestNode() {
        super(new NodeHandle("t", true));
        //  init();
    }

    // public abstract void init();

//    public static <T extends TestNode> Pin addExInPin(T testNode) {
//        return testNode.addExecutionInputPin();
//    }

    public static void add(TestNode testNode) {
        testNode.addExecutionInputPin();
    }

    static public TestNode testNode;


    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        //  return getStr();
        return null;
    }

    //  protected abstract String getStr();
}
