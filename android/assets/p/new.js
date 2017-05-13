// var NativeNode = com.littleinferno.flowchart.node.AndroidNode;
var NativeNode = com.littleinferno.flowchart.node.BaseNode;

var NativeType = com.littleinferno.flowchart.DataType;
var Connection = com.littleinferno.flowchart.Connection;

var NativeConnection = com.littleinferno.flowchart.Connection;
var NativeGenerator = com.littleinferno.flowchart.codegen.JSCodeGenerator;
var PluginParams = com.littleinferno.flowchart.plugin.AndroidBasePluginHandle.PluginParams;

var EditText = com.littleinferno.flowchart.plugin.bridge.TextField;
var FunctionDropDown = com.littleinferno.flowchart.plugin.bridge.FunctionSpinner;
var VariableSpinner = com.littleinferno.flowchart.plugin.bridge.VariableSpinner;

var LOG = android.util.Log;

var codegen = new NativeGenerator();

var Project = com.littleinferno.flowchart.project.FlowchartProject;

function pluginParams() {
    return new PluginParams("basic", "basic plugin for test", "0.0.3", 500);
}

function exportNodes() {
    return [addNode(), subNode(), mulNode(), divNode(), equalNode(),
    lessNode(), greatNode(), integerNode(), floatNode(), stringNode(), variableSetNode(),
    variableGetNode(), printNode(),
    functionBeginNode(), functionReturnNode(), functionCallNode()];
}

function exportRules() {
    return {
        keyWords: [
            "abstract", "arguments", "await", "boolean",
            "break", "byte", "case", "catch",
            "char", "class", "const", "continue",
            "debugger", "default", "delete", "do",
            "double", "else", "enum", "eval",
            "export", "extends", "false", "final",
            "finally", "float", "for", "function",
            "goto", "if", "implements", "import",
            "in", "instanceof", "int", "interface",
            "let", "long", "native", "new",
            "null", "package", "private", "protected",
            "public", "return", "short", "static",
            "super", "switch", "synchronized", "this",
            "throw", "throws", "transient", "true",
            "try", "typeof", "var", "void",
            "volatile", "while", "with", "yield"],

        variableIsAvailable: true,
        functionIsAvailable: true,
        pattern: "[$a-zA-Z_][0-9a-zA-Z_$]*",
        entryPoint: "main"
    };
}

function makeVariable(name, value) {
    return "var " + name + " = " + value + ";";
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
        node.addView(NativeNode.Align.CENTER, editText);
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
        //      node.addView(NativeNode.Align.CENTER,editText);
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
        node.addView(NativeNode.Align.CENTER, editText);
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

function functionBeginNode() {
    var init = function (node) {
        node.addExecutionOutputPin("begin");

        // this.function.addListener(node.setT);
        // this.function.addListener(this::close);

    }

    var initFun = function (node, fun) {
        node.setTitle("begin " + fun.getName())

        // fun.addListener(function (name) { node.setTitle("begin " + name) });
        fun.addParameterListener(function (parameter) {
            if (parameter.getConnection() == Connection.INPUT) {
                pin = node.addDataOutputPin(parameter.getName(), parameter.isArray(), parameter.getDataType());

                parameter.onNameChange(function (change) { pin.setName(change) });
                parameter.onTypeChange(function (change) { pin.setType(change) });
                parameter.onArrayChange(function (change) { pin.setArray(change) });
            }
        }, function (parameter) {
            if (parameter.getConnection() == Connection.INPUT)
                node.removePin(parameter.getName());
        });

        fun.applyParameters();
    }

    var gen = function (node) {
        // return codegen.makeExpr(codegen.makeString(editText.getText()));
        return "";
    }

    var genFun = function (node, fun) {

        var params = fun.getInputParameters();
        var paramsStr = "";

        for (var i = 0; i < params.length; i++) {
            paramsStr += params[i].getName();
            if (i != params.length - 1)
                paramsStr += ", ";
        }

        return "function " + fun.getName() + "(" + paramsStr + "){\n" + node.getPin("begin").generate() + "}";
    }

    return {
        name: "function begin node",
        title: "begin",
        category: "system",
        gen: gen,
        init: init,
        attributes: [{ "functionInit": initFun }, { "functionGen": genFun }, { "closable": false }],
    }
}

function functionReturnNode() {

    var init = function (node) {
        node.addExecutionInputPin("end");
    }

    var initFun = function (node, fun) {
        node.setTitle("return " + fun.getName())

        // fun.addListener(function (name) { node.setTitle("begin " + name) });
        fun.addParameterListener(function (parameter) {
            if (parameter.getConnection() == Connection.OUTPUT) {
                pin = node.addDataInputPin(parameter.getName(), parameter.isArray(), parameter.getDataType());

                parameter.onNameChange(function (change) { pin.setName(change) });
                parameter.onTypeChange(function (change) { pin.setType(change) });
                parameter.onArrayChange(function (change) { pin.setArray(change) });
            }
        }, function (parameter) {
            if (parameter.getConnection() == Connection.OUTPUT) {
                node.removePin(parameter.getName());
            }
        }
        );
    }

    var gen = function (node, pin) {

        params = node.getFunction().getOutputParameters();
        if (params.length = 0)
            return "\n";

        if (params.length == 1)
            return "return " + node.getPin(params[0].getName()).generate() + ";";

        var paramsStr = "return {\n";

        for (var i = 0; i < params.length; i++) {
            paramsStr += params[i].getName() + ":" + node.getPin(params[i].getName()).generate();
            if (i != params.length - 1)
                paramsStr += ",\n";
        }
        paramsStr += "};\n";
        return paramsStr;
    }

    return {
        name: "function return node",
        title: "begin",
        category: "function",
        gen: gen,
        init: init,
        attributes: [{ "functionInit": initFun },],
    }
}

function functionCallNode() {

    var init = function (node) {
        node.addExecutionInputPin("in");
        var next = node.addExecutionOutputPin("out");

        var hash = -1;
        var drop = FunctionDropDown.make(node, function (fun) {
            if (hash != -1)
                fun.removeParameterListener(hash);

            hash = fun.addParameterListener(function (parameter) {
                if (parameter.getConnection() == Connection.INPUT)
                    pin = node.addDataInputPin(parameter.getName(), parameter.isArray(), parameter.getDataType());
                else
                    pin = node.addDataOutputPin(parameter.getName(), parameter.isArray(), parameter.getDataType());

                parameter.onNameChange(function (change) { pin.setName(change) });
                parameter.onTypeChange(function (change) { pin.setType(change) });
                parameter.onArrayChange(function (change) { pin.setArray(change) });
            }, function (parameter) {
                node.removePin(parameter.getName());
            }
            );
            fun.applyParameters();
        });
        node.addView(NativeNode.Align.CENTER, drop);
    }

    var gen = function (node) {
        return "";
    }

    return {
        name: "function call node",
        title: "call",
        category: "function",
        gen: gen,
        init: init,
    }
}


function printNode() {

    var init = function (node) {
        node.addExecutionInputPin("in");
        node.addDataInputPin("data", false, [NativeType.INT, NativeType.FLOAT, NativeType.STRING, NativeType.BOOL]);
        node.addExecutionOutputPin("out");
    }

    var gen = function (node, pin) {
        var dataStr = node.getPin("data").generate();
        var outStr = node.getPin("out").generate();

        var format = "LOG.i(\"\"," + dataStr + ");\n" + outStr;

        return format;
    }

    return {
        name: "print node",
        title: "print",
        category: "basic java script",
        gen: gen,
        init: init,
    }
}

function variableSetNode() {
    var m_var = null;

    var init = function (node) {
        node.addExecutionInputPin("in");
        var next = node.addExecutionOutputPin("out");
        var pin = node.addDataInputPin("data", false, NativeType.UNIVERSAL);

        var m_n = function (change) { pin.setType(change) };
        var m_t = function (change) { pin.setArray(change) };

        var drop = VariableSpinner.make(node, function (variable) {
            if (m_var) {
                pin.disconnectAll();
                m_var.removeTypeChangeListener(m_n);
                m_var.removeArrayChangeListener(m_t);
            }

            m_var = variable;
            if (m_var) {
                m_var.onTypeChange(m_n);
                m_var.onArrayChange(m_t);
                pin.setArray(m_var.isArray());
                pin.setType(m_var.getDataType())
            }
        });
        node.addView(NativeNode.Align.CENTER, drop);
    }

    var gen = function (node) {
        return "";
    }

    return {
        name: "variable set node",
        title: "set",
        category: "variable",
        gen: gen,
        init: init,
    }
}

function variableGetNode() {
    var m_var = null;

    var init = function (node) {
        var pin = node.addDataOutputPin("data", false, NativeType.UNIVERSAL);

        var m_n = function (change) { pin.setType(change) };
        var m_t = function (change) { pin.setArray(change) };

        var drop = VariableSpinner.make(node, function (variable) {
            if (m_var) {
                m_var.removeTypeChangeListener(m_n);
                m_var.removeArrayChangeListener(m_t);
            }

            m_var = variable;
            if (m_var) {
                m_var.onTypeChange(m_n);
                m_var.onArrayChange(m_t);
                pin.setArray(m_var.isArray());
                pin.setType(m_var.getDataType())
            }
        });
        node.addView(NativeNode.Align.CENTER, drop);
    }

    var gen = function (node) {
        return "";
    }

    return {
        name: "variable get node",
        title: "get",
        category: "variable",
        gen: gen,
        init: init,
    }
}