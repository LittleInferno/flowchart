var NativeNode = com.littleinferno.flowchart.node.PluginNode;
var NativeType = com.littleinferno.flowchart.DataType;
var NativeConnection = com.littleinferno.flowchart.Connection;
var NativeGenerator = com.littleinferno.flowchart.codegen.JSCodeGenerator;

function exportNodes() {
    return [addNode(), subNode(), mulNode(), divNode(), equalNode(), lessNode(), greatNode()];
}

function addNode() {
    return {
        name: "add node",
        title: "add",
        gen: function (node, codegen) {
            return codegen.makeAdd(node.getPin("a"), node.getPin("b"));
        },
        pins: [
            {
                name: "a",
                type: [NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            {
                name: "b",
                type: [NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            {
                name: "result",
                type: [NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.OUTPUT
            },
        ],
        typeAlwaysEqual: ["a", "b", "result"]
    }
}

function subNode() {
    return {
        name: "sub node",
        title: "sub",
        gen: function (node, codegen) {
            return codegen.makeSub(node.getPin("a"), node.getPin("b"));
        },
        pins: [
            {
                name: "a",
                type: [NativeType.INT, NativeType.FLOAT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            {
                name: "b",
                type: [NativeType.INT, NativeType.FLOAT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            {
                name: "result",
                type: [NativeType.INT, NativeType.FLOAT, NativeType.FLOAT],
                connection: NativeConnection.OUTPUT
            },
        ],
        typeAlwaysEqual: ["a", "b", "result"]
    }
}

function mulNode() {
    return {
        name: "mul node",
        title: "mul",
        gen: function (node, codegen) {
            return codegen.makeMul(node.getPin("a"), node.getPin("b"));
        },
        pins: [
            {
                name: "a",
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            {
                name: "b",
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            {
                name: "result",
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.OUTPUT
            },
        ],
        typeAlwaysEqual: ["a", "b", "result"]
    }
}

function divNode() {
    return {
        name: "div node",
        title: "div",
        gen: function (node, codegen) {
            return codegen.makeAdd(node.getPin("a"), node.getPin("b"));
        },
        pins: [
            {
                name: "a",
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            {
                name: "b",
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.INPUT
            },
            {
                name: "result",
                type: [NativeType.INT, NativeType.FLOAT],
                connection: NativeConnection.OUTPUT
            },
        ],
        typeAlwaysEqual: ["a", "b", "result"]
    }
}

function equalNode() {
    return {
        name: "equal node",
        title: "equal",
        gen: function (node, codegen) {
            return codegen.makeEq(node.getPin("a"), node.getPin("b"));
        },
        pins: [
            {
                name: "a",
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            {
                name: "b",
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            {
                name: "result",
                type: NativeType.BOOL,
                connection: NativeConnection.OUTPUT
            },
        ],
        typeAlwaysEqual: ["a", "b"]
    }
}

function lessNode() {
    return {
        name: "less node",
        title: "less",
        gen: function (node, codegen) {
            return codegen.makeLt(node.getPin("a"), node.getPin("b"));
        },
        pins: [
            {
                name: "a",
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            {
                name: "b",
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            {
                name: "result",
                type: NativeType.BOOL,
                connection: NativeConnection.OUTPUT
            },
        ],
        typeAlwaysEqual: ["a", "b"]
    }
}

function greatNode() {
    return {
        name: "great node",
        title: "great",
        gen: function (node, codegen) {
            return codegen.makeGt(node.getPin("a"), node.getPin("b"));
        },
        pins: [
            {
                name: "a",
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            {
                name: "b",
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
            {
                name: "result",
                type: NativeType.BOOL,
                connection: NativeConnection.OUTPUT
            },
        ],
        typeAlwaysEqual: ["a", "b"]
    }
}
