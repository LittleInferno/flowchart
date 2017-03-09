package com.littleinferno.flowchart.project;

import com.badlogic.gdx.files.FileHandle;
import com.littleinferno.flowchart.JsonManger;
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
        this.functionManager = new FunctionManager(this);
        this.sceneManager = new SceneManager(this);
        this.jsonManger = new JsonManger();

        this.uiScene = new UIScene(this);
        //   uiScene.init();
        this.projectScreen = new ProjectScreen(uiScene);
    }

    public Project(ProjectHandle projectHandle) {
        instance = this;
        this.name = projectHandle.name;
        this.location = projectHandle.location;

        jsonManger = new JsonManger();
        initJsonManager();

        this.uiScene = new UIScene(this);

        variableManager = new VariableManager(this,
                jsonManger.loadHandle(VariableManager.VariableManagerHandle.class,
                        location.child("variables.json")));

        functionManager = new FunctionManager(this,
                jsonManger.loadHandle(FunctionManager.FunctionManagerHandle.class,
                        location.child("functions.json")));

        sceneManager = instance.jsonManger.load(SceneManager.class, location.child("scenes.json"));

        uiScene.init();

        codeGenerator = new JSCodeGenerator();
        codeExecution = new JSCodeExecution();
        codeExecution.init();

        MainScene scene = sceneManager.getMainScene();
        uiScene.pinToTabbedPane(scene.getUiTab());

        this.projectScreen = new ProjectScreen(uiScene);
    }

    private void initJsonManager() {
        jsonManger.addSerializer(NodeManager.class, new NodeManager.NodeManagerSerializer());
        jsonManger.addSerializer(SceneManager.class, new SceneManager.SceneManagerSerializer());

    }

    public static Project createProject(String name, FileHandle location, BaseCodeGenerator codeGenerator, BaseCodeExecution codeExecution) {
        if (instance != null)
            instance.save();

        instance = new Project(name, location, codeGenerator, codeExecution);
        instance.init();

        MainScene scene = instance.getSceneManager().getMainScene();
        instance.uiScene.pinToTabbedPane(scene.getUiTab());

        return instance;
    }

    public static void load(String name, FileHandle location) {
        if (instance != null)
            instance.save();

        new Project(new ProjectHandle(name, null, null, location.child(name)));
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

    public JsonManger getJsonManger() {
        return jsonManger;
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

    public FileHandle getLocation() {
        return location;
    }

    @Override
    public void dispose() {
        sceneManager.dispose();
        variableManager.dispose();
        functionManager.dispose();
    }

    public interface ProgramStart {
        String gen(BaseCodeGenerator builder);
    }

    public static class ProjectHandle {
        public String name;
        public String codeGenerator;
        public String codeExecution;
        public FileHandle location;

        public ProjectHandle() {
        }

        public ProjectHandle(String projectName, String codeGenerator, String codeExecution, FileHandle location) {
            this.name = projectName;
            this.codeGenerator = codeGenerator;
            this.codeExecution = codeExecution;
            this.location = location;
        }
    }
}
