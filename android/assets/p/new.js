var NativeNode = com.littleinferno.flowchart.node.AndroidNode;
var NativeType = com.littleinferno.flowchart.DataType;
var NativeConnection = com.littleinferno.flowchart.Connection;
var NativeGenerator = com.littleinferno.flowchart.codegen.JSCodeGenerator;
var PluginParams = com.littleinferno.flowchart.plugin.AndroidBasePluginHandle.PluginParams;
var EditText = com.littleinferno.flowchart.plugin.bridge.TextField;
var codegen = new NativeGenerator();

function pluginParams() {
    return new PluginParams("basic", "basic plugin for test", "0.0.1", 400);
}

function exportNodes() {
    return [addNode(), subNode(), mulNode(), divNode(), equalNode(), lessNode(), greatNode(), integerNode(), floatNode(), stringNode()];
}

function addNode() {
    var a;
    var b;
    var result;
    var possibleConvert = [NativeType.INT, NativeType.FLOAT, NativeType.STRING];

    var init = function (node) {
        a = node.addDataInputPin("A", false, possibleConvert);
        b = node.addDataInputPin("B", false, possibleConvert);
        result = node.addDataOutputPin("result", false, possibleConvert);
    }

    var gen = function (node) {
        codegen.makeAdd(a, b);
    }

    return {
        name: "add node",
        title: "add",
        category: "arithmetic",
        gen: gen,
        init: init,
        attributes: [{ "sceneType": "any" }],
    }
}

function subNode() {
    var a;
    var b;
    var result;
    var possibleConvert = [NativeType.INT, NativeType.FLOAT, NativeType.FLOAT];

    var init = function (node) {
        a = node.addDataInputPin("A", false, possibleConvert);
        b = node.addDataInputPin("B", false, possibleConvert); false,
            result = node.addDataOutputPin("result", false, possibleConvert);
    }

    var gen = function (node) {
        codegen.makeSub(a, b);
    }

    return {
        name: "sub node",
        title: "sub",
        category: "arithmetic",
        gen: gen,
        init: init,
    }
}

function mulNode() {
    var a;
    var b;
    var result;
    var possibleConvert = [NativeType.INT, NativeType.FLOAT];

    var init = function (node) {
        a = node.addDataInputPin("A", false, possibleConvert);
        b = node.addDataInputPin("B", false, possibleConvert);
        result = node.addDataOutputPin("result", false, possibleConvert);
    }

    var gen = function (node) {
        codegen.makeMul(a, b);
    }

    return {
        name: "mul node",
        title: "mul",
        category: "arithmetic",
        gen: gen,
        init: init,
    }
}

function divNode() {
    var a;
    var b;
    var result;
    var possibleConvert = [NativeType.INT, NativeType.FLOAT];

    var init = function (node) {
        a = node.addDataInputPin("A", false, possibleConvert);
        b = node.addDataInputPin("B", false, possibleConvert);
        result = node.addDataOutputPin("result", false, possibleConvert);
    }

    var gen = function (node) {
        codegen.makeDiv(a, b);
    }

    return {
        name: "div node",
        title: "div",
        category: "arithmetic",
        gen: gen,
        init: init,
    }
}

function equalNode() {
    var a;
    var b;
    var result;
    var possibleConvert = [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING];

    var init = function (node) {
        a = node.addDataInputPin("A", false, possibleConvert);
        b = node.addDataInputPin("B", false, possibleConvert);
        result = node.addDataOutputPin("result", false, NativeType.BOOL);
    }

    var gen = function (node) {
        codegen.makeEq(a, b);
    }

    return {
        name: "equal node",
        title: "equal",
        category: "arithmetic",
        gen: gen,
        init: init,
    }
}

function lessNode() {
    var a;
    var b;
    var result;
    var possibleConvert = [NativeType.INT, NativeType.FLOAT, NativeType.STRING];

    var init = function (node) {
        a = node.addDataInputPin("A", false, possibleConvert);
        b = node.addDataInputPin("B", false, possibleConvert);
        result = node.addDataOutputPin("result", false, NativeType.BOOL);
    }

    var gen = function (node) {
        codegen.makeLt(a, b);
    }

    return {
        name: "less node",
        title: "less",
        category: "arithmetic",
        gen: gen,
        init: init,
    }
}

function greatNode() {
    var a;
    var b;
    var result;
    var possibleConvert = [NativeType.INT, NativeType.FLOAT, NativeType.STRING];

    var init = function (node) {
        a = node.addDataInputPin("A", false, possibleConvert);
        b = node.addDataInputPin("B", false, possibleConvert);
        result = node.addDataOutputPin("result", false, NativeType.BOOL);
    }

    var gen = function (node) {
        codegen.makeGt(a, b);
    }

    return {
        name: "great node",
        title: "great",
        category: "arithmetic",
        gen: gen,
        init: init,
    }
}

function integerNode() {

    var editText;

    var init = function (node) {
        node.addDataOutputPin("data", false, NativeType.INT);
        editText = EditText.make(node, NativeType.INT);
        node.addView(editText,NativeNode.Align.CENTER);
    }

    var gen = function (node) {
        return codegen.makeExpr(editText.getText());
    }

    return {
        name: "integer node",
        title: "integer",
        category: "basic java script",
        gen: gen,
        init: init,
    }
}

function floatNode() {

    var editText;

    var init = function (node) {
        node.addDataOutputPin("data", false, NativeType.FLOAT);
        editText = EditText.make(node, NativeType.FLOAT);
        node.addView(editText,NativeNode.Align.CENTER);
    }

    var gen = function (node) {
        return codegen.makeExpr(editText.getText());
    }

    return {
        name: "float node",
        title: "float",
        category: "basic java script",
        gen: gen,
        init: init,
    }
}

function stringNode() {

    var editText;

    var init = function (node) {
        node.addDataOutputPin("result", false, NativeType.STRING);
        editText = EditText.make(node, NativeType.STRING);
        node.addView(editText,NativeNode.Align.CENTER);
    }

    var gen = function (node) {
        return codegen.makeExpr(codegen.makeString(editText.getText()));
    }

    return {
        name: "string node",
        title: "string",
        category: "basic java script",
        gen: gen,
        init: init,
    }
}