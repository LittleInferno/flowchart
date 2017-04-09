var NativeNode = com.littleinferno.flowchart.node.PluginNode;
var NativeType = com.littleinferno.flowchart.DataType;
var NativeConnection = com.littleinferno.flowchart.Connection;
var NativeGenerator = com.littleinferno.flowchart.codegen.JSCodeGenerator;
var PluginParams = com.littleinferno.flowchart.plugin.AndroidBasePluginHandle.PluginParams;
var codegen = new NativeGenerator();

function pluginParams() {
    return new PluginParams("basic", "basic plugin for test", "0.0.1", 400);
}

function exportNodes() {
    return [addNode(), subNode(), mulNode(), divNode(), equalNode(), lessNode(), greatNode()];
}

function addNode() {
    var a;
    var b;
    var result;
    var possibleConvert = [NativeType.INT, NativeType.FLOAT, NativeType.STRING];

    var init = function (node) {
        a = node.addDataInputPin("A", possibleConvert);
        b = node.addDataInputPin("B", possibleConvert);
        result = node.addDataOutputPin("result", possibleConvert);
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
        a = node.addDataInputPin("A", possibleConvert);
        b = node.addDataInputPin("B", possibleConvert);
        result = node.addDataOutputPin("result", possibleConvert);
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
        a = node.addDataInputPin("A", possibleConvert);
        b = node.addDataInputPin("B", possibleConvert);
        result = node.addDataOutputPin("result", possibleConvert);
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
        a = node.addDataInputPin("A", possibleConvert);
        b = node.addDataInputPin("B", possibleConvert);
        result = node.addDataOutputPin("result", possibleConvert);
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
        a = node.addDataInputPin("A", possibleConvert);
        b = node.addDataInputPin("B", possibleConvert);
        result = node.addDataOutputPin("result", NativeType.BOOL);
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
        a = node.addDataInputPin("A", possibleConvert);
        b = node.addDataInputPin("B", possibleConvert);
        result = node.addDataOutputPin("result", NativeType.BOOL);
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
        a = node.addDataInputPin("A", possibleConvert);
        b = node.addDataInputPin("B", possibleConvert);
        result = node.addDataOutputPin("result", possibleConvert);
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
