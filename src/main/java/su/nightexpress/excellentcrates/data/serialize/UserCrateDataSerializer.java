package su.nightexpress.excellentcrates.data.serialize;

import com.google.gson.*;
import su.nightexpress.excellentcrates.data.crate.UserCrateData;

import java.lang.reflect.Type;

public class UserCrateDataSerializer implements JsonSerializer<UserCrateData>, JsonDeserializer<UserCrateData> {

    @Override
    public UserCrateData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        long openCooldown = object.get("openCooldown").getAsLong();
        int openings = object.get("openings").getAsInt();
        int milestones = object.get("milestones").getAsInt();

        return new UserCrateData(openCooldown, openings, milestones);
    }

    @Override
    public JsonElement serialize(UserCrateData crateData, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("openCooldown", crateData.getOpenCooldown());
        object.addProperty("openings", crateData.getOpenings());
        object.addProperty("milestones", crateData.getMilestone());

        return object;
    }
}
