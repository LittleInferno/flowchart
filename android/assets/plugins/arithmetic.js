var NativeNode = com.littleinferno.flowchart.node.PluginNode;
var NativeType = com.littleinferno.flowchart.DataType;
var NativeConnection = com.littleinferno.flowchart.Connection;
var NativeGenerator = com.littleinferno.flowchart.codegen.JSCodeGenerator;

var codegen = new NativeGenerator();

function exportNodes() {
    return [addNode(), subNode(), mulNode(), divNode(), equalNode(), lessNode(), greatNode()];
}

function addNode() {
    return {
        name: "add node",
        title: "add",
        gen: function (node) {
            return codegen.makeAdd(node.getPin("a"), node.getPin("b"));
        },
        pins: {
            "a": {
                type: [NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            "b": {
                type: [NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            "result": {
                type: [NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.OUTPUT
            },
        },
        typeAlwaysEqual: ["a", "b", "result"]
    }
}

function subNode() {
    return {
        name: "sub node",
        title: "sub",
        gen: function (node) {
            return codegen.makeSub(node.getPin("a"), node.getPin("b"));
        },
        pins: {
            "a": {
                type: [NativeType.INT, NativeType.FLOAT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            "b": {
                type: [NativeType.INT, NativeType.FLOAT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            "result": {
                type: [NativeType.INT, NativeType.FLOAT, NativeType.FLOAT],
                connection: NativeConnection.OUTPUT
            },
        },
        typeAlwaysEqual: ["a", "b", "result"]
    }
}

function mulNode() {
    return {
        name: "mul node",
        title: "mul",
        gen: function (node) {
            return codegen.makeMul(node.getPin("a"), node.getPin("b"));
        },
        pins: {
            "a": {
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            "b": {
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            "result": {
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.OUTPUT
            },
        },
        typeAlwaysEqual: ["a", "b", "result"]
    }
}

function divNode() {
    return {
        name: "div node",
        title: "div",
        gen: function (node) {
            return codegen.makeAdd(node.getPin("a"), node.getPin("b"));
        },
        pins: {
            "a": {
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            "b": {
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            "result": {
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.OUTPUT
            },
        },
        typeAlwaysEqual: ["a", "b", "result"]
    }
}

function equalNode() {
    return {
        name: "equal node",
        title: "equal",
        gen: function (node) {
            return codegen.makeAdd(this.makeEq("a"), node.getPin("b"));
        },
        pins: {
            "a": {
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            "b": {
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            "result": {
                type: NativeType.BOOL,
                connection: NativeConnection.OUTPUT
            },
        },
        typeAlwaysEqual: ["a", "b"]
    }
}

function lessNode() {
    return {
        name: "less node",
        title: "less",
        gen: function (node) {
            return codegen.makeLt(node.getPin("a"), node.getPin("b"));
        },
        pins: {
            "a": {
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            "b": {
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            "result": {
                type: NativeType.BOOL,
                connection: NativeConnection.OUTPUT
            },
        },
        typeAlwaysEqual: ["a", "b"]
    }
}

function greatNode() {
    return {
        name: "great node",
        title: "great",
        gen: function (node) {
            return codegen.makeGt(node.getPin("a"), node.getPin("b"));
        },
        pins: {
            "a": {
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            "b": {
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            "result": {
                type: NativeType.BOOL,
                connection: NativeConnection.OUTPUT
            },
        },
        typeAlwaysEqual: ["a", "b"]
    }
}
