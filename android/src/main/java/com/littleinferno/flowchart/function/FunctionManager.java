package com.littleinferno.flowchart.function;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.project.ProjectModule;
import com.littleinferno.flowchart.util.Fun;
import com.littleinferno.flowchart.util.Generator;
import com.littleinferno.flowchart.util.Link;

import java.util.ArrayList;
import java.util.List;

public class FunctionManager extends ProjectModule implements Generator, Parcelable {

    public static final String TAG = "FUNCTION_MANAGER";

    private List<Function> functions;
    private int counter;
    private final List<Link> functionRemoveListeners = new ArrayList<>();
    private final List<Link> functionAddListeners = new ArrayList<>();

    public FunctionManager(final Project project) {
        super(project);
        functions = new ArrayList<>();
        counter = 0;
    }

    private FunctionManager(Parcel in) {
        super(in);
        counter = in.readInt();

        functions = new ArrayList<>();
        in.readList(functions, Function.class.getClassLoader());
    }

    public static final Creator<FunctionManager> CREATOR = new Creator<FunctionManager>() {
        @Override
        public FunctionManager createFromParcel(Parcel in) {
            return new FunctionManager(in);
        }

        @Override
        public FunctionManager[] newArray(int size) {
            return new FunctionManager[size];
        }
    };

    public Function createFunction(String name) {
        Function function = new Function(this, name);
        functions.add(function);

        notifyFunctionAdd();
        return function;
    }

    public Function createFunction(Function.SimpleObject saveInfo) {
        Function function = new Function(this, saveInfo);
        functions.add(function);

        notifyFunctionAdd();
        return function;
    }

    public Function createMain() {
        Function function = new Function(this, getProject().getRules().getEntryPoint());
        functions.add(0, function);

        notifyFunctionAdd();
        return function;
    }

    void removeFunction(Function function) {
        function.destroy();
        notifyFunctionRemove();
        functions.remove(function);
    }

    public void removeFunction(int function) {
        removeFunction(functions.get(function));
    }

    public Function getFunction(String name) {

        Optional<Function> function = Stream.of(functions)
                .filter(value -> value.getName().equals(name))
                .findFirst();

        if (!function.isPresent()) {
            String entryPoint = getProject().getRules().getEntryPoint();
            if (name.equals(entryPoint)) {
                return createFunction(entryPoint);
            } else
                throw new RuntimeException("Cannot find functin with title:\"" + name + "\"");
        }
        return function.get();
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public String checkFunctionName(final String name) {

        if (name.isEmpty()) {
            return "Name can not be empty";
        } else if (!getProject().getRules().checkPattern(name)) {
            return "Unacceptable symbols";
        } else if (Stream.of(functions).map(Function::getName).anyMatch(name::equals)) {
            return "This name is already taken";
        } else if (getProject().getRules().containsWord(name))
            return "Invalid name";

        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(counter);
        dest.writeList(functions);
    }

    public Link onFunctionAdd(Fun fun) {
        Link link = new Link(functionAddListeners, fun);
        functionAddListeners.add(link);
        return link;
    }

    public Link onFunctionRemove(Fun fun) {
        Link link = new Link(functionRemoveListeners, fun);
        functionRemoveListeners.add(link);
        return link;
    }

    private void notifyFunctionAdd() {
        Stream.of(functionAddListeners).forEach(Link::call);
    }

    private void notifyFunctionRemove() {
        Stream.of(functionRemoveListeners).forEach(Link::call);
    }

    @Override
    public String generate() {
        return Stream.of(functions).map(Function::generate).collect(Collectors.joining("\n"));
    }

    public Function getFunction(int pos) {
        return functions.get(pos);
    }
}
