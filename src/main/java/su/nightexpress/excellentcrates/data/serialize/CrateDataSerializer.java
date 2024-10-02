package su.nightexpress.excellentcrates.data.serialize;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import su.nightexpress.excellentcrates.data.impl.CrateData;
import su.nightexpress.excellentcrates.data.impl.LimitData;

import java.lang.reflect.Type;
import java.util.Map;

public class CrateDataSerializer implements JsonSerializer<CrateData>, JsonDeserializer<CrateData> {

    @Override
    public CrateData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        long openCooldown = object.get("openCooldown").getAsLong();
        int openings = object.get("openings").getAsInt();
        int milestones = object.get("milestones").getAsInt();
        Map<String, LimitData> winDataMap = context.deserialize(object.get("winDataMap"), new TypeToken<Map<String, Map<String, LimitData>>>(){}.getType());

        return new CrateData(openCooldown, openings, milestones, winDataMap);
    }

    @Override
    public JsonElement serialize(CrateData crateData, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("openCooldown", crateData.getOpenCooldown());
        object.addProperty("openings", crateData.getOpenings());
        object.addProperty("milestones", crateData.getMilestone());
        object.add("winDataMap", context.serialize(crateData.getRewardDataMap()));

        return object;
    }
}
