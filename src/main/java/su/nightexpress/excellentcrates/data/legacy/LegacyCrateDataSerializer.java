package su.nightexpress.excellentcrates.data.legacy;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class LegacyCrateDataSerializer implements JsonSerializer<LegacyCrateData>, JsonDeserializer<LegacyCrateData> {

    @Override
    public LegacyCrateData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        Map<String, LegacyLimitData> winDataMap = context.deserialize(object.get("winDataMap"), new TypeToken<Map<String, LegacyLimitData>>(){}.getType());

        return new LegacyCrateData(winDataMap);
    }

    @Override
    public JsonElement serialize(LegacyCrateData crateData, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.add("winDataMap", context.serialize(crateData.getRewardDataMap()));

        return object;
    }
}
