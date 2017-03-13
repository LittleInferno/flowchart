var NativeNode = com.littleinferno.flowchart.node.PluginNode;
var NativePin = com.littleinferno.flowchart.pin.Pin;
var NativeType = com.littleinferno.flowchart.DataType;
var NativeConnection = com.littleinferno.flowchart.Connection;
var NativeGenerator = com.littleinferno.flowchart.codegen.JSCodeGenerator;

var codegen = new NativeGenerator();

function exportNodes() {
    return [ifNode(),beginNode(),printNode() ];
}

function ifNode() {
    return {
        name: "if node",
        title: "if",
        gen: function (node) {
            var conditionStr = node.getPin("condition").generate(codegen);
            var trueString = node.getPin("True").generate(codegen);
            var falseString = node.getPin("False").isConnect() ? node.getPin("False").generate(codegen) :"";

            var string;
            if (!(!falseString || 0 === falseString.length))
                string = codegen.makeIfElse(conditionStr,
                        codegen.makeBlock(trueString), codegen.makeBlock(falseString));
            else
                string = codegen.makeIf(conditionStr, codegen.makeBlock(trueString));

            return string;
        },
        pins: {
            "ex in": {
                type: NativeType.EXECUTION,
                connection: NativeConnection.INPUT
            },
            "condition":{
                type: NativeType.BOOL,
                connection: NativeConnection.INPUT
            },
            "True": {
                type: NativeType.EXECUTION,
                connection: NativeConnection.OUTPUT
            },
            "False": {
                type: NativeType.EXECUTION,
                connection: NativeConnection.OUTPUT
            },
        }
    }
}

function beginNode() {
    return {
        name: "begin node",
        title: "begin",
        gen: function (node) {
            return node.getPin("start").isConnect() ? node.getPin("start").generate(codegen):"";
        },
        pins: {
            "start": {
                type: NativeType.EXECUTION,
                connection: NativeConnection.OUTPUT
            }
        },
        programstart : true
    }
}

function printNode() {
    return {
        name: "print node",
        title: "print",
        gen: function (node) {

                var valStr = node.getPin("item").generate(codegen);

                var nextStr = node.getPin("ex out").isConnect() ? node.getPin("ex out").generate(codegen) : "";

                var format = "com.littleinferno.flowchart.jsutil.IO.print("+valStr+")";

                return codegen.makeStatement(format) + nextStr;
        },
        pins: {
            "ex in": {
                type: NativeType.EXECUTION,
                connection: NativeConnection.INPUT
            },
            "ex out": {
                type: NativeType.EXECUTION,
                connection: NativeConnection.OUTPUT
            },
            "item": {
                type: [NativeType.BOOL, NativeType.INT, NativeType.FLOAT, NativeType.STRING],
                connection: NativeConnection.INPUT
            },
        },
    }
}