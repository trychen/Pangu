package cn.mccraft.pangu.core.util.data;

import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.Load;
import com.google.gson.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@AutoWired
public enum JsonPersistence implements Persistence {
    INSTANCE;

    private Gson gson;
    private Charset charset = StandardCharsets.UTF_8;

    @Load
    public void createGsonInstance() {
        GsonBuilder builder = new GsonBuilder();
        MinecraftForge.EVENT_BUS.post(new GsonCreateEvent(builder));
        this.gson = builder.create();
    }

    @Override
    public byte[] serialize(String[] parameterNames, Object[] objects, Type[] types) throws IOException {
        assert parameterNames.length == objects.length && objects.length == types.length : "Argument's length must be the same";

        JsonObject jsonObject = new JsonObject();
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i]; if (object == null) continue;
            String name = parameterNames[i];
            Type type = types[i];

            JsonElement json = gson.toJsonTree(object, type);
            jsonObject.add(name, json);
        }

        return jsonObject.toString().getBytes(charset);
    }

    @Override
    public Object[] deserialize(String[] parameterNames, byte[] bytes, Type[] types) throws IOException {
        String json = new String(bytes, charset);
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        Object[] objects = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            String name = parameterNames[i];
            JsonElement element = jsonObject.get(name);
            if (element != null) {
                objects[i] = gson.fromJson(element, types[i]);
            }
        }
        return objects;
    }

    @AllArgsConstructor
    public static class GsonCreateEvent extends Event {
        @Getter
        @Setter
        private GsonBuilder builder;
    }
}
