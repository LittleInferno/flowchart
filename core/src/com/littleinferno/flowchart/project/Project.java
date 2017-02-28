package com.littleinferno.flowchart.project;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.ui.VisUI;
import com.littleinferno.flowchart.JsonManger;
import com.littleinferno.flowchart.codegen.BaseCodeExecution;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.codegen.JSCodeExecution;
import com.littleinferno.flowchart.codegen.JSCodeGenerator;
import com.littleinferno.flowchart.function.FunctionManager;
import com.littleinferno.flowchart.gui.FunctionScene;
import com.littleinferno.flowchart.gui.MainScene;
import com.littleinferno.flowchart.gui.ProjectScreen;
import com.littleinferno.flowchart.gui.Scene;
import com.littleinferno.flowchart.gui.SceneManager;
import com.littleinferno.flowchart.gui.UIScene;
import com.littleinferno.flowchart.node.BeginNode;
import com.littleinferno.flowchart.node.BoolNode;
import com.littleinferno.flowchart.node.FloatNode;
import com.littleinferno.flowchart.node.IfNode;
import com.littleinferno.flowchart.node.IntegerNode;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.node.NodeManager;
import com.littleinferno.flowchart.node.StringNode;
import com.littleinferno.flowchart.node.math.AddNode;
import com.littleinferno.flowchart.node.math.DivNode;
import com.littleinferno.flowchart.node.math.MulNode;
import com.littleinferno.flowchart.node.math.SubNode;
import com.littleinferno.flowchart.util.ProjectException;
import com.littleinferno.flowchart.variable.VariableManager;

public class Project implements Json.Serializable {

    private static Project instance;

    private String name;
    private String location;

    private BaseCodeExecution codeExecution;
    private BaseCodeGenerator codeGenerator;
    private ProgramStart programStart;

    private VariableManager variableManager;
    private FunctionManager functionManager;
    private SceneManager sceneManager;
    private JsonManger jsonManger;

    private UIScene uiScene;

    private ProjectScreen projectScreen;

    public Project() {

    }

    public Project(String name, String location, BaseCodeGenerator codeGenerator, BaseCodeExecution codeExecution) {
        this.name = name;
        this.location = location;
        this.codeGenerator = codeGenerator;
        this.codeExecution = codeExecution;

        this.variableManager = new VariableManager();
        this.functionManager = new FunctionManager();
        this.sceneManager = new SceneManager();
        this.jsonManger = new JsonManger();


        this.uiScene = new UIScene();
        this.projectScreen = new ProjectScreen(uiScene);
    }

    private void initJsonManager() {
        jsonManger.addSerializer(NodeManager.class, new NodeManager.NodeManagerSerializer());
        jsonManger.addSerializer(FunctionScene.class, new Scene.SceneSerializer<>());
        jsonManger.addSerializer(MainScene.class, new Scene.SceneSerializer<>());
        jsonManger.addSerializer(SceneManager.class, new SceneManager.SceneManagerSerializer());

        jsonManger.addSerializer(IntegerNode.class, new Node.DefaultNodeSerializer<>());
        jsonManger.addSerializer(FloatNode.class, new Node.DefaultNodeSerializer<>());
        jsonManger.addSerializer(StringNode.class, new Node.DefaultNodeSerializer<>());
        jsonManger.addSerializer(BoolNode.class, new Node.DefaultNodeSerializer<>());
        jsonManger.addSerializer(IfNode.class, new Node.DefaultNodeSerializer<>());
        jsonManger.addSerializer(BeginNode.class, new Node.DefaultNodeSerializer<>());

        jsonManger.addSerializer(AddNode.class, new Node.DefaultNodeSerializer<>());
        jsonManger.addSerializer(SubNode.class, new Node.DefaultNodeSerializer<>());
        jsonManger.addSerializer(MulNode.class, new Node.DefaultNodeSerializer<>());
        jsonManger.addSerializer(DivNode.class, new Node.DefaultNodeSerializer<>());

        // TODO add all nodes
    }

    public static Project createProject(String name, String location, BaseCodeGenerator codeGenerator, BaseCodeExecution codeExecution) {
        if (instance != null)
            instance.save();

        if (!VisUI.isLoaded()) {
            if (Gdx.app.getType() == Application.ApplicationType.Android)
                VisUI.load("X2/uiskin.json");
            else
                VisUI.load("X1/uiskin.json");
        }

        instance = new Project(name, location, codeGenerator, codeExecution);
        instance.init();

        return instance;
    }

    public static Project loadProject(String name, String location) {
        if (instance != null)
            instance.save();

        if (!VisUI.isLoaded()) {
            if (Gdx.app.getType() == Application.ApplicationType.Android)
                VisUI.load("X2/uiskin.json");
            else
                VisUI.load("X1/uiskin.json");
        }

        instance = new Project(name, location, new JSCodeGenerator(), new JSCodeExecution());
        instance.init();

        FileHandle variables = Gdx.files.external(location).child("variables");
        instance.variableManager = new Json().fromJson(VariableManager.class, variables);

        FileHandle functions = Gdx.files.external(location).child("functions");
        instance.functionManager = new Json().fromJson(FunctionManager.class, functions);

        return null;
    }


    public static Project instance() {
        if (instance == null)
            throw new ProjectException("project not loaded");

        return instance;
    }

    private void init() {
        uiScene.init();
        codeExecution.init();
        initJsonManager();
    }

    public void save() {
        Gdx.files.external(location).mkdirs();
        Json json = new Json();

        FileHandle variables = Gdx.files.external(location).child("variables.json");
        variables.writeString(json.prettyPrint(variableManager), false);

        FileHandle functions = Gdx.files.external(location).child("functions.json");
        functions.writeString(json.prettyPrint(functionManager), false);

        FileHandle scenes = Gdx.files.external(location).child("scenes.json");
        jsonManger.save(sceneManager, scenes);

    }

    public BaseCodeExecution getCodeExecution() {
        return codeExecution;
    }

    public BaseCodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

    public VariableManager getVariableManager() {
        return variableManager;
    }

    public FunctionManager getFunctionManager() {
        return functionManager;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public String getName() {
        return name;
    }

    public UIScene getUiScene() {
        return uiScene;
    }

    public void runProgram() {
        String code = variableManager.gen(codeGenerator) +
                functionManager.gen(codeGenerator) +
                programStart.gen(codeGenerator);

        System.out.println(code);
        codeExecution.setCode(code);
        codeExecution.run();
    }

    @Override
    public void write(Json json) {

    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }

    public ProjectScreen getProjectScreen() {
        return projectScreen;
    }

    public void setProgramStart(ProgramStart programStart) {
        this.programStart = programStart;
    }

    public interface ProgramStart {
        String gen(BaseCodeGenerator builder);
    }
}
