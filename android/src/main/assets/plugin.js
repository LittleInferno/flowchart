var NativeNode = com.littleinferno.flowchart.node.AndroidNode;

var NativeType = com.littleinferno.flowchart.util.DataType;
var Connection = com.littleinferno.flowchart.util.Connection;

var EditText = com.littleinferno.flowchart.plugin.bridge.TextField;
var FunctionDropDown = com.littleinferno.flowchart.plugin.bridge.FunctionSpinner;
var VariableSpinner = com.littleinferno.flowchart.plugin.bridge.VariableSpinner;


function exportNodes() {
    return [addNode(), subNode(), mulNode(), divNode(), equalNode(),
    lessNode(), greatNode(),greatEqNode(), integerNode(), floatNode(), stringNode(), variableSetNode(),
    variableGetNode(), printNode(),
    functionBeginNode(), functionReturnNode(), functionCallNode(), ifNode(), SqrtNode()];
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
        variableGen: function (name, type, isArray) {
            return ("var " + name + " = " + (isArray ? "[]" : type.getDefaultValue()) + ";");
        },
        functionIsAvailable: true,
        pattern: "[$a-zA-Z_][0-9a-zA-Z_$]*",
        entryPoint: "main"
    };
}

function makeVariable(name, value) {
    return "var " + name + " = " + value + ";";
}

var __Counter = 0;
function makeNamedValue(name) {
    return name + __Counter++;
}

function addNode() {
    var possibleConvert = [NativeType.INT, NativeType.FLOAT, NativeType.STRING];

    var init = function (node) {
        node.addDataInputPin("A", false, possibleConvert);
        node.addDataInputPin("B", false, possibleConvert);
        result = node.addDataOutputPin("result", false, possibleConvert);
    }

    var gen = function (node) {
        var a = node.getPin("A").generate();
        var b = node.getPin("B").generate();
        return "( " + a + " + " + b + " )";
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
        var a = node.getPin("A").generate();
        var b = node.getPin("B").generate();
        return "( " + a + " - " + b + " )";
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
        var a = node.getPin("A").generate();
        var b = node.getPin("B").generate();
        return "( " + a + " * " + b + " )";
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
        var a = node.getPin("A").generate();
        var b = node.getPin("B").generate();
        return "( " + a + " / " + b + " )";
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
        var a = node.getPin("A").generate();
        var b = node.getPin("B").generate();
        return "( " + a + " == " + b + " )";
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
        var a = node.getPin("A").generate();
        var b = node.getPin("B").generate();
        return "( " + a + " < " + b + " )";
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
        var a = node.getPin("A").generate();
        var b = node.getPin("B").generate();
        return "( " + a + " > " + b + " )";
    }

    return {
        name: "great node",
        title: "great",
        category: "arithmetic",
        gen: gen,
        init: init,
    }
}

function greatEqNode() {
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
        var a = node.getPin("A").generate();
        var b = node.getPin("B").generate();
        return "( " + a + " >= " + b + " )";
    }

    return {
        name: "great eq node",
        title: "great eq",
        category: "arithmetic",
        gen: gen,
        init: init,
    }
}

function integerNode() {

    var init = function (node) {
        node.addDataOutputPin("data", false, NativeType.INT);
        var editText = EditText.make("data", node, NativeType.INT);
        node.addView(NativeNode.Align.CENTER, editText);
    }

    var gen = function (node) {
        return EditText.getText(node, "data");
    }

    var load = function (node, attributes) {
        EditText.setText(node, "data", attributes[0])
    }

    var save = function (node) {
        return [EditText.getText(node, "data")];
    }

    return {
        name: "integer node",
        title: "integer",
        category: "basic java script",
        gen: gen,
        init: init,
        attributes: [{ "save": save }, { "load": load }]
    }
}

function floatNode() {

    var init = function (node) {
        node.addDataOutputPin("data", false, NativeType.FLOAT);
        var editText = EditText.make("data", node, NativeType.FLOAT);
        node.addView(NativeNode.Align.CENTER, editText);
    }

    var gen = function (node) {
        return EditText.getText(node, "data");
    }

    var load = function (node, attributes) {
        EditText.setText(node, "data", attributes[0])
    }

    var save = function (node) {
        return [EditText.getText(node, "data")];
    }

    return {
        name: "float node",
        title: "float",
        category: "basic java script",
        gen: gen,
        init: init,
        attributes: [{ "save": save }, { "load": load }]
    }
}

function stringNode() {

    var editText;

    var init = function (node) {
        node.addDataOutputPin("result", false, NativeType.STRING);
        var editText = EditText.make("data", node, NativeType.STRING);
        node.addView(NativeNode.Align.CENTER, editText);
    }

    var gen = function (node) {
        return ("\"" + EditText.getText(node, "data") + "\"");
    }

    var load = function (node, attributes) {
        EditText.setText(node, "data", attributes[0])
    }

    var save = function (node) {
        return [EditText.getText(node, "data")];
    }

    return {
        name: "string node",
        title: "string",
        category: "basic java script",
        gen: gen,
        init: init,
        attributes: [{ "save": save }, { "load": load }]
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

        var m_fun = null;
        var m_listener = null;

        var drop = FunctionDropDown.make("data", node, function (fun) {
            if (m_fun) {
                m_fun.removeParameterListener(m_listener);
                node.removeDataPin();
            }

            var n = function (change) { pin.setName(change) }
            var t = function (change) { pin.setType(change) }
            var a = function (change) { pin.setArray(change) }
            m_fun = fun;
            m_listener = m_fun.addParameterListener(function (parameter) {
                var pin;
                if (parameter.getConnection() == Connection.INPUT)
                    pin = node.addDataInputPin(parameter.getName(), parameter.isArray(), parameter.getDataType());
                else
                    pin = node.addDataOutputPin(parameter.getName(), parameter.isArray(), parameter.getDataType());

                parameter.onNameChange(n);
                parameter.onTypeChange(t);
                parameter.onArrayChange(a);
            }, function (parameter) {
                parameter.removeNameChange(n);
                parameter.removeTypeChange(t);
                parameter.removeArrayChange(a);
                node.removePin(parameter.getName());
            }
            );
            fun.applyParameters();
        });
        node.addView(NativeNode.Align.CENTER, drop);
    }

    var gen = function (node, pin) {
        var res = "";

        if (pin.getType() == NativeType.EXECUTION) {
            var parameters = node.getFunction().getOutputParameters().length;
            var call = makeNamedValue("tmpcall");
            node.putAttribute("call", call);

            var next = node.getPin("out").generate();

            var params = "";
            parameters = node.getFunction().getInputParameters();

            for (var i = 0; i < parameters.length; i++) {
                params += node.getPin(parameters[i].getName());
                if (i != parameters.length - 1)
                    params += ',';
            }

            return (call + " =  " + FunctionDropDown.getSelected(node, "data") + "(" + params + ");\n" + next);
        }

        if (node.getFunction().getOutputParameters().length == 1)
            return node.getAttribute("call");

        return (node.getAttribute("call") + '.' + pin.getName());
    }

    var load = function (node, attributes) {
        FunctionDropDown.setSelected(node, "data", attributes[0])
    }

    var save = function (node) {
        return [FunctionDropDown.getSelected(node, "data")];
    }

    return {
        name: "function call node",
        title: "call",
        category: "function",
        gen: gen,
        init: init,
        attributes: [{ "save": save }, { "load": load }]
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

        var format = "Console.log(" + dataStr + ");\n" + outStr;

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

        var drop = VariableSpinner.make("Spinner", node, function (variable) {
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
        return (VariableSpinner.getSelected(node, "Spinner") + " =  " + node.getPin("data").generate()+"\n"+
        node.getPin("out").generate());
    }

    var load = function (node, attributes) {
        VariableSpinner.setSelected(node, "Spinner", attributes[0])
    }

    var save = function (node) {
        return [VariableSpinner.getSelected(node, "Spinner")];
    }

    return {
        name: "variable set node",
        title: "set",
        category: "variable",
        gen: gen,
        init: init,
        attributes: [{ "save": save }, { "load": load }]
    }
}

function variableGetNode() {
    var m_var = null;

    var init = function (node) {
        var pin = node.addDataOutputPin("data", false, NativeType.UNIVERSAL);

        var m_n = function (change) { pin.setType(change) };
        var m_t = function (change) { pin.setArray(change) };

        var drop = VariableSpinner.make("Spinner", node, function (variable) {
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
        return VariableSpinner.getSelected(node, "Spinner");
    }

    var load = function (node, attributes) {
        VariableSpinner.setSelected(node, "Spinner", attributes[0])
    }

    var save = function (node) {
        return [VariableSpinner.getSelected(node, "Spinner")];
    }

    return {
        name: "variable get node",
        title: "get",
        category: "variable",
        gen: gen,
        init: init,
    }
}

function ifNode() {

    var init = function (node) {
        node.addExecutionInputPin("in");
        node.addDataInputPin("Condition", false, NativeType.BOOL);

        node.addExecutionOutputPin("true");
        node.addExecutionOutputPin("false");
    }

    var gen = function (node) {

        var condition = node.getPin("Condition").generate();

        var trueStr = node.getPin("true").generate();

        var faseStr = node.getPin("false").generate();

        var res = "if ( " + condition + ") {\n";
        res += trueStr;
        res += "}\n"

        if (faseStr) {
            res += "else {\n"
            res += faseStr;
            res += "}\n";
        }


        return res;
    }

    return {
        name: "if  node",
        title: "if",
        category: "basic java script",
        gen: gen,
        init: init,
    }
}


function SqrtNode() {

    var init = function (node) {
        node.addDataInputPin("in", false, NativeType.FLOAT, NativeType.INT);
        node.addDataOutputPin("out", false, NativeType.FLOAT, NativeType.INT);
    }

    var gen = function (node) {

        return "Math.sqrt(" + node.getPin("in").generate() +")";
    }

    return {
        name: "sqrt  node",
        title: "sqrt",
        category: "arithmetic",
        gen: gen,
        init: init,
    }
}

