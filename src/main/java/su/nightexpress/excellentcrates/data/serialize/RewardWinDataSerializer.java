package su.nightexpress.excellentcrates.data.serialize;

import com.google.gson.*;
import su.nightexpress.excellentcrates.data.impl.RewardWinData;

import java.lang.reflect.Type;

public class RewardWinDataSerializer implements JsonSerializer<RewardWinData>, JsonDeserializer<RewardWinData> {

    @Override
    public RewardWinData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        int amount = object.get("amount").getAsInt();
        long expireDate = object.get("expireDate").getAsLong();

        return new RewardWinData(amount, expireDate);
    }

    @Override
    public JsonElement serialize(RewardWinData winData, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("amount", winData.getAmount());
        object.addProperty("expireDate", winData.getExpireDate());

        return object;
    }
}
