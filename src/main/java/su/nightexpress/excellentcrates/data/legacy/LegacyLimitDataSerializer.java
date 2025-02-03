package su.nightexpress.excellentcrates.data.legacy;

import com.google.gson.*;

import java.lang.reflect.Type;

public class LegacyLimitDataSerializer implements JsonSerializer<LegacyLimitData>, JsonDeserializer<LegacyLimitData> {

    @Override
    public LegacyLimitData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        int amount = object.get("amount").getAsInt();
        long expireDate = object.get("expireDate").getAsLong();

        return new LegacyLimitData(amount, expireDate);
    }

    @Override
    public JsonElement serialize(LegacyLimitData data, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("amount", data.getAmount());
        object.addProperty("expireDate", data.getExpireDate());

        return object;
    }
}
