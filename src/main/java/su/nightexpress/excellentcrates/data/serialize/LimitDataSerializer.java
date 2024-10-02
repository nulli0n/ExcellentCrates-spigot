package su.nightexpress.excellentcrates.data.serialize;

import com.google.gson.*;
import su.nightexpress.excellentcrates.data.impl.LimitData;

import java.lang.reflect.Type;

public class LimitDataSerializer implements JsonSerializer<LimitData>, JsonDeserializer<LimitData> {

    @Override
    public LimitData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        int amount = object.get("amount").getAsInt();
        long expireDate = object.get("expireDate").getAsLong();

        return new LimitData(amount, expireDate);
    }

    @Override
    public JsonElement serialize(LimitData data, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("amount", data.getAmount());
        object.addProperty("expireDate", data.getExpireDate());

        return object;
    }
}
