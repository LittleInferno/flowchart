package com.littleinferno.flowchart.function;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.generator.Generator;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.project.ProjectModule;
import com.littleinferno.flowchart.util.Fun;
import com.littleinferno.flowchart.util.Link;

import java.util.ArrayList;
import java.util.List;

public class AndroidFunctionManager extends ProjectModule implements Generator, Parcelable {

    public static final String TAG = "FUNCTION_MANAGER";

    private List<AndroidFunction> functions;
    private int counter;
    private final List<Link> functionRemoveListeners;
    private final List<Link> functionAddListeners;

    public AndroidFunctionManager(final FlowchartProject project) {
        super(project);
        functions = new ArrayList<>();
        counter = 0;

        functionRemoveListeners = new ArrayList<>();
        functionAddListeners = new ArrayList<>();
    }

    private AndroidFunctionManager(Parcel in) {
        super(in);
        counter = in.readInt();

        functions = new ArrayList<>();
        in.readList(functions, AndroidFunction .class.getClassLoader());

        functionAddListeners = new ArrayList<>();
        in.readList(functionAddListeners, Fun.class.getClassLoader());

        functionRemoveListeners = new ArrayList<>();
        in.readList(functionRemoveListeners, Fun.class.getClassLoader());
    }

    public static final Creator<AndroidFunctionManager> CREATOR = new Creator<AndroidFunctionManager>() {
        @Override
        public AndroidFunctionManager createFromParcel(Parcel in) {
            return new AndroidFunctionManager(in);
        }

        @Override
        public AndroidFunctionManager[] newArray(int size) {
            return new AndroidFunctionManager[size];
        }
    };

    public AndroidFunction createFunction(String name) {
        AndroidFunction function = new AndroidFunction(this, name);
        functions.add(function);

        notifyFunctionAdd();
        return function;
    }

    public AndroidFunction createFunction(AndroidFunction.SimpleObject saveInfo) {
        AndroidFunction function = new AndroidFunction(this, saveInfo);
        functions.add(function);

        notifyFunctionAdd();
        return function;
    }

    public AndroidFunction createMain() {
        AndroidFunction function = new AndroidFunction(this, getProject().getRules().getEntryPoint());
        functions.add(0, function);

        notifyFunctionAdd();
        return function;
    }

    public AndroidFunction createFunction() {
        AndroidFunction function = new AndroidFunction(this, "newFun" + counter++);

        functions.add(function);
        return function;
    }

    void removeFunction(AndroidFunction function) {
        function.destroy();
        notifyFunctionRemove();
        functions.remove(function);
    }

    public AndroidFunction getFunction(String name) {

        Optional<AndroidFunction> function = Stream.of(functions)
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

    public List<AndroidFunction> getFunctions() {
        return functions;
    }

    public String checkFunctionName(final String name) {

        if (name.isEmpty()) {
            return "Name can not be empty";
        } else if (!getProject().getRules().checkPattern(name)) {
            return "Unacceptable symbols";
        } else if (Stream.of(functions).map(AndroidFunction::getName).anyMatch(name::equals)) {
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
        dest.writeList(functionAddListeners);
        dest.writeList(functionRemoveListeners);
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
        return Stream.of(functions).map(AndroidFunction::generate).collect(Collectors.joining("\n"));
    }
}
