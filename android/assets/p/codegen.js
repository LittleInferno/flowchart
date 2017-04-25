var PluginParams = com.littleinferno.flowchart.plugin.AndroidBasePluginHandle.PluginParams;

function pluginParams() {
    return new PluginParams("basic code generator", "basic js code genearator", "0.0.1", 200);
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
        pattern:"[$a-zA-Z_][0-9a-zA-Z_$]*"
    };
}
