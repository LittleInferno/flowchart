var NativeNode = com.littleinferno.flowchart.node.PluginNode;
var NativeType = com.littleinferno.flowchart.DataType;
var NativeConnection = com.littleinferno.flowchart.Connection;
var NativeGenerator = com.littleinferno.flowchart.codegen.JSCodeGenerator;

var codegen = new NativeGenerator();

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
        gen: gen,
        init: init,
        typeAlwaysEqual: ["a", "b", "result"]
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
        gen: gen,
        init: init,
        typeAlwaysEqual: ["a", "b", "result"]
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
        gen: gen,
        init: init,
        typeAlwaysEqual: ["a", "b", "result"]
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
        gen: gen,
        init: init,
        typeAlwaysEqual: ["a", "b", "result"]
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
        gen: gen,
        init: init,
        typeAlwaysEqual: ["a", "b"]
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
        gen: gen,
        init: init,
        typeAlwaysEqual: ["a", "b"]
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
        gen: gen,
        init: init,
        typeAlwaysEqual: ["a", "b"]
    }
}
