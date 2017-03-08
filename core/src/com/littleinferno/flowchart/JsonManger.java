package com.littleinferno.flowchart;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class JsonManger {

    private Json json;

    public JsonManger() {
        json = new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public <T, U extends Json.Serializer<T>> void addSerializer(Class<T> type, U serializer) {
        json.setSerializer(type, serializer);
    }

    public <T> String save(T object) {
        return json.prettyPrint(object);
    }

    public <T> void save(T object, FileHandle fileHandle) {
        fileHandle.writeString(save(object), false);
    }

    public <T> T load(Class<T> type, String string) {
        return json.fromJson(type, string);
    }

    public <T> T load(Class<T> type, FileHandle fileHandle) {
        return load(type, fileHandle.readString());
    }

    public <T> T loadHandle(Class<T> type, FileHandle fileHandle) {
        return json.fromJson(type, fileHandle.readString());
    }
}
