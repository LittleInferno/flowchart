package com.littleinferno.flowchart.function;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.node.FunctionBeginNode;
import com.littleinferno.flowchart.node.FunctionReturnNode;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.ui.Main;
import com.littleinferno.flowchart.util.DestroyListener;
import com.littleinferno.flowchart.util.NameChangedListener;

import java.util.ArrayList;
import java.util.List;


public class Function {

    private String name;

    private FunctionBeginNode beginNode;
    private List<FunctionReturnNode> returnNodes;

    private List<FunctionParameter> parameters;

    private List<NameChangedListener> nameChangedListeners;
    private List<DestroyListener> destroyListeners;

    private List<FunctionParameter.Added> parameterAddedListeners;
    private List<FunctionParameter.Removed> parameterRemovedListeners;



    public Function(String name) {
        this.name = name;

        parameters = new ArrayList<>();

        nameChangedListeners = new ArrayList<>();
        destroyListeners = new ArrayList<>();

        parameterAddedListeners = new ArrayList<>();
        parameterRemovedListeners = new ArrayList<>();


        //   Table functionWindow = Main.addWindow(name).getContentTable();
//        functionWindow.addActor(beginNode);

        beginNode = new FunctionBeginNode(this, Main.skin);
        beginNode.setPosition(100, 100);

        returnNodes = new ArrayList<>();

        // FunctionReturnNode returnNode = new FunctionReturnNode(this, Main.skin);
        //  returnNode.setPosition(400, 100);
        //   returnNodes.add(returnNode);



    }

    public FunctionBeginNode getBeginNode() {
        return beginNode;
    }

    public List<FunctionParameter> getParameters() {
        return parameters;
    }

    public List<FunctionReturnNode> getReturnNodes() {
        return returnNodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        notifyListenersNameChanged(name);
    }

    public void removeNode(Node node) {

        if (node instanceof FunctionBeginNode)
            beginNode = null;
        else if (node instanceof FunctionReturnNode)
            returnNodes.remove(node);
    }

    public void addParameter(FunctionParameter parameter) {
        notifyListenersParameterAdded(parameter);
        parameters.add(parameter);
    }

    public void removeParameter(FunctionParameter parameter) {
        notifyListenersParameterRemoved(parameter);
        parameters.remove(parameter);
    }

    public void addListener(NameChangedListener listener) {
        nameChangedListeners.add(listener);
    }

    public void addListener(DestroyListener listener) {
        destroyListeners.add(listener);
    }

    public void addPareameterListener(FunctionParameter.Added added, FunctionParameter.Removed removed) {
        parameterAddedListeners.add(added);
        parameterRemovedListeners.add(removed);
    }

    private void notifyListenersNameChanged(String newName) {
        Stream.of(nameChangedListeners).forEach(var -> var.changed(newName));
    }

    private void notifyListenersDestroed() {
        Stream.of(destroyListeners).forEach(DestroyListener::destroyed);
    }

    private void notifyListenersParameterAdded(FunctionParameter parameter) {
        Stream.of(parameterAddedListeners).forEach(var -> var.add(parameter));
    }

    private void notifyListenersParameterRemoved(FunctionParameter parameter) {
        Stream.of(parameterRemovedListeners).forEach(var -> var.remove(parameter));
    }

    public void addReturnNode(FunctionReturnNode returnNode) {
        returnNodes.add(returnNode);
    }

    public void applyParameters() {
        if (!parameterAddedListeners.isEmpty()) {
            FunctionParameter.Added listener = parameterAddedListeners.get(parameterAddedListeners.size() - 1);
            Stream.of(parameters).forEach(listener::add);
        }
    }


    public void delete() {

        // TODO
        //  removeNode(beginNode);
        notifyListenersDestroed();
//
//        for (Iterator<FunctionReturnNode> i = returnNodes.iterator(); i.hasNext(); ) {
//            Node node = i.next();
//            node.close();
//            i.remove();
//        }
//
//        for (Iterator<FunctionCallNode> i = callNodes.iterator(); i.hasNext(); ) {
//            Node node = i.next();
//            node.close();
//            i.remove();
//        }
    }






}
