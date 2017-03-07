package com.littleinferno.flowchart.project;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kotcrab.vis.ui.VisUI;
import com.littleinferno.flowchart.JsonManger;
import com.littleinferno.flowchart.Setting;
import com.littleinferno.flowchart.codegen.BaseCodeExecution;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.codegen.JSCodeExecution;
import com.littleinferno.flowchart.codegen.JSCodeGenerator;
import com.littleinferno.flowchart.function.FunctionManager;
import com.littleinferno.flowchart.gui.ProjectScreen;
import com.littleinferno.flowchart.gui.UIScene;
import com.littleinferno.flowchart.node.NodeManager;
import com.littleinferno.flowchart.scene.MainScene;
import com.littleinferno.flowchart.scene.Scene;
import com.littleinferno.flowchart.scene.SceneManager;
import com.littleinferno.flowchart.util.ProjectException;
import com.littleinferno.flowchart.util.managers.BaseManager;
import com.littleinferno.flowchart.variable.VariableManager;

import java.util.ArrayList;
import java.util.UUID;

public class Project extends BaseManager {

    private static Project instance;

    private String name;
    private FileHandle location;

    private BaseCodeExecution codeExecution;
    private BaseCodeGenerator codeGenerator;
    private ProgramStart programStart;

    private VariableManager variableManager;
    private FunctionManager functionManager;
    private SceneManager sceneManager;
    private JsonManger jsonManger;

    private UIScene uiScene;

    private ProjectScreen projectScreen;

    public Project(String name, FileHandle location, BaseCodeGenerator codeGenerator, BaseCodeExecution codeExecution) {
        this.name = name;
        this.location = location.child(name);

        if (!this.location.exists())
            this.location.mkdirs();

        this.codeGenerator = codeGenerator;
        this.codeExecution = codeExecution;

        this.variableManager = new VariableManager(this);
        this.functionManager = new FunctionManager();
        this.sceneManager = new SceneManager();
        this.jsonManger = new JsonManger();

        this.uiScene = new UIScene();
        this.projectScreen = new ProjectScreen(uiScene);
    }

    private void initJsonManager() {
        jsonManger.addSerializer(NodeManager.class, new NodeManager.NodeManagerSerializer());
        jsonManger.addSerializer(SceneManager.class, new SceneManager.SceneManagerSerializer());

    }

    public static Project createProject(String name, FileHandle location, BaseCodeGenerator codeGenerator, BaseCodeExecution codeExecution) {
        if (instance != null)
            instance.save();

        if (!VisUI.isLoaded()) {
            if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android)
                VisUI.load("X2/uiskin.json");
            else
                VisUI.load("X1/uiskin.json");
        }

        instance = new Project(name, location, codeGenerator, codeExecution);
        instance.init();

        MainScene scene = instance.getSceneManager().getMainScene();
        instance.uiScene.pinToTabbedPane(scene.getUiTab());
        instance.uiScene.controlTable.init();

        return instance;
    }

    public static void load(String name, FileHandle location) {
        if (instance != null)
            instance.save();

        if (!VisUI.isLoaded()) {
            if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android)
                VisUI.load("X2/uiskin.json");
            else
                VisUI.load("X1/uiskin.json");
        }

        instance = new Project(name, location, new JSCodeGenerator(), new JSCodeExecution());
        instance.init();

        FileHandle variables = instance.getLocation().child("variables.json");
        instance.variableManager = new VariableManager(instance,
                instance.jsonManger.loadHandle(VariableManager.VariableManagerHandle.class, variables));

        FileHandle functions = instance.getLocation().child("functions.json");
        instance.functionManager = new FunctionManager(
                instance.jsonManger.loadHandle(FunctionManager.FunctionManagerHandle.class, functions));

        FileHandle scenes = instance.getLocation().child("scenes.json");
        instance.sceneManager = instance.jsonManger.load(SceneManager.class, scenes);

        MainScene scene = instance.getSceneManager().getMainScene();
        instance.uiScene.pinToTabbedPane(scene.getUiTab());

        instance.uiScene.controlTable.init();
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
        FileHandle variables = location.child("variables.json");
        jsonManger.save(variableManager.getHandle(), variables);

        FileHandle functions = location.child("functions.json");
        jsonManger.save(functionManager.getHandle(), functions);

        FileHandle scenes = location.child("scenes.json");
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

    public Scene getCurrentScene() {
        return uiScene.getShow();
    }

    public static UUID createID() {
        return UUID.randomUUID();
    }

    public ProjectScreen getProjectScreen() {
        return projectScreen;
    }

    public void setProgramStart(ProgramStart programStart) {
        this.programStart = programStart;
    }

    public static ArrayList<String> getProjectsNames() {
        return Stream.of(Gdx.files.external(Setting.projectsLocation).list())
                .map(FileHandle::name)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public FileHandle getLocation() {
        return location;
    }

    @Override
    public void dispose() {

    }

    public interface ProgramStart {
        String gen(BaseCodeGenerator builder);
    }
}
